package xyz.oribuin.lilori.command.general

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Cmd
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.util.concurrent.TimeUnit

@Cmd(
        name = "Ping",
        description = "Get all the bot's latency.",
        category = Category.Type.GENERAL,
        arguments = [],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)
class CmdPing(bot: LilOri) : Command(bot) {

    override fun executeCommand(event: CommandEvent) {
        val embedBuilder = EmbedBuilder()

        embedBuilder.setDescription("Pinging Lil' Ori")

        val ping: Long = event.jda.gatewayPing
        // Define the bot ping
        //val ping = event.jda.gatewayPing
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
        event.channel.sendMessage(embedBuilder.build()).queue { message ->

            // Define a new embed
            val newEmbed = EmbedBuilder()
                    .setColor(message.embeds[0].color)
                    .setDescription("**${ping}ms latency**")

            // Edit the message after three seconds
            message.editMessage(newEmbed.build()).queueAfter(3, TimeUnit.SECONDS)
        }
    }
}