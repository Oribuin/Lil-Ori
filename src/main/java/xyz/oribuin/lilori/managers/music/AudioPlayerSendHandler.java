package xyz.oribuin.lilori.managers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public boolean canProvide() {
        if (lastFrame == null)
            lastFrame = audioPlayer.provide();

        return lastFrame != null;
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        if (lastFrame == null)
            lastFrame = audioPlayer.provide();

        byte[] dataByte = lastFrame != null ? lastFrame.getData() : null;
        lastFrame = null;

        return ByteBuffer.wrap(dataByte);
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
