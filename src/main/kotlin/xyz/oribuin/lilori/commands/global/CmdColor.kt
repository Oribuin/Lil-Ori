package xyz.oribuin.lilori.commands.global

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class CmdColor(bot: LilOri) : Command(bot) {
    init {
        name = "Color"
        description = "See a color in an embed."
        aliases = emptyList()
        arguments = listOf("<#hex>/<r,g,b>")
    }

    private lateinit var embedColor: Color
    private lateinit var color: Color
    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()
        println(args.size)
        if (args[1].toLowerCase() == "set") {


            if (args.size < 3 ) {
                event.reply(event.author.asMention + ", **Usage 1 ${event.prefix}color set 255 0 255**")
                return
            }

            if (args.size != 3 && args.size != 5) {
                event.reply(event.author.asMention + ", **Usage 2 ${event.prefix}color set 255 0 255**")
                return
            }

            try {
                color = if (args[2].startsWith("#")) {
                    Color.decode(args[3])

                } else {
                    val red = args[2].toInt()
                    val green = args[3].toInt()
                    val blue = args[4].toInt()
                    Color(red, green, blue)
                }

                val file = File("images", "color.png")
                val hex: String = String.format("%02x%02x%02x", color.red, color.green, color.blue)
                this.createImage()

                val embedBuilder = EmbedBuilder()
                        .setTitle("Changed Embed Color")
                        .setColor(color)
                        .setImage("attachment://color.png")
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("""Changed the guild embed color!
                            
                            Color: #$hex (${embedColor.red},${embedColor.green},${embedColor.blue})""".trimMargin())

                event.channel.sendFile(file).embed(embedBuilder.build()).queue()
                bot.connector.connect { connection ->
                    val getPrefix = "REPLACE INTO guild_settings (guild_id, color) VALUES (?, ?)"
                    connection.prepareStatement(getPrefix).use { statement ->
                        statement.setLong(1, event.guild.idLong)
                        statement.setString(2, hex)

                        statement.executeUpdate()
                    }
                }

            } catch (ex: NumberFormatException) {
                event.reply(event.author.asMention + ", Correct usage example " + event.prefix + "color 255 0 255")
            }
            return
        }

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

            val hex: String = String.format("%02x%02x%02x", embedColor.red, embedColor.green, embedColor.blue)
            val file = File("images", "color.png")
            this.createImage()

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

    private fun createImage() {
        val bufferedImage = BufferedImage(750, 175, BufferedImage.TYPE_INT_RGB)
        val graphics = bufferedImage.createGraphics()
        graphics.color = Color(embedColor.rgb)
        graphics.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
        graphics.dispose()

        val file = File("images", "color.png")
        ImageIO.write(bufferedImage, "png", file)
    }
}