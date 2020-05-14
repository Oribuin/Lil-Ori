package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

public class CmdShutdown extends Command {

    public CmdShutdown() {
        this.name = "Shutdown";
        this.description = "Shutdown the bot.";
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
