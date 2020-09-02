package xyz.oribuin.lilori.commands.console

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.console.ConsoleCMD
import xyz.oribuin.lilori.handler.console.ConsoleEvent

class CmdTest(bot: LilOri) : ConsoleCMD(bot) {

    init {
        name = "test"
        aliases = listOf("test2")
        description = "Description"
    }
    override fun executeCommand(event: ConsoleEvent) {
        event.print("Test Working")
    }
}