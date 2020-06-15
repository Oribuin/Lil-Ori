package xyz.oribuin.lilori.managers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.*;

public class TrackManager {

    public List<AudioTrack> trackList = new ArrayList<>();

    public final AudioPlayerManager playerManager;
    public final Map<Long, GuildMusicManager> musicManagerMap;

    public TrackManager() {
        this.musicManagerMap = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private void connectChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && audioManager.getGuild().getSelfMember().getVoiceState() != null) {
            audioManager.openAudioConnection(audioManager.getGuild().getSelfMember().getVoiceState().getChannel());
        }
    }

    public GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());

        GuildMusicManager musicManager = musicManagerMap.get(guildId);
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagerMap.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(TextChannel textChannel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());

        playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(textChannel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                textChannel.sendMessage("Added " + firstTrack.getInfo().title + " to queue.").queue();
                play(textChannel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                textChannel.sendMessage("Could not find " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                textChannel.sendMessage("Exception: " + exception.getMessage()).queue();
                exception.printStackTrace();
            }
        });
    }

    public void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectChannel(guild.getAudioManager());
        musicManager.scheduler.queue(track);
    }

    public void skipTrack(TextChannel textChannel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());
        musicManager.scheduler.nextTrack();
    }

    public AudioPlaylist getPlaylist(Guild guild) {
        GuildMusicManager musicManager = this.getGuildAudioPlayer(guild);

        return new AudioPlaylist() {
            @Override
            public String getName() {
                return guild.getName().toLowerCase() + "_playlist";
            }

            @Override
            public List<AudioTrack> getTracks() {
                return trackList;
            }

            @Override
            public AudioTrack getSelectedTrack() {
                return musicManager.player.getPlayingTrack();
            }

            @Override
            public boolean isSearchResult() {
                return false;
            }
        };
    }

    public void addSong(Guild guild, AudioTrack track) {
        this.getPlaylist(guild).getTracks().add(track);
    }

    public void removeSong(Guild guild, AudioTrack track) {
        this.getPlaylist(guild).getTracks().remove(track);
    }

    public void clearPlaylist(Guild guild) {
        this.getPlaylist(guild).getTracks().clear();
    }
}
