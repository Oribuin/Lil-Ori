package xyz.oribuin.lilori.managers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private AudioPlayer player;
    private AudioPlayerManager playerManager;
    private final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player, AudioPlayerManager playerManager) {
        this.player = player;
        this.playerManager = playerManager;
        this.queue = new LinkedBlockingQueue<>();

        if (player.getPlayingTrack() == null)
            nextTrack();
        ;
    }


    public void loadTrack(String song) {
        playerManager.loadItem(song, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                System.out.println("Loaded! " + track.getInfo().title);
                player.startTrack(track, false);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                nextTrack();
            }

            @Override
            public void noMatches() {
                nextTrack();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
                nextTrack();
            }
        });
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, false))
            queue.offer(track);
    }


    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext)
            nextTrack();
    }
}
