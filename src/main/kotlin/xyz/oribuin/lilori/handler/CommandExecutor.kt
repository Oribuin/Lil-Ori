package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.exceptions.PermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings

class CommandExecutor(private val bot: LilOri, private val commandHandler: CommandHandler) : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {

        // TODO: Create a command client to store owner id
        val guildSettings = bot.guildSettingsManager.getGuildSettings(event.guild)
        val content = event.message.contentRaw.toLowerCase()

        if (!content.startsWith(guildSettings!!.getPrefix().toLowerCase()))
            return

        // Filter through each command
        for (cmd in commandHandler.commandList()) {
            try {
                val args = event.message.contentRaw.split(" ").toTypedArray()
                requireNotNull(cmd.aliases) { "Null Aliases in command " + cmd.name }

                // Check if command name or alias
                if (!cmd.name.equals(args[0].substring(1), ignoreCase = true) &&
                        cmd.aliases!!.stream().noneMatch { x: String -> x.equals(args[0].substring(1), ignoreCase = true) }) continue

                // Check if command is enabled
                if (!cmd.isEnabled) return

                // Check if the command is owner only and the owner id equals to command author
                if (cmd.isOwnerOnly && event.author.id != Settings.OWNER_ID) {
                    val embed = EmbedBuilder()
                            .setAuthor("\uD83D\uDC94 No Permission!")
                            .setColor(Settings.EMBED_COLOR)
                            .setDescription("Only a developer can access this command!")
                            .setFooter("Sorry :(")
                    event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
                    return
                }

                // Check if the command author is a bot or fake
                if (event.author.isBot || event.author.isFake) return

                println(48)
                // Check user permissions
                if (cmd.botPermissions.isNotEmpty() && !event.guild.selfMember.permissions.containsAll(cmd.botPermissions.toList())) {
                    val embed = EmbedBuilder()
                            .setAuthor("\uD83D\uDC94 No Permission!")
                            .setColor(Settings.EMBED_COLOR)
                            .setDescription("I do not have enough permissions for this command!")
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
                    return
                }

                println(60)
                
                // Check user permissions
                if (cmd.userPermissions.isNotEmpty() && event.member?.permissions?.containsAll(cmd.userPermissions.toList()) == false) {
                    val embed = EmbedBuilder()
                            .setAuthor("\uD83D\uDC94 No Permission!")
                            .setColor(Settings.EMBED_COLOR)
                            .setDescription("You do not have enough permissions for this command!")
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    event.channel.sendMessage(event.author.asMention).embed(embed.build()).queue()
                }


                // Execute this command
                println("Executed command: ${cmd.name}")
                cmd.executeCommand(CommandEvent(bot, event))
            } catch (ex: PermissionException) {
                // Send permission exception log to console
                println(("Error Running Command: ${cmd.name} " +
                        "Guild: ${event.guild.name} " +
                        "Author: ${event.author.asTag} " +
                        "Permission: ${ex.permission}").trimIndent())
            }
        }
    }

}