package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri

open class Command : ListenerAdapter() {

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
    lateinit var userPermissions: Array<Permission>
        protected set
    lateinit var botPermissions: Array<Permission>
        protected set

    open fun executeCommand(event: CommandEvent?) {
        // Unused
    }

}