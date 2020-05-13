package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

public class CmdTest extends Command {

    public CmdTest() {
        this.name = "Test";
        this.description = "A test command.";
        this.category = new Category("Test");
        this.arguments = "[None]";

        this.hidden = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
    }
}
