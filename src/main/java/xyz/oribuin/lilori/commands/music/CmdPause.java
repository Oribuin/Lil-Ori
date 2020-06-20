package xyz.oribuin.lilori.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.AudioPlayerSendHandler;
import xyz.oribuin.lilori.managers.music.TrackManager;

import java.awt.*;
import java.util.Collections;

public class CmdPause extends Command {

    public CmdPause() {
        this.name = "Pause";
        this.aliases = Collections.emptyList();
        this.description = "Pause the music";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        TrackManager tm = TrackManager.getInstance(event.getGuild());

        if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel()) {
            event.reply(event.getAuthor().getAsMention() + ", Could not change volume since you are not in the voice channel");
            return;
        }

        AudioPlayer player = tm.getMusicManager().player;

        String msg;

        if (player.isPaused()) {
            player.setPaused(false);
            msg = "Playback is no longer paused";
        } else {
            player.setPaused(true);
            msg = "Playback is now paused";
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("\uD83C\uDFB5 " + msg)
                .setColor(Color.RED)
                .setDescription("Type " + event.getPrefix() + "pause to pause/unpause!")
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png");

        event.getTextChannel().sendMessage(event.getAuthor().getAsMention()).embed(embedBuilder.build()).queue();
    }
}