package xyz.oribuin.lilori.handler

class CommandHandler {
    val commands = mutableListOf<Command>()

    fun registerCommands(vararg cmds: Command) {
        this.commands.addAll(cmds)
    }

    fun registerCommand(cmd: Command) {
        if (!commands.contains(cmd)) {
            commands.add(cmd)
        }
    }

    fun commandList(): List<Command> {
        return this.commands.toList()
    }

    fun getCommand(name: String): Command {
        return commandList().stream().filter { command: Command -> command.name?.toLowerCase() == name }.findFirst().get()
    }
}