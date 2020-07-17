package xyz.oribuin.lilori.commands.support.general

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.time.OffsetDateTime

// ;announce #channel true <message>
class CmdAnnounce : Command() {
    init {
        name = "Announce"
        description = "Announce a message into a channel"
        aliases = emptyList()
        arguments = emptyList()
        guildId = "731659405958971413"
        userPermissions = arrayOf(Permission.ADMINISTRATOR)
    }

    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 2) {
            invalidArguments(event)
            return
        }

        val channel = event.guild.getTextChannelById("733084628696563772")

        if (!listOf("true", "false").contains(args[1].toLowerCase())) {
            invalidBoolean(event)
            return
        }

        val announcement = event.message.contentRaw.substring(args[0].length + args[1].length + 2)

        val embedBuilder = EmbedBuilder()
                .setColor(Color.BLUE)
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setAuthor("Announcement By ${event.author.name}", null, "https://img.oribuin.xyz/bot-images/announcement.jpg")
                .setDescription(announcement)
                .setTimestamp(OffsetDateTime.now())


        if (args[1].toBoolean()) {
            (channel?: return).sendMessage(event.guild.publicRole.asMention).embed(embedBuilder.build()).queue()
            event.reply("${event.author.asMention} Successfully sent an announcement ${channel.asMention}")
            return
        }

        channel?.sendMessage(embedBuilder.build())?.queue()
        event.reply("${event.author.asMention} Successfully sent an announcement ${channel?.asMention}")

    }

    private fun invalidArguments(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("Invalid Arguments")
                .setDescription("You have provided invalid arguments for the command!")
                .setColor(Settings.EMBED_COLOR)
                .setAuthor("${event.prefix}announce #<channel> <should-mention(boolean)> <message>")

        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun invalidBoolean(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("Invalid Boolean")
                .setDescription("You have provided an invalid boolean!")
                .setColor(Settings.EMBED_COLOR)
                .setAuthor("${event.prefix}announce #<channel> <should-mention(boolean)> <message>")

        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }
}