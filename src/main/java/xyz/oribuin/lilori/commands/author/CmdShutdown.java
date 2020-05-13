package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

public class CmdShutdown extends Command {


    public CmdShutdown() {
        this.name = "database";
        this.category = new Category("Author");
        this.description = "Shutdown the bot.";
        this.arguments = "[None]";
        this.guildOnly = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");


    }
}
