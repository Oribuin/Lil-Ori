package xyz.oribuin.lilori.listener

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.data.GuildSettings
import xyz.oribuin.lilori.manager.GuildSettingsManager
import java.util.function.Consumer

class GeneralEvents(private val bot: LilOri) : ListenerAdapter() {
    override fun onDisconnect(event: DisconnectEvent) {
        event.jda.guilds.forEach(Consumer { guild: Guild -> guild.audioManager.closeAudioConnection() })
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        bot.getManager(GuildSettingsManager::class).createGuild(event.guild)
        bot.getManager(GuildSettingsManager::class).loadGuildSettings(event.guild)
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        bot.getManager(GuildSettingsManager::class).removeGuild(event.guild)
        bot.getManager(GuildSettingsManager::class).removeGuildSettings(event.guild)
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {

        val embedBuilder = EmbedBuilder()
                .setAuthor("Lil' Ori Bot | Prefix: " + GuildSettings(event.guild).getPrefix(), "https://discordapp.com/oauth2/authorize?client_id=581203970203189269&permissions=121498961&scope=bot")
                .setColor(GuildSettings(event.guild).getColor())
                .setDescription("""» Discord Utility Bot Created by Oribuin « 
                        
                        • Find all my commands using **${GuildSettings(event.guild).getPrefix()}help** 
                        • Find my source code on https://github.com/Oribuin/Lil-Ori/
                        • Website: https://oribuin.xyz/
                        • Donate: https://oribuin.xyz/donate""".trimIndent())
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")

        if (event.message.contentRaw == "<@!581203970203189269>")
            event.channel.sendMessage(embedBuilder.build()).queue()
    }
}