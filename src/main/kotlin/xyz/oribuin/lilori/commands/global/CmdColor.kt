package xyz.oribuin.lilori.commands.global

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class CmdColor : Command() {
    init {
        name = "Color"
        description = "See a color in an embed."
        aliases = emptyList()
        arguments = listOf("<#hex>/<r,g,b>")
    }

    private lateinit var embedColor: Color
    override fun executeCommand(event: CommandEvent) {
        val args  = event.message.contentRaw.split(" ").toTypedArray()
        if (args.size < 2) {
            event.reply(event.author.asMention + ", Please include the correct arguments. " + event.prefix + "color <#HEX-CODE/Red,Green,Blue>")
            return
        }

        if (args.size > 2 && args.size != 4) {
            event.reply(event.author.asMention + ", Correct usage example: " + event.prefix + "color 255 0 255")
            return
        }

        try {
            embedColor = if (args[1].startsWith("#")) {
                Color.decode(args[1])

            } else {
                val red = args[1].toInt()
                val green = args[2].toInt()
                val blue = args[3].toInt()
                Color(red, green, blue)
            }


            val bufferedImage = BufferedImage(750, 175, BufferedImage.TYPE_INT_RGB)
            val graphics = bufferedImage.createGraphics()
            graphics.color = Color(embedColor.rgb)
            graphics.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
            graphics.dispose()

            val hex: String = String.format("%02x%02x%02x", embedColor.red, embedColor.green, embedColor.blue)

            val file = File("images", "color.png")
            ImageIO.write(bufferedImage, "png", file)

            val embedBuilder = EmbedBuilder()
                    .setTitle("Lil' Ori Colors")
                    .setColor(embedColor)
                    .setImage("attachment://color.png")
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("""Use this command to show your favourite hex codes!
                            
                            Color: #$hex (${embedColor.red},${embedColor.green},${embedColor.blue})""".trimMargin())

            event.channel.sendFile(file).embed(embedBuilder.build()).queue()

        } catch (ex: NumberFormatException) {
            event.reply(event.author.asMention + ", Correct usage example: " + event.prefix + "color 255 0 255")
        }
    }

}