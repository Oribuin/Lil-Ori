package xyz.oribuin.lilori.commands.administrative;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.util.Collections;

public class CmdPerms extends Command {
    public CmdPerms() {
        this.name = "Permissions";
        this.aliases = Collections.singletonList("Perms");
        this.description = "List of permissions the bot has.";
        this.aliases = Collections.emptyList();
    }

    @Override
    public void executeCommand(CommandEvent event) {
    }
}
