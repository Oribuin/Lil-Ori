package xyz.oribuin.lilori.commands.global.administrative

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.util.*
import java.util.function.Consumer

class CmdPerms : Command() {
    init {
        name = "Permissions"
        aliases = listOf("Perms")
        description = "List of permissions the bot has."
        aliases = emptyList()
    }

    override fun executeCommand(event: CommandEvent?) {
        (event ?: return)

        val perms: MutableList<String> = ArrayList()
        event.selfMember.permissions.forEach(Consumer { permission: Permission -> perms.add(permission.getName().toLowerCase().replace("_", " ")) })

        val embedBuilder = EmbedBuilder()
                .setAuthor("Lil' Ori Permissions")
                .setColor(Color.decode("#babaeb"))
                .setDescription(perms.toString().replace("\\[", "").replace("]", "").replace(",", "\n •"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")

        event.reply(embedBuilder.build())
    }

}