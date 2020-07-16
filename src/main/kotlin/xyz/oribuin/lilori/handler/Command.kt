package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri

abstract class Command : ListenerAdapter() {

    @JvmField
    val bot: LilOri = LilOri.instance
    var name: String? = null
        protected set
    var description: String? = null
        protected set
    var arguments: List<String>? = null
        protected set
    var aliases: List<String>? = null
        protected set
    var isEnabled = true
        protected set
    var isOwnerOnly = false
        protected set
    var userPermissions: Array<Permission> = emptyArray()
        protected set
    var botPermissions: Array<Permission> = emptyArray()
        protected set
    var guildId: String? = null
        protected set

    abstract fun executeCommand(event: CommandEvent)

}