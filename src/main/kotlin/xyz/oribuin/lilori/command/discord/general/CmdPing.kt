package xyz.oribuin.lilori.command.discord.general

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.util.concurrent.TimeUnit

class CmdPing(bot: LilOri) : Command(bot) {
    init {
        name = "Ping"
        category = Category(Category.Type.GENERAL)
        aliases = listOf("latency", "connection")
        description = "Get the latency ping for the bot."
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {

        // Define the bot ping
        val ping = event.jda.gatewayPing
        val embedBuilder = EmbedBuilder().setDescription("Pinging Lil' Ori")

        if (ping < 101) {
            embedBuilder.setColor(Color.green)
        }

        // If Ping is Higher than 100, Do yellow
        if (ping > 100) {
            embedBuilder.setColor(Color.decode("#ffff00"))
        }

        // If Ping is Higher than 200 Do Orange
        if (ping > 199) {
            embedBuilder.setColor(Color.decode("#ffa500"))
        }

        // If Ping is higher than 300, Do Red
        if (ping > 299) {
            embedBuilder.setColor(Color.red)
        }

        // Send the embed to the channel
        event.channel.sendMessage(embedBuilder.build()).queue { message  ->

            // Define a new embed
            val newEmbed = EmbedBuilder()
                    .setColor(message.embeds[0].color)
                    .setDescription("**${ping}ms latency**")

            // Edit the message after three seconds
            message.editMessage(newEmbed.build()).queueAfter(3, TimeUnit.SECONDS)
        }
    }
}