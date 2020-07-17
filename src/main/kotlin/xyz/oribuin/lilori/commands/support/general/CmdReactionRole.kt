package xyz.oribuin.lilori.commands.support.general

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

class CmdReactionRole : Command() {
    init {
        name = "Reaction"
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
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("""• <@&733512602725908573> - :warning:""")

        event.channel.sendMessage(embedBuilder.build()).queue {msg: Message ->
            msg.addReaction("⚠").queue()
        }
    }
}