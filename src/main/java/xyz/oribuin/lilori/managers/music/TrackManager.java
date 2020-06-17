package xyz.oribuin.lilori.managers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackManager {

    public List<AudioTrack> trackList = new ArrayList<>();

    public final AudioPlayerManager playerManager;
    public final Map<String, GuildMusicManager> musicManagers;

    public TrackManager() {
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        this.playerManager.registerSourceManager(new HttpAudioSourceManager());
        this.playerManager.registerSourceManager(new LocalAudioSourceManager());

        musicManagers = new HashMap<>();
    }

    private void connectChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && audioManager.getGuild().getSelfMember().getVoiceState() != null) {
            audioManager.openAudioConnection(audioManager.getGuild().getSelfMember().getVoiceState().getChannel());
        }
    }

    public GuildMusicManager getGuildAudioPlayer(Guild guild) {

        GuildMusicManager musicManager = musicManagers.get(guild.getId());
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guild.getId(), musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(TextChannel textChannel, String trackUrl, boolean addPlaylist) {
        GuildMusicManager musicManager = this.getGuildAudioPlayer(textChannel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                List<AudioTrack> trackList = playlist.getTracks();

                if (firstTrack == null) {
                    firstTrack = trackList.get(0);
                }

                if (addPlaylist) {
                    trackList.forEach(musicManager.scheduler::queue);
                    return;
                }

                musicManager.scheduler.queue(firstTrack);
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

    private GuildMusicManager getMusicManager(Guild guild) {
        GuildMusicManager musicManager = musicManagers.get(guild.getId());
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManager.player.setVolume(50);
            musicManagers.put(guild.getId(), musicManager);
        }

        return musicManager;
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
