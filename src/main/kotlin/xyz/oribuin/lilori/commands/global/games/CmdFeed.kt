package xyz.oribuin.lilori.commands.global.games

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

class CmdFeed : Command() {
    init {
        name = "Feed"
        aliases = listOf("Snack")
        description = "Feed' Lil Ori Cookies."
        //this.arguments = "";
    }

    private var cookies = 0

    override fun executeCommand(event: CommandEvent?) {
        (event?: return)

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