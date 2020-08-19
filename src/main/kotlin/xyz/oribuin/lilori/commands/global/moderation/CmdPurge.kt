package xyz.oribuin.lilori.commands.global.moderation

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import org.apache.commons.lang3.StringUtils
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.utils.EventWaiter
import java.util.concurrent.TimeUnit

class CmdPurge(bot: LilOri, private val waiter: EventWaiter) : Command(bot) {

    init {
        name = "Purge"
        aliases = listOf("clear")
        description = "Mass clear server messages."
        arguments = listOf("<msg-count>/channel [#Channel]")
        botPermissions = arrayOf(Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
        userPermissions = arrayOf(Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
        this.isEnabled = true
    }

    // ignore all of this
    override fun executeCommand(event: CommandEvent) {
        // Variables
        val args: Array<String> = event.message.contentRaw.split(" ").toTypedArray()
        if (event.member == null)
            return

        if (args.size <= 2) {
            event.deleteCmd(20, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Correct Format: ;purge " + arguments, 10, TimeUnit.SECONDS)
            return
        }

        when (args[1].toLowerCase()) {
            "messages", "msgs" -> {
                try {
                    val messageCount = Integer.parseInt(args[2])

                    if (messageCount <= 1 || messageCount > 100) {
                        event.deleteCmd(10, TimeUnit.SECONDS)
                        event.timedReply(event.author.asMention + ", You can only purge up to 100 messages due to API Limitations.", 20, TimeUnit.SECONDS)
                        return
                    }

                    event.deleteCmd()

                    val messages: List<Message> = event.channel.history.retrievePast(messageCount).complete()
                    if (messages.size > 1) {
                        event.channel.purgeMessages(messages)
                    }
                } catch (ex: NumberFormatException) {
                    event.deleteCmd(20, TimeUnit.SECONDS)
                    event.timedReply(event.author.asMention + ", Please include a valid number of messages to purge.", 20, TimeUnit.SECONDS)
                }
            }

            "channel" -> {
                if (event.message.mentionedChannels.size == 0) {
                    event.timedReply("${event.author.asMention}, Please mention a channel to purge.", 20, TimeUnit.SECONDS)
                    event.deleteCmd(20, TimeUnit.SECONDS)
                    return
                }

                val channel = event.message.mentionedChannels[0]

                if (channel !is TextChannel) {
                    event.timedReply("${event.author.asMention}, You can only purge Text Channels.", 20, TimeUnit.SECONDS)
                    event.deleteCmd(20, TimeUnit.SECONDS)
                    return
                }

                if (!(event.member ?: return).hasPermission(channel)) {
                    event.timedReply("${event.author.asMention}, You cannot purge this channel.", 20, TimeUnit.SECONDS)
                    event.deleteCmd(20, TimeUnit.SECONDS)
                    return
                }

                event.channel.sendMessage(event.author.asMention + ", You are about to purge (delete) ${channel.asMention}, Please react with ✅ to continue, React with ❌ to cancel the purge. (Note: This deletes existing webhooks").queue { msg ->
                    event.deleteCmd(20, TimeUnit.SECONDS)
                    msg.addReaction("✅").queue()
                    msg.addReaction("❌").queue()

                    val time = event.message.timeCreated

                    waiter.waitForEvent(GuildMessageReactionAddEvent::class.java, { check -> check.user == event.author && check.messageId == msg.id }, { action ->
                        when (action.reactionEmote.emoji) {

                            "✅" -> {
                                val embedBuilder = EmbedBuilder()
                                        .setAuthor("Successfully Purged Channel")
                                        .setColor(Settings.EMBED_COLOR)
                                        .setDescription("""**Channel:** ${channel.name}
                                        **Purged By:** ${event.author.asMention}
                                        **Purged on:** ${StringUtils.capitalize(time.dayOfWeek.name)} at ${time.hour}:${time.minute}""".trimIndent())

                                event.guild.createTextChannel(channel.name)
                                        .setNSFW(channel.isNSFW)
                                        .setSlowmode(channel.slowmode)
                                        .setTopic(channel.topic)
                                        .setParent(channel.parent)
                                        .setPosition(channel.position).queue()

                                //channel.permissionOverrides.forEach { override -> override.permissionHolder?.let { channel.createPermissionOverride(it) }?.queue() }
                                channel.sendMessage(event.author.asMention).queue()
                                channel.sendMessage(embedBuilder.build()).queue()

                                event.textChannel.delete().queue()
                                waiter.shutdown()
                            }

                            "❌" -> {
                                event.timedReply(event.author.asMention + ", You cancelled channel purging, To restart, Retype the command.", 20, TimeUnit.SECONDS)
                                waiter.shutdown()
                            }
                        }

                    })
                }
            }
        }
    }
}