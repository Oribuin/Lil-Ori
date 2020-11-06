package xyz.oribuin.lilori.command.general

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category.Type
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.handler.CommandHandler
import xyz.oribuin.lilori.util.BotUtils
import java.awt.Color
import kotlin.streams.toList

@CommandInfo(
        name = "Help",
        description = "View all bot commands.",
        category = Type.GAMES,
        arguments = ["<Category>"],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)
class CmdHelp(bot: LilOri) : BotCommand(bot) {
    override fun executeCommand(event: CommandEvent) {

        // Check if the user has included the right amount of arguments
        if (event.args.size < 2) {
            this.categoryList(event)
            return
        }

        // Get a Category as nullable from second argument.
        val category = this.getCategory(event.args[1].toUpperCase())

        // Check if the category exists
        if (category == null) {
            this.categoryList(event)
            return
        }

        // If the category is an bot author category and the user is not an category, Send category list
        if (category == Type.AUTHOR && event.author.id != "345406020450779149") {
            this.categoryList(event)
            return
        }

        // Define the command embed
        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDCD5 Lil' Ori Commands (Category: ${category.categoryName})")
                .setColor(Color.decode("#82ee8b"))
                .setFooter("Created by Ori#0004")

        // Create a string builder
        val stringBuilder = StringBuilder("Here are a list of commands in category, ${category.categoryName}\n \n")
        val commands = mutableListOf<BotCommand>()

        // Add all the commands to the mutableList
        bot.getManager(CommandHandler::class).commands.stream().filter { cmd -> getAnnotation(cmd.javaClass).category == category }.toList().toMutableList().forEach { cmd ->
            if (!cmd.getAnnotation(cmd.javaClass).ownerOnly)
                commands.add(cmd)
        }

        // For every command in the list of commands, Append the formatted command text into the string builder
        for (cmd in commands) {
            // Check if the user has permission for the command and the command is enabled
            if (event.member.permissions.containsAll(getAnnotation(cmd.javaClass).userPermissions.toList()) && event.selfMember.permissions.containsAll(getAnnotation(cmd.javaClass).botPermissions.toList())) {
                stringBuilder.append("${event.prefix}**${getAnnotation(cmd.javaClass).name}** ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())} **»** ${getAnnotation(cmd.javaClass).description}\n")
            }
        }

        // Set the description as the stringbuilder
        embedBuilder.setDescription(stringBuilder.toString())

        // Reply with the embed.
        event.reply(embedBuilder.build())
    }

    private fun categoryList(event: CommandEvent) {
        // Define a string builder
        val stringBuilder = StringBuilder("Here are the available categories.\n \n")

        // For loop each Category Type
        for (category in Type.values()) {
            // If the category is an bot author category and the user is not an category, skip the category
            if (category == Type.AUTHOR && event.author.id != "345406020450779149")
                continue

            if (category == Type.ECONOMY || category == Type.SUPPORT && event.guild.id != "731659405958971413")
                continue


            // If the category permission is null or if the category permission is null and user has permission for it
            // Add category to the string builder
            if (category.permission == null || event.member.hasPermission(category.permission))
                stringBuilder.append("**»** ${category.categoryName}\n")
        }

        // Send the embed list
        event.sendEmbedReply("\uD83D\uDCD5 Category List", stringBuilder.toString())
    }

    // Get the CategoryType as null
    private fun getCategory(input: String): Type? {
        for (type in Type.values()) {
            if (type.name == input)
                return type
        }

        return null
    }
}
