package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.util.Collections;

public class CmdShutdown extends Command {

    public CmdShutdown() {
        this.name = "Shutdown";
        this.description = "Shutdown the bot.";
        this.aliases = Collections.emptyList();
        //this.arguments = "[None]";
        this.ownerOnly = true;
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        event.getChannel().sendMessage("**Shutting down bot**").queue();
        event.getJDA().shutdown();


    }
}
