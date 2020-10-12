package xyz.oribuin.lilori.listener.support

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class SupportListeners : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (event.guild.id != "731659405958971413")
            return

        // Add role to member
        event.guild.addRoleToMember(event.member, event.guild.getRolesByName("Member", true)[0]).queue()

        // Define the channel
        val channel = event.guild.getTextChannelById("733059354328170629")

        // Embed builder
        val embedBuilder = EmbedBuilder()
                .setAuthor("A new member has joined!")
                .setColor(Color.decode("#49fff6"))
                .setFooter("Lil' Ori created by Oribuin", "http://img.oribuin.xyz/profile.png")
                .setThumbnail(event.member.user.avatarUrl)
                .addField("Member", "${event.member.asMention} (${event.member.user.asTag})", false)
                .addField("ID", event.member.user.id, false).build()

        channel?.sendMessage(embedBuilder)?.queue()
        println("${event.user.asTag} (${event.user.id}) has joined ${event.guild.name}!")
    }

    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        if (event.guild.id != "731659405958971413")
            return

        // Define the channel
        val channel = event.guild.getTextChannelById("733059354328170629")

        // Embed builder
        val embedBuilder = EmbedBuilder()
                .setAuthor("A new member has left!")
                .setColor(Color.decode("#fe4747"))
                .setFooter("Lil' Ori created by Oribuin", "http://img.oribuin.xyz/profile.png")
                .addField("Member", event.user.asTag, false)
                .addField("ID", event.user.id, false)

        // Send message to channel
        (channel ?: return).sendMessage(embedBuilder.build()).queue()
        println("${event.user.asTag} (${event.user.id}) has left ${event.guild.name}!")


    }

    override fun onGuildBan(event: GuildBanEvent) {
        if (event.guild.id != "731659405958971413")
            return

        // Define the channel
        val channel = event.guild.getTextChannelById("733059354328170629")

        channel?.sendMessage("${event.user.asTag} was banned from the server!")?.queue()
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        // Ticket channel check
        if (event.channel.id == "733092364771655710") {
            if (event.author.isBot)
                return


            if (!event.message.contentRaw.toLowerCase().equals(";ticket", true)) {
                event.message.delete().queue()
            }
        }

        // Anti Songoda | If any songoda lovers are reading this, I don't care for your opinion, It's wrong.
        if (event.message.contentRaw.toLowerCase().contains("songoda") && event.guild.id == "731659405958971413") {
            if (!event.guild.getMember(event.author)?.hasPermission(Permission.ADMINISTRATOR)!!) {
                event.message.delete().queue()
                event.channel.sendMessage("${event.author.asMention}, Please do not mention that company in this discord server.").queue()
            }
        }
    }

    override fun onGuildVoiceJoin(event: GuildVoiceJoinEvent) {
        if (event.guild.id != "731659405958971413")
            return


        if (event.channelLeft != null)
            return

        if (event.member.hasPermission(Permission.ADMINISTRATOR))
            return

        val channel = event.guild.getTextChannelsByName("voice-talk", true)[0]
        channel.createPermissionOverride(event.member).setAllow(Permission.MESSAGE_READ).queue()
    }

    override fun onGuildVoiceLeave(event: GuildVoiceLeaveEvent) {
        if (event.guild.id != "731659405958971413")
            return

        if (event.member.hasPermission(Permission.ADMINISTRATOR))
            return

        val channel = event.guild.getTextChannelsByName("voice-talk", true)[0]
        (channel.getPermissionOverride(event.member) ?: return).delete().queue()
    }

    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        if (event.channel.id == "733084715736891502") {
            when (event.reactionEmote.emoji) {
                "⚠" -> {
                    event.guild.addRoleToMember(event.member, event.guild.getRolesByName("Plugin Updates", true)[0]).queue()
                }
            }
        }
    }

    override fun onGuildMessageReactionRemove(event: GuildMessageReactionRemoveEvent) {
        if (event.channel.id == "733084715736891502") {
            when (event.reactionEmote.emoji) {
                "⚠" -> {
                    event.guild.removeRoleFromMember(event.userId, event.guild.getRolesByName("Plugin Updates", true)[0]).queue()
                }
            }
        }
    }
}