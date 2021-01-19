package xyz.oribuin.lilori.command.general

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.GuildSettingsManager
import xyz.oribuin.lilori.util.BotUtils
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.lang.NumberFormatException
import javax.imageio.ImageIO

@BotCommand.CommandInfo(
        name = "Color",
        description = "View any hex or rgb code or set bot color.",
        category = Category.Type.GENERAL,
        arguments = ["[set] <#hex/r g b>>"],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)
class CmdColor(bot: LilOri) : BotCommand(bot, bot.eventWaiter) {

    private lateinit var embedColor: Color
    private lateinit var color: Color

    /*
    Normal Color Command:
    ;color[0] <#hex[1] / red[1]> <green[2]> <blue[3]> if (args.size != 2 && args.size != 4) // return invalid args
    Color set Command:
    ;color[0] set[1] <#hex[2] / red[2]> <green[3]> <blue[4]> - if (args[1] == "set && args.size != 3 && args.size != 5) // return invalid args
     */

    override fun executeCommand(event: CommandEvent) {

        // Check if sub command is "set"

        if (event.args[1].equals("set", true)) {

            // Check if the user has permission for the command.
            if (!event.member.hasPermission(Permission.ADMINISTRATOR)) {
                event.sendEmbedReply("❗ Invalid Permission", "You must be an admin for this command!")
                return
            }

            // Check if the user has provided the right arguments.
            if (event.args.size != 3 && event.args.size != 5) {
                event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
                return
            }

            try {
                // Check if the third argument starts with a #, If so, assign color as #Hex
                color = if (event.args[2].startsWith("#")) {
                    Color.decode(event.args[2])
                } else {

                    // If the arguments provided are as rgb, Assign color to the rgb code
                    val red = event.args[2].toInt()
                    val green = event.args[3].toInt()
                    val blue = event.args[4].toInt()
                    Color(red, green, blue)
                }

                // Define the color file & hex code for the color
                val file = File("images", "color.png")
                val hex = BotUtils.formatToHex(color)

                // Create the image
                this.createImage(color)

                // Create the embed
                val embedBuilder = EmbedBuilder()
                        .setTitle("\uD83D\uDD8C Changed Guild Color")
                        .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")
                        .setDescription("""You have changed the guild's embed color, Try it out by tagging ${event.selfMember.asMention} in chat!
                            
                            **»** Color: $hex (${color.red},${color.green},${color.blue})
                        """.trimIndent())
                        .setImage("attachment://color.png")
                        .setColor(color)

                // Send the embed to channel
                event.channel.sendFile(file).embed(embedBuilder.build()).queue()

                // Update the embed in the database
                bot.getManager(GuildSettingsManager::class).updateGuild(event.guild, event.prefix, color)

                // Print the change to console
                println("${event.author.asTag} Updated \"${event.guild.name}\" Color to $hex")

                // If the rgb defined are not numbers, send invalid format message.
            } catch (ex: NumberFormatException) {
                event.sendEmbedReply("❗ Invalid Format", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            }

            return
        }

        // Start the check for normal color command.
        // Check if the user has provided the right arguments.
        if (event.args.size != 2 && event.args.size != 4) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        try {
            // Check if the third argument starts with a #, If so, assign color as #Hex
            embedColor = if (event.args[1].startsWith("#")) {
                Color.decode(event.args[1])
            } else {

                // If the arguments provided are as rgb, Assign color to the rgb code
                val red = event.args[1].toInt()
                val green = event.args[2].toInt()
                val blue = event.args[3].toInt()
                Color(red, green, blue)
            }

            // Define the color file & hex code for the color
            val file = File("images", "color.png")
            val hex = BotUtils.formatToHex(embedColor)

            // Create the image
            this.createImage(embedColor)

            // Create the embed
            val embedBuilder = EmbedBuilder()
                    .setTitle("\uD83D\uDD8C Colour Showcase")
                    .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")
                    .setDescription("""Here is the color you request, ${event.member.asMention}!
                            
                            **»** Color: $hex (${embedColor.red},${embedColor.green},${embedColor.blue})
                        """.trimIndent())
                    .setImage("attachment://color.png")
                    .setColor(embedColor)

            // Send the embed to channel
            event.channel.sendFile(file).embed(embedBuilder.build()).queue()

            // If the rgb defined are not numbers, send invalid format message.
        } catch (ex: NumberFormatException) {
            event.sendEmbedReply("❗ Invalid Format", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
        }

    }

    private fun createImage(imgColor: Color) {
        val bufferedImage = BufferedImage(750, 175, BufferedImage.TYPE_INT_RGB)
        val graphics = bufferedImage.createGraphics()
        graphics.color = Color(imgColor.rgb)
        graphics.fillRect(0, 0, bufferedImage.width, bufferedImage.height)
        graphics.dispose()

        val file = File("images", "color.png")
        ImageIO.write(bufferedImage, "png", file)
    }
}