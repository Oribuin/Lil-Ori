package xyz.oribuin.lilori.command.discord.economy

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.EconomyManager

class CmdBalance (bot: LilOri) : Command(bot) {
    init {
        name = "Balance"
        category = Category(Category.Type.ECONOMY)
        aliases = emptyList()
        description = "Get your economy."
        arguments = emptyList()
        guildId = "731659405958971413"
    }

    override fun executeCommand(event: CommandEvent) {

        val embedBuilder = EmbedBuilder()
                .setAuthor("You currently have ${bot.getManager(EconomyManager::class).getBalance(event.author)} coins!")
                .setDescription("Currently these coins can't be obtained or used for like, anything but they should end up having a usage.")
                .setColor(event.color)
                .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")

        event.reply(embedBuilder.build())
    }
}
