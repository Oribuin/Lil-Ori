package xyz.oribuin.lilori.handler.console

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.command.console.CmdActivity
import xyz.oribuin.lilori.command.console.CmdTest
import xyz.oribuin.lilori.manager.Manager

class ConsoleCMDHandler(bot: LilOri) : Manager(bot) {
    val commands = mutableListOf<ConsoleCMD>()

    fun registerCommands() {
        commands.addAll(listOf(CmdTest(bot), CmdActivity(bot)))
    }

    fun getCommand(name: String): ConsoleCMD {
        return commands.stream().filter { command -> command.name.toLowerCase() == name }.findFirst().get()
    }

    override fun enable() {
        // Unused
    }
}