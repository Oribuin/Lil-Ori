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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackManager {

    private static TrackManager instance;
    public final AudioPlayerManager playerManager;
    public final Map<String, GuildMusicManager> musicManagers;
    private final Guild guild;
    public List<AudioTrack> trackList = new ArrayList<>();

    private TrackManager(Guild guild) {
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        this.playerManager.registerSourceManager(new HttpAudioSourceManager());
        this.playerManager.registerSourceManager(new LocalAudioSourceManager());

        this.guild = guild;
        musicManagers = new HashMap<>();
    }

    public static TrackManager getInstance(Guild guild) {
        if (instance == null) {
            instance = new TrackManager(guild);
        }
        return instance;
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

    public void loadAndPlay(Member author, TextChannel textChannel, String trackUrl, boolean addPlaylist) {
        playerManager.loadItemOrdered(this.getMusicManager(), trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                // Message Here
                long totalSeconds = track.getDuration() / 1000;
                totalSeconds %= 3600;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor("\uD83C\uDFB5 Now Playing " + track.getInfo().title)
                        .setColor(Color.RED)
                        .setDescription("**Song URL**\n" +
                                track.getInfo().uri + "\n \n" +
                                "**Song Duration**\n" +
                                minutes + " minutes & " + seconds + " seconds\n \n")
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png");

                textChannel.sendMessage(author.getAsMention()).embed(embedBuilder.build()).queue();
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
                    textChannel.sendMessage(author.getAsMention() + ", Adding " + playlist.getTracks().size() +  " to the playlist").queue();
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
