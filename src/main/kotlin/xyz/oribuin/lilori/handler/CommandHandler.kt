package xyz.oribuin.lilori.handler

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.managers.Manager

class CommandHandler(bot: LilOri): Manager(bot) {
    val commands = mutableListOf<Command>()

    fun registerCommands(vararg cmds: Command) {
        this.commands.addAll(cmds)
    }

    fun getCommandList(): List<Command> {
        val list = mutableListOf<Command>()

        commands.toList().forEach { cmd ->
            list.add(cmd)
        }

        return list
    }

    fun getCommand(name: String): Command {
        return commands.stream().filter { command: Command -> command.name.toLowerCase() == name }.findFirst().get()
    }

    override fun enable() {
        // Unused
    }
}