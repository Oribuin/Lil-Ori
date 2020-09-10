package xyz.oribuin.lilori.command.discord.support.general

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

class CmdReactionRole(bot: LilOri) : Command(bot) {
    init {
        name = "Reaction"
        category = Category(Category.Type.SUPPORT)
        aliases = emptyList()
        description = "Reaction Role Embed."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        event.deleteCmd()

        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDC96 Reaction Roles")
                .setColor(Color.decode("#70b8ff"))
                .setFooter("Created by Ori#0004", "https://img.oribuin.xyz/profile.png")
                .setDescription("""• <@&733512602725908573> - :warning:""")

        event.channel.sendMessage(embedBuilder.build()).queue { msg ->
            msg.addReaction("⚠").queue()
        }
    }
}