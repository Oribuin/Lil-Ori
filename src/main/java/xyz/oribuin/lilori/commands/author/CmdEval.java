package xyz.oribuin.lilori.commands.author;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class CmdEval extends Command {
    public CmdEval() {
        this.name = "Eval";
        this.help = "An evaluation.";
        this.category = new Category("Test");
        this.arguments = "[Code]";
        this.hidden = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

    }
}
