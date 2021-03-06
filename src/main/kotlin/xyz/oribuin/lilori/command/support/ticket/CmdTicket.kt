package xyz.oribuin.lilori.command.support.ticket

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.TicketManager
import java.awt.Color
import java.util.concurrent.TimeUnit

@BotCommand.CommandInfo(
        name = "Ticket",
        description = "Create a support ticket.",
        category = Category.Type.SUPPORT,
        arguments = [],
        aliases = [],
        userPermissions = [Permission.ADMINISTRATOR],
        botPermissions = [],
        guildId = "731659405958971413"
)
class CmdTicket(bot: LilOri) : BotCommand(bot, bot.eventWaiter) {

    override fun executeCommand(event: CommandEvent) {

        val username = event.author.name.replace("/[^a-z0-9]/gi", "")
        val channelName = "${username.toLowerCase()}-ticket"

        if (event.guild.getTextChannelsByName(channelName, true).size > 0) {
            event.timedReply("${event.author.asMention}, You already have a ticket channel.", 30, TimeUnit.SECONDS)
            return
        }

        val embedBuilder = EmbedBuilder()
                .setColor(Color.decode("#687cf9"))
                .setAuthor("\uD83D\uDC99 Welcome to your ticket channel!")
                .setDescription("""Welcome to your Ticket channel, ${event.member.asMention}, Please describe your issue and
                    we will assist you with anything you need.
                    
                    Please put any errors you have inside __https://hasteb.in/__, Please do not type the errors in chat!""".trimMargin())
                .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")

        (event.guild.getCategoryById("733086484470694018") ?: return).createTextChannel(channelName)
                .setTopic("🎫 Please explain your issue our support team will be with you soon!").queue { channel ->
                    event.guild.getRoleById("733059033405063298")?.asMention?.let { channel.sendMessage(it).queue { msg -> msg.delete().queue() } }

                    event.member.let { channel.createPermissionOverride(it).setAllow(Permission.VIEW_CHANNEL).queue() }
                    event.member.asMention.let {
                        channel.sendMessage(it).embed(embedBuilder.build()).queue()
                    }
                }

        event.deleteCmd()
        event.timedReply("${event.author.asMention}, Successfully created your ticket channel!", 30, TimeUnit.SECONDS)
        println("${event.author.asTag} has created the ticket channel, #$channelName")

        // Update User
        bot.getManager(TicketManager::class).updateUser(event.author, bot.getManager(TicketManager::class).getTicketCount(event.author) + 1)
    }
}