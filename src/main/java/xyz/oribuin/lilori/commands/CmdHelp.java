package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.util.Collections;

public class CmdHelp extends Command {

    public CmdHelp() {
        this.name = "Help";
        this.aliases = Collections.singletonList("Support");
        this.description = "Get the list of commands for the bot.";
    }

    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Lil' Ori Help Menu")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png");


        if (args.length < 2) {
            embedBuilder.setDescription("» Info - " + event.getPrefix() + "**help info** «\n" +
                    "» Music - " + event.getPrefix() + "**help music** «\n" +
                    "» Games - " + event.getPrefix() + "**help games** «\n" +
                    "» Moderation - " + event.getPrefix() + "**help moderation** «\n" +
                    "» Admin - " + event.getPrefix() + "**help admin**"
            );

            event.reply(embedBuilder);
            return;
        }

        switch (args[1].toLowerCase()) {
            case "info":
                embedBuilder.setDescription("» Color Command - " + event.getPrefix() + "**color <#hex/rgb>**\n" +
                        "» Help Command - " + event.getPrefix() + "**help** «\n" +
                        "» Ping Command - " + event.getPrefix() + "**ping** «");
                break;

            case "music":
                embedBuilder.setDescription("» Loop Command (WIP)- " + event.getPrefix() + "**loop** «\n" +
                        "» Pause Command - " + event.getPrefix() + "**pause** «\n" +
                        "» Play Command - " + event.getPrefix() + "**play <Youtube-URL>** «\n" +
                        "» Stop Command - " + event.getPrefix() + "**stop** «\n" +
                        "» Volume Command - " + event.getPrefix() + "**volume <Volume>** «");
                break;

            case "games":
                embedBuilder.setDescription("» Coinflip Command - " + event.getPrefix() + "**coinflip** «\n" +
                        "» Eightball Command - " + event.getPrefix() + "**ball <question>** «\n" +
                        "» Feed Command - " + event.getPrefix() + "**feed** «\n" +
                        "» Gay Command - " + event.getPrefix() + "**gay** «\n" +
                        "» Quote Command - " + event.getPrefix() + "**quote** «\n" +
                        "» Slap Command - " + event.getPrefix() + "**slap <users>** «");
                break;

            case "moderation":
                embedBuilder.setDescription("» Ban Command - " + event.getPrefix() + "**ban <@User> <Messages> <Reason>**\n" +
                        "» Kick Command - " + event.getPrefix() + "**<@User> <Reason>** «\n" +
                        "» Mute Command - " + event.getPrefix() + "**mute <Setup/@User>** «\n" +
                        "» Purge Command - " + event.getPrefix() + "**purge <Channel/Msgs/User> <#Channel/Number/@User>** «");
                break;

            case "admin":
                embedBuilder.setDescription("» Prefix Command - " + event.getPrefix() + "**prefix <prefix>** «\n" +
                        "» Perm Command - " + event.getPrefix() + "**perms**");
                break;

            default:
                event.reply(event.getAuthor().getAsMention() + ", Please use the correct arguments.");
                return;
        }

        event.reply(embedBuilder);

    }
}