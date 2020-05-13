package xyz.oribuin.lilori.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CmdGay extends Command {

    public CmdGay() {
        this.name = "Gay";
        this.category = new Category("Games");
        this.arguments = "";
        this.description = "How gay are you?";
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.MESSAGE_ADD_REACTION};
    }

    @Override
    protected void execute(CommandEvent event) {
        int upperBound = 100;
        int lowerBound = 1;
        int randomBound = new Random().nextInt(upperBound - lowerBound - 1) + lowerBound;

        event.getChannel().sendMessage("Calculating your gayness :rainbow_flag:").queue(message -> {

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor("Gay Calculator")
                    .setColor(Color.decode("#33539e"))
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("**You are " + randomBound + "% Gay** :rainbow_flag:");

            message.editMessage(embedBuilder.build()).queueAfter(2, TimeUnit.SECONDS);
        });
    }
}
