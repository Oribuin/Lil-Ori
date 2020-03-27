package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

public class CmdShutdown extends Command {

    public CmdShutdown() {
        this.name = "Shutdown";
        this.category = new Category("Author");
        this.help = "Shutdown the bot.";
        this.arguments = "[None]";
        this.guildOnly = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {

        event.reactSuccess();
        System.out.print("The bot has now been shut offline.");
        event.getJDA().setAutoReconnect(true);
        event.getJDA().shutdown();
    }
}
