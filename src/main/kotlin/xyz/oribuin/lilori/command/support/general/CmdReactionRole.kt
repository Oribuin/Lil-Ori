package xyz.oribuin.lilori.command.support.general

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

@BotCommand.CommandInfo(
        name = "Reaction",
        description = "Send reaction roles message",
        category = Category.Type.SUPPORT,
        arguments = [],
        aliases = [],
        userPermissions = [Permission.ADMINISTRATOR],
        botPermissions = [],
        guildId = "731659405958971413",
        ownerOnly = true
)
class CmdReactionRole(bot: LilOri) : BotCommand(bot, bot.eventWaiter) {

    override fun executeCommand(event: CommandEvent) {
        event.deleteCmd()

        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDC96 Reaction Roles")
                .setColor(Color.decode("#70b8ff"))
                .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")
                .setDescription("""• <@&733512602725908573> - :warning:""")

        event.channel.sendMessage(embedBuilder.build()).queue { msg ->
            msg.addReaction("⚠").queue()
        }
    }
}