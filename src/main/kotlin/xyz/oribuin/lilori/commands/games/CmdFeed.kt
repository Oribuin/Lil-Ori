package xyz.oribuin.lilori.commands.games

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdFeed(bot: LilOri) : Command(bot) {
    init {
        name = "Feed"
        category = Category(Category.Type.GAMES)
        aliases = listOf("Snack")
        description = "Feed' Lil Ori Cookies."
        arguments = emptyList()
    }


    override fun executeCommand(event: CommandEvent) {
        // Put command in maintenance mode.
        if (event.author.id != "345406020450779149") {
        event.sendEmbedReply("\uD83D\uDC99 Command in Maintenance!", "Hey! This command is currently being rewritten into a currency system where you'll " +
                "be able to use cookies to buy stuff!")
        return
            }

    }
}