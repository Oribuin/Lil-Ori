package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri

abstract class BotCommand(val bot: LilOri): ListenerAdapter() {

    abstract fun executeCommand(event: CommandEvent)

    fun getAnnotation(command: Class<BotCommand>): CommandInfo {
        return command.getAnnotation(CommandInfo::class.java)
    }

}