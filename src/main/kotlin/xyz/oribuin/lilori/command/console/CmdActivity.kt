package xyz.oribuin.lilori.command.console

import net.dv8tion.jda.api.entities.Activity
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.console.ConsoleCMD
import xyz.oribuin.lilori.handler.console.ConsoleEvent
import xyz.oribuin.lilori.manager.BotManager

class CmdActivity(bot: LilOri) : ConsoleCMD(bot) {

    var isLooping = true

    companion object {
        lateinit var instance: CmdActivity
    }

    init {
        name = "activity"
        aliases = listOf("activity")
        description = "Description"
        instance = this
    }

    override fun executeCommand(event: ConsoleEvent) {
        if (event.args.size == 1) {
            event.print("Activity Help Commands:")
            event.print("/activity add <ActivityType> <Activity>")
            event.print("/activity set <ActivityType> <Activity>")
            event.print("/activity remove <ActivityType> <Activity>")
            return
        }

        if (event.args.size > 1) {
            if (event.args.size >= 4) {

                val activity = java.lang.String.join(" ", *event.args).substring(1)

                when (event.args[1].toLowerCase()) {
                    "set" -> {
                        val activityType = Activity.ActivityType.valueOf(event.args[2].toUpperCase())
                        event.jda?.presence?.activity = Activity.of(activityType, activity)
                        event.print("Changed the status to ${activityType.name.toLowerCase()} $activity")
                        isLooping = false
                    }

                    "add" -> {
                        val activityType = Activity.ActivityType.valueOf(event.args[2].toUpperCase())
                        bot.getManager(BotManager::class).activities.add(Activity.of(activityType, activity))
                        event.print("Added value to the activity list ${activityType.name.toLowerCase()} $activity")
                    }

                    "toggle" -> {
                        isLooping = !isLooping
                        event.print("Activity cycling is now set to $isLooping")
                    }
                }
            }
        }
    }
}