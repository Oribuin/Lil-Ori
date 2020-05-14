package xyz.oribuin.lilori.commands;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

public class CmdHelp extends Command {

    public CmdHelp() {
        this.name = "Help";
        this.aliases = new String[]{"Support"};
        this.description = "Get the list of commands for the bot.";
    }

    public void executeCommand(CommandEvent event) {
        event.reply("This command is currently disabled.");
    }
}