package xyz.oribuin.lilori.commands.author

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdShutdown : Command() {
    init {
        name = "Shutdown"
        description = "Shutdown the bot."
        aliases = emptyList()
        //this.arguments = "[None]";
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent?) {
        (event ?: return)

        event.channel.sendMessage("**Shutting down bot**").queue()
        event.jda.shutdown()
    }
}