package xyz.oribuin.lilori.commands.discord.author

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.commands.console.CmdActivity
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.text.SimpleDateFormat
import java.util.*

class CmdTest(bot: LilOri) : Command(bot) {
    init {
        name = "Test"
        category = Category(Category.Type.GENERAL)
        aliases = emptyList()
        description = "A test command."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        event.deleteCmd()
    }
}
