package xyz.oribuin.lilori.command.support.ticket

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.concurrent.TimeUnit

@BotCommand.CommandInfo(
        name = "Close",
        description = "Close your ticket.",
        category = Category.Type.SUPPORT,
        arguments = [],
        aliases = [],
        userPermissions = [Permission.ADMINISTRATOR],
        botPermissions = [],
        guildId = "731659405958971413"
)
class CmdClose(bot: LilOri) : BotCommand(bot, bot.eventWaiter) {
    var useWaiter = true

    override fun executeCommand(event: CommandEvent) {
        if (!event.channel.name.toLowerCase().endsWith("-ticket")) {
            event.timedReply("${event.author.asMention}, You cannot close this ticket channel", 30, TimeUnit.SECONDS)
            return
        }

        if (useWaiter) {
            event.channel.sendMessage(event.author.asMention + ", Are you sure you want to close the ticket channel?").queue { msg ->
                msg.addReaction("✅").queue()
                msg.addReaction("❌").queue()

                waiter.waitForEvent(GuildMessageReactionAddEvent::class.java, { check -> check.user == event.author && check.messageId == msg.id }, { action ->
                    when (action.reactionEmote.emoji) {
                        "✅" -> {
                            println("${event.author.asTag} has closed the ticket channel, #${event.textChannel.name}!")
                            event.textChannel.delete().queue()
                        }

                        "❌" -> {
                            event.reply(event.author.asMention + ", You have cancelled ticket channel.")
                            waiter.shutdown()
                        }
                    }

                })

            }
        } else {
            println("${event.author.asTag} has closed the ticket channel, #${event.textChannel.name}!")
            event.textChannel.delete().queue()
        }

    }
}