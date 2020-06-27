package xyz.oribuin.lilori.commands.global.games

import net.dv8tion.jda.api.entities.Message
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.*
import java.util.concurrent.TimeUnit

class CmdCoinflip : Command() {
    init {
        name = "Coinflip"
        aliases = listOf("Flip")
        //this.arguments = "";
        description = "Flip a coin."
    }

    override fun executeCommand(event: CommandEvent?) {
        val lowerInt = 1
        val higherInt = 100
        event!!.channel.sendMessage(":moneybag: Flipping...").queue { message: Message ->
            val randomInt = Random().nextInt(higherInt - lowerInt + 1) + lowerInt
            if (randomInt > 50) message.editMessage(":moneybag: You landed on **Heads**.").queueAfter(2, TimeUnit.SECONDS) else message.editMessage(":moneybag: You landed on **Tails**.").queueAfter(2, TimeUnit.SECONDS)
        }
    }
}