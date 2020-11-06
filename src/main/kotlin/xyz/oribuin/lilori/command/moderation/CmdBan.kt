package xyz.oribuin.lilori.command.moderation

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.exceptions.HierarchyException
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent

@CommandInfo(
        name = "Ban",
        description = "Ban a member from the server.",
        category = Category.Type.MODERATION,
        arguments = ["@<User>", "<Delete-Days>", "<Reason>"],
        aliases = ["bean"],
        userPermissions = [Permission.BAN_MEMBERS],
        botPermissions = [Permission.BAN_MEMBERS],
        guildId = ""
)
class CmdBan(bot: LilOri) : BotCommand(bot) {

    override fun executeCommand(event: CommandEvent) {
        // Delete the command on send for reasons we're not sure of yet
        event.deleteCmd()

        // Define the message time
        val msgTime = event.message.timeCreated

        // Check if the user sent the right amount of args
        if (event.args.size < 4) {
            sendInvalidArgs(event)
            return
        }

        // Check if the user has mentioned anyone
        if (event.message.mentionedMembers.size == 0) {
            sendInvalidUsersMentioned(event)
            return
        }

        // Get the first member they mentioned
        val member = event.message.mentionedMembers[0]

        // Check if the member is null
        if (member == null) {
            sendInvalidUsersMentioned(event)
            return
        }

        // If the user cannot ban the member.
        if (cantBan(event)) {
            sendRankHierarchy(event)
            return
        }

        try {

            // Define the amount of days of messages will be deleted
            val delDays = event.args[2].toInt()

            // If the delDays is lower than 0 or higher than 100
            if (delDays < 0 || delDays > 100) {
                sendInvalidNumber(event)
                return
            }

            // Define the ban reason
            val reason = event.message.contentRaw.substring(event.args[0].length + event.args[1].length + event.args[2].length + 3)

            // Ban User
            member.ban(delDays, reason).queue()

            // Define the embed
            val embedBuilder = EmbedBuilder()
                    .setAuthor("\uD83D\uDC94 Banned User " + member.user.asTag)
                    .setDescription("""Successfully banned ${member.user.asTag} (${member.id}) at
                         ${msgTime.hour}h, ${msgTime.minute}m & ${msgTime.second}s on ${msgTime.dayOfMonth} ${msgTime.month.name.toLowerCase()} for $reason""".trimMargin())
                    .setColor(event.color)
                    .setFooter("get bonked", "http://img.oribuin.xyz/bot-images/bonk.gif")

            // Send the embed
            event.channel.sendMessage(event.author.asMention).embed(embedBuilder.build()).queue()

            // Catch incorrect number
        } catch (ex: NumberFormatException) {
            sendInvalidNumber(event)
            // Catch if the user cannot be banned
        } catch (ex: HierarchyException) {
            sendRankHierarchy(event)
        }
    }

    private fun sendInvalidArgs(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("Invalid Arguments")
                .setDescription("You have provided invalid arguments for the command!")
                .setColor(event.color)
                .setAuthor("${event.prefix}ban @<User> <Delete-Days> <Reason>")
        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun sendInvalidUsersMentioned(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("\uD83D\uDC94 Invalid User")
                .setDescription("Please include a valid user!")
                .setColor(event.color)
                .setAuthor("${event.prefix}ban @<User> <Delete-Days> <Reason>")
        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun sendInvalidNumber(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("\uD83D\uDC94 Invalid Number")
                .setDescription("Please make your third argument a number between 0-100!")
                .setColor(event.color)
                .setAuthor("${event.prefix}ban @<User> <Delete-Days> <Reason>")
        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun sendRankHierarchy(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("\uD83D\uDC94 Can't ban that user")
                .setDescription("Cannot ban this user due to hierarchy!")
                .setColor(event.color)
                .setAuthor("${event.prefix}ban @<User> <Delete-Days> <Reason>")

        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun cantBan(event: CommandEvent): Boolean {
        val mentioned = event.message.mentionedMembers[0]
        return when {
            mentioned === event.member -> true
            mentioned.isOwner -> true
            else -> mentioned.user.isBot
        }
    }
}