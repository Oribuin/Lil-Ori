package xyz.oribuin.lilori.commands.support.general

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdAnnounce : Command() {
    init {
        name = "Announce"
        description = "Announce a message into a channel"
        aliases = emptyList()
        arguments = emptyList()
        guildId = "731659405958971413"
    }

    override fun executeCommand(event: CommandEvent) {
        TODO("Not yet implemented, Waiting on EventWaiter fix")
    }
}