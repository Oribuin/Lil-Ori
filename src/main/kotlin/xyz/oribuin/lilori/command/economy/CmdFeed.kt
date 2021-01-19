package xyz.oribuin.lilori.command.economy

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.EconomyManager
import java.util.*

@BotCommand.CommandInfo(
        name = "Feed",
        description = "Feed Lil' Ori cookies for coins.",
        category = Category.Type.ECONOMY,
        arguments = [],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = "731659405958971413"
)
class CmdFeed(bot: LilOri) : BotCommand(bot, bot.eventWaiter) {
    var feedCooldownMap = mutableMapOf<String, Long>()


    override fun executeCommand(event: CommandEvent) {

        if (feedCooldownMap.contains(event.author.id)) {
            val secondsLeft = (feedCooldownMap[event.author.id] ?: return).div(1000).plus(86400L).minus(System.currentTimeMillis().div(1000))
            if (secondsLeft > 0) {
                event.sendEmbedReply("\uD83D\uDC99 On Cooldown!", "You cannot feed Lil' Ori yet, Please wait $secondsLeft seconds.")
                return
            }
        }

        feedCooldownMap[event.author.id] = System.currentTimeMillis()

        val random = Random().nextInt(20 - 5 + 1) + 5
        val balance = bot.getManager(EconomyManager::class).getBalance(event.author)
        bot.getManager(EconomyManager::class).updateBalance(event.author, balance + random)

        event.sendEmbedReply("\uD83D\uDC99 Gave Lil' Ori cookies!", "You have gained +**$random** coins! Use these coins in Lil' Ori's Store.\n" +
                " \n" +
                "You now have a total of **${balance + random} coins**!")

    }
}