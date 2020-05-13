package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

public class CmdGetTrack extends Command {
    public CmdGetTrack() {
        this.name = "Track";
        this.description = "Get the info about the current track.";
        this.category = new Command.Category("Music");
        this.arguments = "";
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
    }
}
