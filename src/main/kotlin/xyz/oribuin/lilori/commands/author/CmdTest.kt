package xyz.oribuin.lilori.commands.author

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdTest : Command() {
    init {
        name = "Test"
        aliases = emptyList()
        description = "A test command."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        event.deleteCmd()
        println("Ticket count for ${event.author.asTag}: ${bot.ticketManager.getTicketCount(event.author)}")
    }
}
