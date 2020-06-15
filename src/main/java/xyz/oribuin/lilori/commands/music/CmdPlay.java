package xyz.oribuin.lilori.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;
import xyz.oribuin.lilori.managers.music.TrackScheduler;

import java.awt.*;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CmdPlay extends Command {

    public CmdPlay() {
        this.name = "Play";
        this.description = "Play Music";
        this.aliases = Collections.emptyList();
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    public void executeCommand(CommandEvent event) {
        TrackManager trackManager = new TrackManager();
        GuildMusicManager musicManager = trackManager.getGuildAudioPlayer(event.getGuild());
        TrackScheduler trackScheduler = new TrackScheduler(musicManager.player);

        String[] args = event.getMessage().getContentRaw().split(" ");

        if (event.getMember().getVoiceState() == null && !event.getMember().getVoiceState().inVoiceChannel()) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", You must be in a voice channel to execute this command.", 10, TimeUnit.SECONDS);
            return;
        }

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please input a url for the music.", 10, TimeUnit.SECONDS);
            return;
        }

        if (musicManager.player.getPlayingTrack() == null) {
            String input = event.getMessage().getContentRaw().substring(args[0].length() + 1);



            event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
            trackManager.loadAndPlay((TextChannel) event.getChannel(), input);
            AudioTrack track = musicManager.player.getPlayingTrack();
            trackScheduler.onTrackEnd(musicManager.player, track, AudioTrackEndReason.FINISHED);

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

            event.reply(playEmbed.build());
            event.getMessage().delete().queue();
            return;
        }
    }
}
