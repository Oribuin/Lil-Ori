package xyz.oribuin.lilori.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.utils.GuildSettings
import java.awt.Color
import java.util.*
import java.util.function.Consumer

class GeneralEvents(private val bot: LilOri) : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
            for (guild in event.jda.guilds) {
                val guildSettings = GuildSettings(guild)

                bot.guildSettingsManager.updateGuild(guild, guildSettings.getPrefix())
            }

        val activities = arrayOf(
                Activity.watching("https://oribuin.xyz/"),
                Activity.watching("#BlackLivesMatter"),
                Activity.watching("https://jars.oribuin.xyz/"),
                Activity.watching("#JusticeForBreonnaTaylor"),
                Activity.watching("https://oribuin.xyz/support"),
                Activity.watching("#BLM"),
                Activity.watching("https://oribuin.xyz/donate")
        )

        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val randomAnswer = Random().nextInt(activities.size)
                event.jda.presence.setPresence(OnlineStatus.DO_NOT_DISTURB, activities[randomAnswer])
            }
        }

        timer.schedule(timerTask, 0, 20000)
    }

    override fun onDisconnect(event: DisconnectEvent) {
        event.jda.guilds.forEach(Consumer { guild: Guild -> guild.audioManager.closeAudioConnection() })
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        bot.guildSettingsManager.createGuild(event.guild)
        bot.guildSettingsManager.loadGuildSettings(event.guild)
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        bot.guildSettingsManager.removeGuild(event.guild)
        bot.guildSettingsManager.removeGuildSettings(event.guild)
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {

        val embedBuilder = EmbedBuilder()
                .setAuthor("Lil' Ori Bot | Prefix: " + GuildSettings(event.guild).getPrefix(), "https://discordapp.com/oauth2/authorize?client_id=581203970203189269&permissions=121498961&scope=bot")
                .setColor(GuildSettings(event.guild).getColor())
                .setDescription("""» Discord Utility Bot Created by Oribuin « 
                        
                        • Find all my commands using **$${GuildSettings(event.guild).getPrefix()}help** 
                        • Find my source code on https://github.com/Oribuin/Lil-Ori/
                        • Website: https://oribuin.xyz/
                        • Donate: https://oribuin.xyz/donate""".trimIndent())
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")

        if (event.message.contentRaw == "<@!581203970203189269>")
            event.channel.sendMessage(embedBuilder.build()).queue()
    }
}