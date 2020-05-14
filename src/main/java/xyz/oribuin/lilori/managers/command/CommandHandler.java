package xyz.oribuin.lilori.managers.command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandHandler {
    private LinkedList<Command> commands = new LinkedList<>();

    public void registerCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Command getCommand(String name) {
        return getCommands().stream().filter(command -> command.getName().toLowerCase().equals(name)).findFirst().get();
    }
}
