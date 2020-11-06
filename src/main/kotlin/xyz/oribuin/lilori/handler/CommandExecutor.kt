package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.exceptions.PermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.data.GuildSettings
import java.text.SimpleDateFormat

class CommandExecutor(private val bot: LilOri, private val commandHandler: CommandHandler) : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {

        // TODO: Create a command client to store owner id
        val guildSettings = GuildSettings(event.guild)
        val content = event.message.contentRaw.toLowerCase()
        val prefix = guildSettings.getPrefix().toLowerCase()

        if (!guildSettings.getPrefix().toLowerCase().let { content.startsWith(it) })
            return

        // Filter through each command
        for (cmd in commandHandler.commands) {
            try {
                val cmdInfo = cmd.getAnnotation(cmd.javaClass)
                val args = event.message.contentRaw.split(" ").toTypedArray()

                // Check if command name or alias
                if (!cmdInfo.name.equals(args[0].substring(prefix.length), ignoreCase = true) && cmdInfo.aliases.toList().stream().noneMatch { x -> x.equals(args[0].toLowerCase().substring(prefix.length), true) })
                    continue


                if (cmdInfo.guildId.isNotEmpty() && cmdInfo.guildId != event.guild.id)
                    return

                // Check if the command is owner only and the owner id equals to command author
                if (cmdInfo.ownerOnly && event.author.id != Settings.OWNER_ID) {
                    val embed = EmbedBuilder()
                            .setAuthor("\uD83D\uDC94 No Permission!")
                            .setColor(GuildSettings(event.guild).getColor())
                            .setDescription("Only a developer can access this command!")
                            .setFooter("Sorry :(")
                    event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
                    return
                }

                // Check if the command author is a bot
                if (event.author.isBot) return

                // Check user permissions
                if (cmdInfo.botPermissions.isNotEmpty() && !event.guild.selfMember.permissions.containsAll(cmdInfo.botPermissions.toList())) {
                    val embed = EmbedBuilder()
                            .setAuthor("\uD83D\uDC94 No Permission!")
                            .setColor(GuildSettings(event.guild).getColor())
                            .setDescription("I do not have enough permissions for this command!")
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
                    return
                }

                // Check user permissions
                if (cmdInfo.userPermissions.isNotEmpty() && event.member?.permissions?.containsAll(cmdInfo.userPermissions.toList()) == false) {
                    val embed = EmbedBuilder()
                            .setAuthor("\uD83D\uDC94 No Permission!")
                            .setColor(GuildSettings(event.guild).getColor())
                            .setDescription("You do not have enough permissions for this command!")
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
                }


                // Execute this command
                cmd.executeCommand(CommandEvent(bot, event))

            } catch (ex: PermissionException) {
                // Send permission exception log to console
                println(("Error Running Command: ${cmd.getAnnotation(cmd.javaClass).name} " +
                        "Guild: ${event.guild.name} " +
                        "Author: ${event.author.asTag} " +
                        "Permission: ${ex.permission}").trimIndent())
                ex.printStackTrace()
            }
        }
    }
}