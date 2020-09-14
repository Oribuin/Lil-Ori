package xyz.oribuin.lilori.command.discord.author

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

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
        event.guild.members.forEach { x -> print(x.user.asTag) }
    }
}
