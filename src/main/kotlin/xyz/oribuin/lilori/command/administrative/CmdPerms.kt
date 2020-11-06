package xyz.oribuin.lilori.command.administrative

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import org.apache.commons.lang3.StringUtils
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.function.Consumer

@CommandInfo(
        name = "permissions",
        description = "Get all the permissions the bot has access to.",
        category = Category.Type.ADMIN,
        arguments = [],
        aliases = ["perms"],
        userPermissions = [Permission.ADMINISTRATOR],
        botPermissions = [],
        guildId = "",
)

class CmdPerms(bot: LilOri) : BotCommand(bot) {

    override fun executeCommand(event: CommandEvent) {
        val perms = mutableSetOf<String>()

        event.member
        /// Add all the permissions to the set.
        event.selfMember.permissions.forEach(Consumer { permission: Permission -> perms.add(StringUtils.capitalize(permission.getName()).replace("_", " ")) })

        // Define the embed
        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDCD5 Lil' Ori Permissions")
                .setColor(event.color)
                .setDescription(perms.toString().removePrefix("[").removeSuffix("]").replace(",", "\n **»**"))
                .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")

        event.reply(embedBuilder.build())
    }

}