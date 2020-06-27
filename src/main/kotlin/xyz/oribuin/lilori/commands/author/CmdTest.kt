package xyz.oribuin.lilori.commands.author

import org.apache.commons.lang3.StringUtils
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdTest : Command() {
    init {
        name = "Test"
        aliases = emptyList()
        description = "A test command."
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        val time = event.author.timeCreated

        event.reply("Created account on ${time.dayOfMonth} ${StringUtils.capitalize(time.month.name.toLowerCase())} ${time.year} at ${time.hour}h, ${time.minute}m & ${time.second}s")
    }
}