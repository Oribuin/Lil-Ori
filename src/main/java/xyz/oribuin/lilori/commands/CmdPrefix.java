package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;

public class CmdPrefix extends Command {

    public CmdPrefix() {
        this.name = "Prefix";
        this.description = "Change the bot permission";
        this.arguments = new String[]{"<Prefix>"};
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length == 1) {
            event.reply(event.getAuthor().getAsMention() + ", Please provide the correct args! " + this.getName());
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
