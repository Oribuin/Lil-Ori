package xyz.oribuin.lilori.listeners.support

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SupportListener : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (event.guild.id == "731659405958971413")
            return

        // Add role to member
        event.guild.addRoleToMember(event.member, event.guild.getRolesByName("member", true)[0])

        // Define the channel name
        val channel = event.guild.getTextChannelsByName("join-logs", true)[0]

        // Embed builder
        val embedBuilder = EmbedBuilder()
                .setAuthor("A new member has joined!")
                .setColor(event.member.roles[0].color)
                .setFooter("Lil' Ori created by Oribuin", "https://img.oribuin.xyz/profile.png")
                .setThumbnail(event.member.user.avatarUrl)
                .addField("Member", event.member.asMention, false)

        // Send message to channel
        channel.sendMessage(embedBuilder.build()).queue()
    }

    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        if (event.guild.id == "731659405958971413")
            return

        // Define the channel name
        val channel = event.guild.getTextChannelsByName("join-logs", true)[0]

        // Embed builder
        val embedBuilder = EmbedBuilder()
                .setAuthor("A new member has left!")
                .setColor(event.guild.owner?.color)
                .setFooter("Lil' Ori created by Oribuin", "https://img.oribuin.xyz/profile.png")
                .addField("Member", event.user.asTag, false)

        // Send message to channel
        channel.sendMessage(embedBuilder.build()).queue()
    }
}