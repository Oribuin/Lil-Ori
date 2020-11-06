package xyz.oribuin.lilori.command.moderation

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import org.apache.commons.lang3.StringUtils
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Cmd
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.util.BotUtils
import xyz.oribuin.lilori.util.EventWaiter

@Cmd(
        name = "Purge",
        description = "Purge messages from the server.",
        category = Category.Type.MODERATION,
        arguments = ["<msg/channel>", "<count/#channel>"],
        aliases = [],
        userPermissions = [Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL],
        botPermissions = [Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL],
        guildId = ""
)
class CmdPurge(bot: LilOri, private val waiter: EventWaiter) : Command(bot) {

    // ignore all of this
    override fun executeCommand(event: CommandEvent) {
        if (event.args.size <= 2) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        when (event.args[1].toLowerCase()) {
            "messages", "msgs" -> {
                try {
                    // Define the message count
                    val messageCount = Integer.parseInt(event.args[2])

                    // Check the message count
                    if (messageCount <= 1 || messageCount > 100) {
                        event.sendEmbedReply("❗ Invalid Number", "You can only purge 2-100 messages due to API Limitations")
                        return
                    }

                    // Delete command
                    event.deleteCmd()

                    // Define the list of messages
                    val messages = event.channel.history.retrievePast(messageCount).complete()

                    // If the message size
                    event.channel.purgeMessages(messages)

                } catch (ex: NumberFormatException) {
                    event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
                }
            }

            "channel" -> {
                // Check if a channel was mentioned
                if (event.message.mentionedChannels.size == 0) {
                    event.sendEmbedReply("❗ Invalid Channel", "Please mention a valid channel.")
                    return
                }

                // Define the channel mentioned
                val channel = event.message.mentionedChannels[0]

                // Check if the channel is a text channel
                if (channel !is TextChannel) {
                    event.sendEmbedReply("❗ Invalid Channel Type", "You can only purge text channels!")
                    return
                }

                if (!event.member.hasPermission(channel) || event.selfMember.hasPermission(channel)) {
                    event.sendEmbedReply("❗ Can't Purge", "I cannot purge that channel due to no permission")
                    return
                }

                // Send the starting confirmation message
                event.channel.sendMessage(event.author.asMention + ", You are about to purge (delete) ${channel.asMention}, Please react with ✅ to continue, React with ❌ to cancel the purge. (Note: __This deletes existing webhooks__)").queue { msg ->
                    // Add the message reactions
                    msg.addReaction("✅").queue()
                    msg.addReaction("❌").queue()

                    // Define the time the message was created
                    val time = event.message.timeCreated

                    // Wait for the GuildMessageReactionAddEvent, Check if the user is the author and the message is the confirmation message
                    waiter.waitForEvent(GuildMessageReactionAddEvent::class.java, { check -> check.user == event.author && check.messageId == msg.id }, { action ->
                        // Switch case the reaction emoji
                        when (action.reactionEmote.emoji) {
                            "✅" -> {
                                // Define the embed message
                                val embedBuilder = EmbedBuilder()
                                        .setAuthor("Successfully Purged Channel")
                                        .setColor(event.color)
                                        .setDescription("""**»** Channel: ${channel.name}
                                        **»** Purged By: ${event.author.asMention}
                                        **»** On: ${StringUtils.capitalize(time.dayOfWeek.name)} at ${time.hour}:${time.minute}""".trimIndent())

                                // Create the new channel with the exact same properties
                                event.guild.createTextChannel(channel.name)
                                        .setNSFW(channel.isNSFW)
                                        .setSlowmode(channel.slowmode)
                                        .setTopic(channel.topic)
                                        .setParent(channel.parent)
                                        .setPosition(channel.position).queue { newChannel ->
                                            newChannel.permissionOverrides.forEach { override -> override.permissionHolder?.let { channel.createPermissionOverride(it) }?.queue() }
                                            newChannel.sendMessage(event.author.asMention).queue()
                                            newChannel.sendMessage(embedBuilder.build()).queue()
                                        }

                                // Delete the mentioned channel
                                channel.delete().queue()

                                // Shutdown the event waiter
                                waiter.shutdown()
                            }

                            "❌" -> {
                                // Send cancel message
                                event.sendEmbedReply("❌ Cancelled Channel Purge", "You have cancelled purging ${channel.asMention}, To retry type ${event.prefix}purge channel ${channel.asMention}!")

                                // Shutdown event waiter
                                waiter.shutdown()
                            }
                        }

                    })
                }
            }
        }
    }
}