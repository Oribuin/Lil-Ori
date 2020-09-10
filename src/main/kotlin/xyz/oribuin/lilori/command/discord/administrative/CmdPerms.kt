package xyz.oribuin.lilori.command.discord.administrative

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import org.apache.commons.lang3.StringUtils
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.function.Consumer

class CmdPerms(bot: LilOri) : Command(bot) {
    init {
        name = "Permissions"
        category = Category(Category.Type.ADMIN)
        aliases = listOf("Perms")
        description = "List of permissions the bot has."
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        val perms = mutableSetOf<String>()

        /// Add all the permissions to the set.
        event.selfMember.permissions.forEach(Consumer { permission: Permission -> perms.add(StringUtils.capitalize(permission.getName()).replace("_", " ")) })

        // Define the embed
        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDCD5 Lil' Ori Permissions")
                .setColor(event.color)
                .setDescription(perms.toString().removePrefix("[").removeSuffix("]").replace(",", "\n **»**"))
                .setFooter("Created by Ori#0004", "https://img.oribuin.xyz/profile.png")

        event.reply(embedBuilder.build())
    }

}