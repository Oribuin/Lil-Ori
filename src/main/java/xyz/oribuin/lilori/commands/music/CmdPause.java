package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

public class CmdPause extends Command {
    public CmdPause() {
        this.name = "Pause";
        this.description = "Pause the current track.";
        this.category = new Command.Category("Music");
        this.arguments = "";
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
    }
}
