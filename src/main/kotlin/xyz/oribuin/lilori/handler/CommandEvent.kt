package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.utils.GuildSettings
import java.awt.Color
import java.util.concurrent.TimeUnit

class CommandEvent(private val bot: LilOri, val event: GuildMessageReceivedEvent) {

    fun reply(message: String) {

        event.channel.sendMessage(message).queue()
    }

    fun reply(embedBuilder: EmbedBuilder) {
        event.channel.sendMessage(embedBuilder.build()).queue()
    }

    fun reply(messageEmbed: MessageEmbed) {
        event.channel.sendMessage(messageEmbed).queue()
    }

    fun timedReply(message: String, time: Int, timeUnit: TimeUnit) {
        event.channel.sendMessage(message).queue { msg: Message -> msg.delete().queueAfter(time.toLong(), timeUnit) }
    }

    fun timedReply(embedBuilder: EmbedBuilder, time: Int, timeUnit: TimeUnit) {
        event.channel.sendMessage(embedBuilder.build()).queue { msg: Message -> msg.delete().queueAfter(time.toLong(), timeUnit) }
    }

    fun timedReply(messageEmbed: MessageEmbed, time: Int, timeUnit: TimeUnit) {
        event.channel.sendMessage(messageEmbed).queue { msg: Message -> msg.delete().queueAfter(time.toLong(), timeUnit) }
    }

    fun deleteCmd() {
        event.message.delete().queue()
    }

    fun deleteCmd(time: Int, timeUnit: TimeUnit) {
        try {
            event.message.delete().queueAfter(time.toLong(), timeUnit)
        } catch (ignored: ErrorResponseException) {
        }
    }

    val prefix: String
        get() = GuildSettings(guild).getPrefix()

    val color: Color
        get() = GuildSettings(guild).getColor()

    val selfMember: Member
        get() = event.guild.selfMember

    fun kickMember(user: User) {
        event.guild.getMember(user)?.kick()?.queue()
    }

    fun kickMember(user: User, reason: String) {
        event.guild.getMember(user)?.kick(reason)?.queue()
    }

    fun banMember(user: User, days: Int) {
        event.guild.getMember(user)?.ban(days)?.queue()
    }

    fun banMember(user: User, days: Int, reason: String) {
        event.guild.getMember(user)?.ban(days, reason)?.queue()
    }

    fun banMember(guild: Guild, user: User, days: Int) {
        guild.getMember(user)?.ban(days)?.queue()
    }

    fun banMember(guild: Guild, user: User, days: Int, reason: String) {
        guild.getMember(user)?.ban(days, reason)?.queue()
    }

    // GuildMessageReceivedEvent shortcuts
    val author: User
        get() = event.author

    val channel: MessageChannel
        get() = event.channel

    val textChannel: TextChannel
        get() = channel as TextChannel

    val guild: Guild
        get() = event.guild

    val jda: JDA
        get() = event.jda

    val member: Member?
        get() = event.member

    val message: Message
        get() = event.message

    val privateChannel: PrivateChannel
        get() = event.author.openPrivateChannel().complete()

    val responseNumber: Long
        get() = event.responseNumber

}