package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.util.EventWaiter

abstract class BotCommand(val bot: LilOri, val waiter: EventWaiter): ListenerAdapter() {

    abstract fun executeCommand(event: CommandEvent)

    fun getAnnotation(command: Class<BotCommand>): CommandInfo {
        return command.getAnnotation(CommandInfo::class.java)
    }

    annotation class CommandInfo(
        val name: String,
        val description: String,
        val category: Category.Type,
        val arguments: Array<String>,
        val aliases: Array<String>,
        val userPermissions: Array<Permission>,
        val botPermissions: Array<Permission>,
        val guildId: String,
        val ownerOnly: Boolean = false
    )

}