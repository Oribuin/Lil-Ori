package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdCommand extends Command {

    public CmdCommand() {
        this.name = "Command";
        this.description = "Get info about a command.";
        this.category = new Command.Category("Info");
        this.arguments = "[Command]";
        this.aliases = new String[]{"info"};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) {
            event.reply(event.getAuthor().getAsMention() + ", Invalid Arguments.");
            return;
        }

        Command command = event.getClient().getCommandByName(args[1]);
        if (command == null) {
            event.reply(event.getAuthor().getAsMention() + ", You have provided an invalid command");
            return;
        }

        List<String> aliases = new ArrayList<>();

        if (command.getAliases().length == 0)
            aliases.add("None");
        else
            aliases.add(Arrays.toString(command.getAliases()));

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Lil' Ori Cmd Info")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**Enabled:** " + command.isEnabled() +
                        "\n**Name:** " + command.getName() + " " + command.getArguments() +
                        "\n**Description:** " + command.getDescription() +
                        "\n**Category:** " + command.getCategory().getName() +
                        "\n**Aliases:** " + aliases.toString().replace("[", "").replace("]", "") +
                        "\n**Hidden:** " + command.isHidden());

        event.reply(embedBuilder.build());
    }
}
