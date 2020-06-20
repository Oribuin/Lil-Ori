package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.util.Collections;

public class CmdPrefix extends Command {

    public CmdPrefix() {
        this.name = "Prefix";
        this.description = "Change the bot permission";
        this.aliases = Collections.emptyList();
        this.arguments = Collections.singletonList("<prefix>");
        //this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length == 1) {
            event.reply(event.getAuthor().getAsMention() + ", Please provide the correct args! " + this.getName());
            return;
        }

        if (args[0].equalsIgnoreCase(event.getPrefix())) {
            event.reply(event.getAuthor().getAsMention() + ", That prefix is already set.");
            return;
        }

        if (event.getPrefix().length() > 1) {
            event.reply(event.getAuthor().getAsMention() + ", Due to current odd issues, you cannot have a prefix longer than 1 character");
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Changed Lil' Ori Prefix")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**Old Prefix** " + event.getPrefix() +
                        "\n\n**New Prefix:** " + args[1]);

        event.reply(embedBuilder);
        this.bot.getGuildSettingsManager().updateGuild(event.getGuild(), args[1]);
        System.out.println(event.getAuthor().getAsTag() + " Updated \"" + event.getGuild().getName() + "\" Prefix to " + args[1]);

    }
}
