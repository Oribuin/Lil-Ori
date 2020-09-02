package xyz.oribuin.lilori.handler.console

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.commands.console.CmdTest
import xyz.oribuin.lilori.managers.Manager

class ConsoleCMDHandler(bot: LilOri) : Manager(bot) {
    val commands = mutableListOf<ConsoleCMD>()

    fun registerCommands() {
        commands.addAll(listOf(
                CmdTest(bot))
        )
    }

    fun getCommand(name: String): ConsoleCMD {
        return commands.stream().filter { command -> command.name.toLowerCase() == name }.findFirst().get()
    }

    override fun enable() {
        // Unused
    }
}