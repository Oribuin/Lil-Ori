package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

public class CmdTest extends Command {

    public CmdTest() {
        this.name = "Test";
        this.description = "A test command.";
        this.ownerOnly = true;

    }

    @Override
    public void executeCommand(CommandEvent event) {


    }
}
