package xyz.oribuin.lilori.command.discord.support.general

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.time.OffsetDateTime

// ;announce #channel true <message>
class CmdAnnounce(bot: LilOri) : Command(bot) {
    init {
        name = "Announce"
        category = Category(Category.Type.SUPPORT)
        description = "Announce a message into a channel"
        aliases = emptyList()
        arguments = emptyList()
        guildId = "731659405958971413"
        userPermissions = arrayOf(Permission.ADMINISTRATOR)
    }

    override fun executeCommand(event: CommandEvent) {


        if (event.args.size < 2) {
            invalidArguments(event)
            return
        }

        when (event.args[1].toLowerCase()) {
            "announcement" -> {
                // ;announce announcement <boolean> <message>
                if (event.args.size < 3) {
                    invalidArguments(event)
                    return
                }

                val channel = event.guild.getTextChannelById("733084628696563772")

                if (!listOf("true", "false").contains(event.args[2].toLowerCase())) {
                    invalidBoolean(event)
                    return
                }

                val announcement = event.message.contentRaw.substring(event.args[0].length + event.args[1].length + event.args[2].length + 3)

                val embedBuilder = EmbedBuilder()
                        .setColor(Color.BLUE)
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setAuthor("Announcement By ${event.author.name}", null, "https://img.oribuin.xyz/bot-images/announcement.jpg")
                        .setDescription(announcement)
                        .setTimestamp(OffsetDateTime.now())


                if (event.args[2].toBoolean()) {
                    (channel
                            ?: return).sendMessage(event.guild.publicRole.asMention).embed(embedBuilder.build()).queue()
                    event.reply("${event.author.asMention} Successfully sent an announcement ${channel.asMention}")
                    return
                }

                channel?.sendMessage(embedBuilder.build())?.queue()
                event.reply("${event.author.asMention} Successfully sent an announcement to ${channel?.asMention}")

            }
            "plugin" -> {
                // ;announce plugin <plugin> <version> <changelog>
                if (event.args.size < 4) {
                    invalidArguments(event)
                    return
                }

                val channel = event.guild.getTextChannelById("733086809096978492")

                val changelog = event.message.contentRaw.substring(event.args[0].length + event.args[1].length + event.args[2].length + event.args[3].length + 4)

                val embedBuilder = EmbedBuilder()
                        .setColor(Color.BLUE)
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setAuthor("Plugin Update: ${event.args[2]}", null, "https://img.oribuin.xyz/bot-images/announcement.jpg")
                        .setDescription("""$changelog
                            
                        Find the latest jar file on https://jars.oribuin.xyz/""".trimMargin())
                        .setTimestamp(OffsetDateTime.now())

                (channel
                        ?: return).sendMessage(event.guild.getRolesByName("Plugin Updates", true)[0].asMention).embed(embedBuilder.build()).queue()
                event.reply("${event.author.asMention} Successfully sent plugin update to ${channel.asMention}")
            }
        }

    }

    private fun invalidArguments(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("Invalid Arguments")
                .setDescription("You have provided invalid arguments for the command!")
                .setColor(event.color)
                .setAuthor("${event.prefix}announce")

        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun invalidBoolean(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("Invalid Boolean")
                .setDescription("You have provided an invalid boolean!")
                .setColor(event.color)
                .setAuthor("${event.prefix}announce")

        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }
}