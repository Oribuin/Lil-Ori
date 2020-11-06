package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri

abstract class Command(val bot: LilOri): ListenerAdapter() {

    abstract fun executeCommand(event: CommandEvent)

    fun getAnnotation(command: Class<Command>): Cmd {
        return command.getAnnotation(Cmd::class.java)
    }

}