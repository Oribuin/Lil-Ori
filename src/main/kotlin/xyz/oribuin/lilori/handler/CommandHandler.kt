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

    fun getCommandList(): List<Command> {
        val list = mutableListOf<Command>()

        commands.toList().forEach { cmd ->
            list.add(cmd)
        }

        return list
    }

    fun getCommand(name: String): Command {
        return commands.stream().filter { command: Command -> command.name?.toLowerCase() == name }.findFirst().get()
    }
}