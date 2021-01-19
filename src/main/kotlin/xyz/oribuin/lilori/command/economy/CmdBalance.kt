package xyz.oribuin.lilori.command.economy

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.EconomyManager

@BotCommand.CommandInfo(
        name = "Balance",
        description = "Get your current balance for the bot.",
        category = Category.Type.ECONOMY,
        arguments = [],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = "731659405958971413"
)
class CmdBalance (bot: LilOri) : BotCommand(bot, bot.eventWaiter) {
    override fun executeCommand(event: CommandEvent) {

        val embedBuilder = EmbedBuilder()
                .setAuthor("You currently have ${bot.getManager(EconomyManager::class).getBalance(event.author)} coins!")
                .setDescription("Currently these coins can't be obtained or used for like, anything but they should end up having a usage.")
                .setColor(event.color)
                .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")

        event.reply(embedBuilder.build())
    }
}
