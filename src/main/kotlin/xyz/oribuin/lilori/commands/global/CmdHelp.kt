package xyz.oribuin.lilori.commands.global

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdHelp : Command() {
    init {
        name = "Help"
        aliases = listOf("Support")
        description = "Get the list of commands for the bot."
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        event.reply("Coming soon:tm:")
    }
}