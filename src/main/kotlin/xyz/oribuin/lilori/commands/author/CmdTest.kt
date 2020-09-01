package xyz.oribuin.lilori.commands.author

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdTest(bot: LilOri) : Command(bot) {
    init {
        name = "Test"
        category = Category(Category.Type.AUTHOR)
        aliases = emptyList()
        description = "A test command."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        event.deleteCmd()

        val embed = EmbedBuilder()
                .setAuthor("Test")
                .setColor(event.color).build()

        event.reply(embed)
        println("Ticket count for ${event.author.asTag}: ${bot.ticketManager.getTicketCount(event.author)}")
    }
}
