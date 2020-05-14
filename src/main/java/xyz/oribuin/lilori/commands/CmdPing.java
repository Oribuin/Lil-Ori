package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CmdPing extends Command {

    private String emoji;

    public CmdPing() {
        this.name = "Ping";
        this.aliases = new String[]{"latency"};
        this.description = "Get the latency ping for the bot.";
    }

    public void executeCommand(CommandEvent event) {
        long ping = event.getJDA().getGatewayPing();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if (ping < 101) {
            emoji = "<a:PanGLoveG:702322097195712523>";
            embedBuilder.setColor(Color.green);
        }

        // If Ping is Higher than 100, Do yellow
        if (ping > 100) {
            emoji = "<a:PanGLoveY:702322097292181534>";
            embedBuilder.setColor(Color.decode("#ffff00"));
        }

        // If Ping is Higher than 200 Do Orange
        if (ping > 199) {
            emoji = "<a:PanGLoveO:702322097002643577>";
            embedBuilder.setColor(Color.decode("#ffa500"));
        }

        // If Ping is higher than 300, Do Red
        if (ping > 299) {
            emoji = "<a:PanGLove:702322097979916298>";
            embedBuilder.setColor(Color.red);
        }

        embedBuilder.setDescription("Pinging Lil' Ori");
        event.getChannel().sendMessage(embedBuilder.build()).queue(message -> {
            EmbedBuilder newEmbed = new EmbedBuilder()
                    .setColor(message.getEmbeds().get(0).getColor())
                    .setDescription("**" + ping + "ms latency** " + emoji);

            message.editMessage(newEmbed.build()).queueAfter(1500, TimeUnit.NANOSECONDS);

        });
    }
}