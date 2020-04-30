package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

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
        event.reply("Now Shutting Down.");
        event.getJDA().shutdown();
    }
}
