package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

public class CmdTest extends Command {

    public CmdTest() {
        this.name = "Test";
        this.description = "A test command.";
        //this.arguments = "[None]";

    }

    @Override
    public void executeCommand(CommandEvent event) {
    }
}
