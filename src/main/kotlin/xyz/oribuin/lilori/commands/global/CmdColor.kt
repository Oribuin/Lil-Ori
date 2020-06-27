package xyz.oribuin.lilori.commands.global

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

class CmdColor : Command() {
    init {
        name = "Color"
        description = "See a color in an embed."
        aliases = emptyList()
    }

    private var embedColor: Color? = null
    override fun executeCommand(event: CommandEvent?) {
        val args = event!!.message.contentRaw.split(" ").toTypedArray()
        if (args.size < 2) {
            event.reply(event.author.asMention + ", Please include the correct arguments. " + event.prefix + "color <#HEX-CODE/Red,Green,Blue>")
            return
        }

        if (args.size > 2 && args.size != 4) {
            event.reply(event.author.asMention + ", Correct usage example: " + event.prefix + "color 255 0 255")
            return
        }

        try {
            if (args[1].startsWith("#")) {
                embedColor = Color.decode(args[1])

            } else {
                val red = args[1].toInt()
                val green = args[2].toInt()
                val blue = args[3].toInt()
                embedColor = Color(red, green, blue)
            }

            val embedBuilder = EmbedBuilder()
                    .setTitle("Lil' Ori Colors")
                    .setColor(embedColor)
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("Use this command to show your favourite hex codes! " +
                            " " +
                    "Color: ${embedColor?.red},${embedColor?.green},${embedColor?.blue}".trimMargin())
            
            event.channel.sendMessage(embedBuilder.build()).queue()

        } catch (ex: NumberFormatException) {
            event.reply(event.author.asMention + ", Correct usage example: " + event.prefix + "color 255 0 255")
        }
    }
}