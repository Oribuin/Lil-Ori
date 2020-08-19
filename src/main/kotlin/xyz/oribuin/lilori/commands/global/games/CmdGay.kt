package xyz.oribuin.lilori.commands.global.games

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.util.*
import java.util.concurrent.TimeUnit

class CmdGay(bot: LilOri) : Command(bot) {
    init {
        name = "Gay"
        description = "How gay are you?"
        aliases = emptyList()
        botPermissions = arrayOf(Permission.MESSAGE_MANAGE, Permission.MESSAGE_ADD_REACTION)
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {


        val upperBound = 100
        val lowerBound = 1
        val randomBound = Random().nextInt(upperBound - lowerBound - 1) + lowerBound

        event.channel.sendMessage("Calculating your gayness :rainbow_flag:").queue { message: Message ->
            val embedBuilder = EmbedBuilder()
                    .setAuthor("Gay Calculator")
                    .setColor(Color.decode("#33539e"))
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("**You are $randomBound% Gay** :rainbow_flag:")

            event.channel.sendMessage(embedBuilder.build()).queueAfter(2, TimeUnit.SECONDS)
            message.delete().queueAfter(2, TimeUnit.SECONDS)
        }
    }
}