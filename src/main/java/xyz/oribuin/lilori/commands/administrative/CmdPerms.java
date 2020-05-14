package xyz.oribuin.lilori.commands.administrative;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

public class CmdPerms extends Command {
    public CmdPerms() {
        this.name = "Permissions";
        this.aliases = new String[]{"Perms"};
        this.description = "List of permissions the bot has.";
        //this.arguments = "<Page>";
    }

    @Override
    public void executeCommand(CommandEvent event) {
    }
}
