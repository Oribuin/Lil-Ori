package xyz.oribuin.lilori.managers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TrackManager {

    public final AudioPlayerManager playerManager;
    public final Map<Long, GuildMusicManager> musicManagerMap;

    public TrackManager() {
        this.musicManagerMap = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());

        GuildMusicManager musicManager = musicManagerMap.get(guildId);
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagerMap.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(final TextChannel textChannel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(textChannel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                long totalSeconds = track.getDuration() / 1000;
                totalSeconds %= 3600;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                EmbedBuilder playEmbed = new EmbedBuilder()
                        .setAuthor("« Lil' Ori Music »")
                        .setColor(Color.decode("#33539e"))
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("Song: " + track.getInfo().title + " (" + minutes + ":" + seconds + ")\n" +
                                "Author: " + track.getInfo().author + "\n" +
                                "URL: " + track.getInfo().uri);

                textChannel.sendMessage(playEmbed.build()).queue();
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
        musicManager.scheduler.nextTrack();;
        textChannel.sendMessage("Skipped Track").queue();
    }

    private static void connectChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            audioManager.openAudioConnection(audioManager.getGuild().getSelfMember().getVoiceState().getChannel());
        }
    }
}
