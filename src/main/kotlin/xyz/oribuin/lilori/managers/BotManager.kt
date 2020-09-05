package xyz.oribuin.lilori.managers

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.data.GuildSettings
import java.util.*

class BotManager(bot: LilOri): Manager(bot) {
    
    // Define activities list
    var activities = mutableListOf<String>()
    
    override fun enable() {
        // Unused
    }


    fun registerStatus(jda: JDA) {
        jda.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)

        activities = mutableListOf(
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
                jda.presence.activity = activities[randomAnswer]
            }
        }
        timer.schedule(timerTask, 0, 20000)
    }

    fun registerGuilds(jda: JDA) {

        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                for (guild in jda.guilds) {
                    val guildSettings = GuildSettings(guild)
                    bot.getManager(GuildSettingsManager::class).updateGuild(guild, guildSettings.getPrefix(), guildSettings.getColor())

                }
            }
        }

        timer.schedule(timerTask, 5000)
    }
}
