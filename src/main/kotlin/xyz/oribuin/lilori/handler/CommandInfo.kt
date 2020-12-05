package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.Permission

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