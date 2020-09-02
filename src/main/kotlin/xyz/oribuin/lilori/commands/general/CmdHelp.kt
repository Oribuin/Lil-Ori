package xyz.oribuin.lilori.commands.general

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Category.Type
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import kotlin.streams.toList

class CmdHelp(bot: LilOri) : Command(bot) {

    init {
        name = "Help"
        category = Category(Type.GENERAL)
        aliases = listOf("Commands")
        description = "Get the list of commands for the bot."
        arguments = listOf("<Category>")
    }

    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 2) {
            this.categoryList(event)
            return
        }

        val category = this.getCategory(args[1].toUpperCase())


        if (category == null) {
            this.categoryList(event)
            return
        }

        if (category == Type.AUTHOR && event.author.id != "345406020450779149") {
            this.categoryList(event)
            return
        }

        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDCD5 Lil' Ori Commands (Category: ${category.categoryName})")
                .setColor(Color.decode("#82ee8b"))
                .setFooter("Created by Ori#0004")

        val stringBuilder = StringBuilder("Here are a list of commands in category, ${category.categoryName}\n \n")
        val commands = mutableListOf<Command>()

        // Add all the commands to the mutableList
        bot.commandHandler.commands.stream().filter { cmd -> cmd.category.type == category }.toList().toMutableList().forEach { cmd ->
            if (!cmd.isOwnerOnly)
                commands.add(cmd)
        }

        for (cmd in commands) {
            if (event.member.permissions.containsAll(cmd.userPermissions.toList()) && event.selfMember.permissions.containsAll(cmd.botPermissions.toList()) && cmd.isEnabled) {
                stringBuilder.append("${event.prefix}**${cmd.name}** ${cmd.arguments.toString().removePrefix("[").removeSuffix("]").replace(", ", " ")} **»** ${cmd.description}\n")
            }
        }

        embedBuilder.setDescription(stringBuilder.toString())
        event.reply(embedBuilder.build())

    }

    private fun categoryList(event: CommandEvent) {
        val stringBuilder = StringBuilder("Here are the available categories.\n \n")

        for (category in Type.values()) {
            if (category == Type.AUTHOR && event.author.id == "345406020450779149")
                continue

            if (category.permission == null || event.member.hasPermission(category.permission))
                stringBuilder.append("**»** ${category.categoryName}\n")
        }

        event.sendEmbedReply("\uD83D\uDCD5 Category List", stringBuilder.toString())
    }

    private fun getCategory(input: String): Type? {
        for (type in Type.values()) {
            if (type.name == input)
                return type
        }

        return null
    }
}
