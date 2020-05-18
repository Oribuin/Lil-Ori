package xyz.oribuin.lilori.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CmdGay extends Command {

    public CmdGay() {
        this.name = "Gay";
        this.description = "How gay are you?";
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.MESSAGE_ADD_REACTION};
    }

    @Override
    public void executeCommand(CommandEvent event) {
        int upperBound = 100;
        int lowerBound = 1;
        int randomBound = new Random().nextInt(upperBound - lowerBound - 1) + lowerBound;

        event.getChannel().sendMessage("Calculating your gayness :rainbow_flag:").queue(message -> {

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor("Gay Calculator")
                    .setColor(Color.decode("#33539e"))
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("**You are " + randomBound + "% Gay** :rainbow_flag:");

            event.getChannel().sendMessage(embedBuilder.build()).queueAfter(2, TimeUnit.SECONDS);
            message.delete().queueAfter(2, TimeUnit.SECONDS);
        });
    }
}
