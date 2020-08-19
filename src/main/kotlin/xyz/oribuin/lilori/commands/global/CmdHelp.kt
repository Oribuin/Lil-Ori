package xyz.oribuin.lilori.commands.global

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdHelp(bot: LilOri) : Command(bot) {
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