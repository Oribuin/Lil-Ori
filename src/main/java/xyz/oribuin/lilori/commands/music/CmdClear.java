package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

public class CmdClear extends Command {

    public CmdClear() {
        this.name = "OhFuck";
        this.help = "fuck.";
        this.category = new Command.Category("Music");
        this.arguments = "";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
    }
}
