package xyz.oribuin.lilori.commands.support.ticket

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.concurrent.TimeUnit

class CmdClose() : Command() {
    init {
        name = "Close"
        description = "Close a ticket!"
        aliases = emptyList()
        arguments = emptyList()
        guildId = "731659405958971413"

    }

    override fun executeCommand(event: CommandEvent) {
        if (!event.channel.name.toLowerCase().endsWith("-ticket")) {
            event.timedReply("${event.author.asMention}, You cannot close this ticket channel", 30, TimeUnit.SECONDS)
            return
        }

        /*
        event.channel.sendMessage(event.author.asMention + ", Are you sure you want to close the ticket channel?").queue { msg ->
            msg.addReaction("✅").queue()
            msg.addReaction("❌").queue()

            waiter.waitForEvent(GuildMessageReactionAddEvent::class.java, { check: GuildMessageReactionAddEvent -> check.user.equals(event.author) && check.messageId.equals(msg.id) }) { action ->
                when (action.reactionEmote.emoji) {
                    "✅" -> {
                        println("${event.author.asTag} has closed the ticket channel, ${event.textChannel.name}")
                        event.textChannel.delete().queue()
                    }

                    "❌" -> {
                        event.reply(event.author.asMention + ", You have cancelled ticket channel.")
                        waiter.shutdown()
                    }
                }
            }

             */
        //}

        println("${event.author.asTag} has closed the ticket channel, ${event.textChannel.name}")
        event.textChannel.delete().queue()
    }
}