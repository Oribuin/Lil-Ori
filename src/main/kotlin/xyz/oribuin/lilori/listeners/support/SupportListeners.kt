package xyz.oribuin.lilori.listeners.support

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SupportListeners : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (event.guild.id != "731659405958971413")
            return

        // Add role to member
        event.guild.addRoleToMember(event.member, event.guild.getRolesByName("Member", true)[0]).queue()

        // Define the channel name
        val channel = event.guild.getTextChannelById("733059354328170629")

        // Embed builder
        val embedBuilder = EmbedBuilder()
                .setAuthor("A new member has joined!")
                .setColor(event.member.roles[0].color)
                .setFooter("Lil' Ori created by Oribuin", "https://img.oribuin.xyz/profile.png")
                .setThumbnail(event.member.user.avatarUrl)
                .addField("Member", "${event.member.asMention} (${event.member.user.asTag})", false)
                .addField("ID", event.member.user.id, false)

        // Send message to channel
        (channel ?: return).sendMessage(embedBuilder.build()).queue()
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
                .setColor(event.guild.owner?.color)
                .setFooter("Lil' Ori created by Oribuin", "https://img.oribuin.xyz/profile.png")
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
            if (!event.message.contentRaw.toLowerCase().equals(";ticket")) {
                event.message.delete().queue()
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
}