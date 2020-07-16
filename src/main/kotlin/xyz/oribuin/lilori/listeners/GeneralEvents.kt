package xyz.oribuin.lilori.listeners

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import java.awt.Color
import java.util.*
import java.util.function.Consumer

class GeneralEvents : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        for (guild in event.jda.guilds) {
            LilOri.instance.guildSettingsManager.loadGuildSettings(guild!!)
        }

        val activities = arrayOf(
                Activity.watching("#BlackLivesMatter"),
                Activity.watching("#BLM"),
                Activity.watching("#JusticeForGeorgeFloyd"),
                Activity.watching("#JusticeForBreonnaTaylor")
        )

        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val randomAnswer = Random().nextInt(activities.size)
                event.jda.presence.activity = activities[randomAnswer]
            }
        }
        timer.schedule(timerTask, 0, 10000)
    }

    override fun onDisconnect(event: DisconnectEvent) {
        event.jda.guilds.forEach(Consumer { guild: Guild -> guild.audioManager.closeAudioConnection() })
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        LilOri.instance.dataManager.createGuild(event.guild, Settings.DEFAULT_PREFIX)
        LilOri.instance.guildSettingsManager.loadGuildSettings(event.guild)
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        LilOri.instance.dataManager.removeGuild(event.guild)
        LilOri.instance.guildSettingsManager.removeGuildSettings(event.guild)
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {

        val embedBuilder = EmbedBuilder()
                .setAuthor("Lil' Ori Bot | Prefix: " + LilOri.instance.guildSettingsManager.getGuildSettings(event.guild)?.getPrefix(), "https://discordapp.com/oauth2/authorize?client_id=581203970203189269&permissions=121498961&scope=bot")
                .setColor(Color.decode("#33539e"))
                .setDescription("""» Discord Utility Bot Created by Oribuin « 
                        
                        • Find all my commands using **${LilOri.instance.guildSettingsManager.getGuildSettings(event.guild)?.getPrefix()}help** 
                        • Find my source code on https://github.com/Oribuin/Lil-Ori/
                        • Website: https://oribuin.xyz/
                        • Donate: https://oribuin.xyz/donate""".trimIndent())
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")

        if (event.message.contentRaw == "<@!581203970203189269>")
            event.channel.sendMessage(embedBuilder.build()).queue()
    }
}