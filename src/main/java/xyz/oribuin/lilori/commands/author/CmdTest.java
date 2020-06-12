package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.util.Collections;

public class CmdTest extends Command {

    public CmdTest() {
        this.name = "Test";
        this.description = "A test command.";
        this.ownerOnly = true;
        this.aliases = Collections.emptyList();
    }

    @Override
    public void executeCommand(CommandEvent event) {
        event.reply("Test Working");
    }
}
