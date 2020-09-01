package xyz.oribuin.lilori.commands.global

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.util.concurrent.TimeUnit

class CmdPing(bot: LilOri) : Command(bot) {
    private var emoji: String? = null

    init {
        name = "Ping"
        category = Category(Category.Type.GENERAL)
        aliases = listOf("latency", "connection")
        description = "Get the latency ping for the bot."
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        val ping: Long? = event.jda.gatewayPing
        val embedBuilder = EmbedBuilder()

        if (ping == null)
            return

        if (ping < 101) {
            emoji = "<a:PanGLoveG:702322097195712523>"
            embedBuilder.setColor(Color.green)
        }

        // If Ping is Higher than 100, Do yellow
        if (ping > 100) {
            emoji = "<a:PanGLoveY:702322097292181534>"
            embedBuilder.setColor(Color.decode("#ffff00"))
        }

        // If Ping is Higher than 200 Do Orange
        if (ping > 199) {
            emoji = "<a:PanGLoveO:702322097002643577>"
            embedBuilder.setColor(Color.decode("#ffa500"))
        }

        // If Ping is higher than 300, Do Red
        if (ping > 299) {
            emoji = "<a:PanGLove:702322097979916298>"
            embedBuilder.setColor(Color.red)
        }

        embedBuilder.setDescription("Pinging Lil' Ori")

        event.channel.sendMessage(embedBuilder.build()).queue { message: Message ->
            val newEmbed = EmbedBuilder()
                    .setColor(message.embeds[0].color)
                    .setDescription("**${ping}ms latency** $emoji")
            message.editMessage(newEmbed.build()).queueAfter(2, TimeUnit.SECONDS)
        }
    }
}