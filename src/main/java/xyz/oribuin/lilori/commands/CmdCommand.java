package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CmdCommand extends Command {

    public CmdCommand() {
        this.name = "Command";
        this.aliases = new String[]{"latency"};
        this.description = "Get the latency ping for the bot.";
    }

    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Invalid Arguments.").queueAfter(10, TimeUnit.SECONDS);
            return;
        }

        Command command = bot.getCommandHandler().getCommand(args[1]);
        if (command == null) {
            event.reply(event.getAuthor().getAsMention() + ", You have provided an invalid command");
            return;
        }

        List<String> aliases = new ArrayList<>();
        List<String> arguments = new ArrayList<>();

        if (command.getAliases().length == 0)
            aliases.add("None");
        else
            aliases.add(Arrays.toString(command.getArguments()));

        if (command.getArguments().length == 0)
            arguments.add("None");
        else
            arguments.add(Arrays.toString(command.getArguments()));

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Lil' Ori Cmd Info")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**Enabled:** " + command.isEnabled() +
                        "\n**Name:** " + command.getName() + " " + arguments.toString().replace("[", "").replace("]", "") +
                        "\n**Description:** " + command.getDescription() +
                        "\n**Aliases:** " + aliases.toString().replace("[", "").replace("]", "") +
                        "\n**Enabled:** " + command.isEnabled() +
                        "\n**Owner Only:** " + command.isOwnerOnly());

        event.reply(embedBuilder.build());
    }
}
