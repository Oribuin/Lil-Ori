package xyz.oribuin.lilori.managers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackManager {

    public List<AudioTrack> trackList = new ArrayList<>();

    private static TrackManager instance;
    public final AudioPlayerManager playerManager;
    public final Map<String, GuildMusicManager> musicManagers;
    private final Guild guild;
    
    public TrackManager() {
        thi   
    }

    public TrackManager(Guild guild) {
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        this.playerManager.registerSourceManager(new HttpAudioSourceManager());
        this.playerManager.registerSourceManager(new LocalAudioSourceManager());

        this.guild = guild;
        musicManagers = new HashMap<>();
        instance = this;
    }

    public GuildMusicManager getMusicManager() {

        GuildMusicManager musicManager = musicManagers.get(guild.getId());
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guild.getId(), musicManager);
        }

        musicManager.getAudioManager(guild).setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }
    
    public static synchronized TrackManager getInstance() {
        return instance;
    }

    public void loadAndPlay(TextChannel textChannel, String trackUrl, boolean addPlaylist) {
        playerManager.loadItemOrdered(this.getMusicManager(), trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getMusicManager().scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                List<AudioTrack> trackList = playlist.getTracks();

                if (firstTrack == null) {
                    firstTrack = trackList.get(0);
                }

                if (addPlaylist) {
                    trackList.forEach(getMusicManager().scheduler::queue);
                    return;
                }

                getMusicManager().scheduler.queue(firstTrack);
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

    public AudioPlaylist getPlaylist() {

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
                return getMusicManager().player.getPlayingTrack();
            }

            @Override
            public boolean isSearchResult() {
                return false;
            }
        };
    }

    public TrackScheduler getTrackScheduler() {
        return new TrackScheduler(this.getMusicManager().player);
    }
}
