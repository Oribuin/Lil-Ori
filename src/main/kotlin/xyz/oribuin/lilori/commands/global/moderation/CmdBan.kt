package xyz.oribuin.lilori.commands.global.moderation

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.exceptions.HierarchyException
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdBan : Command() {
    init {
        name = "Ban"
        aliases = listOf("bean")
        description = "Ban a member from a server!"
        userPermissions = arrayOf(Permission.BAN_MEMBERS)
        botPermissions = arrayOf(Permission.BAN_MEMBERS)
    }

    override fun executeCommand(event: CommandEvent) {
        // Define all the variables
        event.deleteCmd()

        val args = event.message.contentRaw.split(" ").toTypedArray()
        val msgTime = event.message.timeCreated

        if (args.size < 4) {
            sendInvalidArgs(event)
            return
        }

        if (event.message.mentionedMembers.size == 0) {
            sendInvalidUsersMentioned(event)
            return
        }

        val member = event.message.mentionedMembers[0]

        if (member == null) {
            sendInvalidUsersMentioned(event)
            return
        }

        if (isHigher(event)) {
            sendRankHierarchy(event)
            return
        }

        try {
            val delDays = args[2].toInt()

            if (delDays < 0 || delDays > 100) {
                sendInvalidNumber(event)
                return
            }

            val reason = event.message.contentRaw.substring(args[0].length + args[1].length + args[2].length + 3)

            member.ban(delDays, reason).queue()

            val embedBuilder = EmbedBuilder()
                    .setAuthor("\uD83D\uDC94 Banned User " + member.user.asTag)
                    .setDescription("""Successfully banned ${member.user.asTag} (${member.id}) at ${msgTime.hour}h, ${msgTime.minute}m & ${msgTime.second}s on
                            ${msgTime.dayOfMonth} ${msgTime.month.name.toLowerCase()} for $reason""".trimMargin())
                    .setColor(Settings.EMBED_COLOR)
                    .setFooter("get bonked", "https://img.oribuin.xyz/bot-images/bonk.gif")

            event.channel.sendMessage(event.author.asMention).embed(embedBuilder.build()).queue()

        } catch (ex: NumberFormatException) {
            sendInvalidNumber(event)
        } catch (ex: HierarchyException) {
            sendRankHierarchy(event)
        }
    }

    private fun sendInvalidArgs(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("Invalid Arguments")
                .setDescription("You have provided invalid arguments for the command!")
                .setColor(Settings.EMBED_COLOR)
                .setAuthor("~ban @<User> <Delete-Days> <Reason>")
        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun sendInvalidUsersMentioned(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("\uD83D\uDC94 Invalid User")
                .setDescription("Please include a valid user!")
                .setColor(Settings.EMBED_COLOR)
                .setAuthor("~ban @<User> <Delete-Days> <Reason>")
        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun sendInvalidNumber(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("\uD83D\uDC94 Invalid Number")
                .setDescription("Please make your third argument a number between 0-100!")
                .setColor(Settings.EMBED_COLOR)
                .setAuthor("~ban @<User> <Delete-Days> <Reason>")
        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun sendRankHierarchy(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("\uD83D\uDC94 Can't ban that user")
                .setDescription("Cannot ban this user due to hierarchy!")
                .setColor(Settings.EMBED_COLOR)
                .setAuthor("~ban @<User> <Delete-Days> <Reason>")
        event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
    }

    private fun isHigher(event: CommandEvent): Boolean {
        val mentioned = event.message.mentionedMembers[0]
        return if (mentioned === event.member) true else if (mentioned.isOwner) true else if (mentioned.isFake) true else if (mentioned.roles[0].position >= event.selfMember.roles[0].position) true else if (mentioned.roles[0].position >= event.member!!.roles[0].position) true else mentioned.user.isBot
    }
}