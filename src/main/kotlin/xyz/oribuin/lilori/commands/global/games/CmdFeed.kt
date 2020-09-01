package xyz.oribuin.lilori.commands.global.games

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

class CmdFeed(bot: LilOri) : Command(bot) {
    init {
        name = "Feed"
        category = Category(Category.Type.GAMES)
        aliases = listOf("Snack")
        description = "Feed' Lil Ori Cookies."
        arguments = emptyList()
    }

    private var cookies = 0

    override fun executeCommand(event: CommandEvent) {

        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size >= 2) {
            event.reply("**Lil' Ori now has $cookies cookies!**")
            return
        }

        cookies++
        val embedBuilder = EmbedBuilder()
                .setAuthor("You gave Lil' Ori A Cookie!")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**He now has $cookies Cookies**")

        event.reply(embedBuilder.build())
    }
}