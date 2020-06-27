package xyz.oribuin.lilori.commands.global.moderation

import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdPurge() : Command() {

    init {
        name = "Purge"
        aliases = listOf("clear")
        description = "Mass clear server messages."
        botPermissions = arrayOf(Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
        userPermissions = arrayOf(Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
        this.isEnabled = false;
    }

    // ignore all of this
    override fun executeCommand(event: CommandEvent?) {

        // i dont want to talk about it
        /*
        // Variables
        val args: Array<String?> = event!!.message.contentRaw.split(" ").toTypedArray()
        val channel = event.channel as TextChannel
        val time = event.message.timeCreated
        if (event.member == null) return
        if (args.size <= 2) {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Correct Format: ;purge " + arguments, 10, TimeUnit.SECONDS)
            return
        }
        if (args[1].equals("messages", ignoreCase = true) || args[1].equals("msgs", ignoreCase = true)) {
            if (args[2] == null) {
                event.deleteCmd(10, TimeUnit.SECONDS)
                event.timedReply(event.author.asMention + ", Please include the number of messages to delete. (Between 2-100)", 10, TimeUnit.SECONDS)
                return
            }
            try {
                val msgCount = args[2]!!.toInt()
                if (msgCount <= 1 || msgCount > 100) {
                    event.timedReply(event.author.asMention + ", You can only purge up to 100 messages at once, You provided **" + msgCount + "**.", 10, TimeUnit.SECONDS)
                    event.deleteCmd(10, TimeUnit.SECONDS)
                    return
                }
                event.deleteCmd()
                val msgs = channel.history.retrievePast(msgCount).complete()
                if (msgs.size > 1) event.channel.purgeMessages(msgs)
            } catch (e: NumberFormatException) {
                event.deleteCmd(10, TimeUnit.SECONDS)
                event.timedReply(event.author.asMention + ", Please include valid arguments.", 10, TimeUnit.SECONDS)
            }
        } else if (args[1].equals("channel", ignoreCase = true)) {
            if (event.message.mentionedChannels.size == 0) {
                event.timedReply(event.author.asMention + ", Please mention a text channel to purge.", 10, TimeUnit.SECONDS)
                event.deleteCmd(10, TimeUnit.SECONDS)
                return
            }
            if (event.message.mentionedChannels[0].type != ChannelType.TEXT) {
                event.timedReply(event.author.asMention + ", Please include a text channel to purge.", 10, TimeUnit.SECONDS)
                event.deleteCmd(10, TimeUnit.SECONDS)
                return
            }
            if (args[2].equals(event.message.mentionedChannels[0].asMention, ignoreCase = true)) {
                val textChannel = event.message.mentionedChannels[0]
                if (textChannel == null) {
                    event.timedReply(event.author.asMention + ", textChannel is null...", 10, TimeUnit.SECONDS)
                    event.deleteCmd(10, TimeUnit.SECONDS)
                    return
                }
                if (event.member != null) if (!event.member!!.hasPermission(textChannel)) {
                    event.timedReply(event.author.asMention + ", You cannot purge this channel.", 10, TimeUnit.SECONDS)
                    event.deleteCmd(10, TimeUnit.SECONDS)
                    return
                }
                event.timedReply(event.author.asMention + ", You are about to purge " + textChannel.asMention + ", Please type **confirm** to continue. (Note: This deletes existing webhooks)", 2, TimeUnit.MINUTES)
                event.deleteCmd(10, TimeUnit.SECONDS)
                val getWeek = time.dayOfWeek.name.substring(0, 1).toUpperCase() + time.dayOfWeek.name.substring(1).toLowerCase()
                waiter.waitForEvent(GuildMessageReceivedEvent::class.java,
                        Predicate { predicate: GuildMessageReceivedEvent ->
                            (predicate.author == event.message.author && predicate.message.contentRaw.toLowerCase().contains("confirm")
                                    || predicate.message.contentRaw.toLowerCase().contains("cancel"))
                        },
                        Consumer { consumer: GuildMessageReceivedEvent ->
                            if (consumer.message.contentRaw.toLowerCase().contains("confirm")) {
                                val embedBuilder = EmbedBuilder()
                                        .setAuthor("Successfully Purged Channel")
                                        .setDescription("""**Channel:** #${textChannel.name}
**Purged By:** ${event.author.asMention}
**Purged on:** $getWeek at ${time.hour}:${time.minute}""")
                                consumer.message.delete().queueAfter(2, TimeUnit.MINUTES)
                                event.guild.createTextChannel(textChannel.name)
                                        .setNSFW(textChannel.isNSFW)
                                        .setSlowmode(textChannel.slowmode)
                                        .setTopic(textChannel.topic)
                                        .setParent(textChannel.parent)
                                        .setPosition(textChannel.position).queue { getChannel: TextChannel ->
                                            textChannel.permissionOverrides.forEach(Consumer { permissionOverride: PermissionOverride -> getChannel.createPermissionOverride(permissionOverride.permissionHolder!!).queue() })
                                            getChannel.sendMessage(event.author.asMention).queue()
                                            getChannel.sendMessage(embedBuilder.build()).queue()
                                        }
                                textChannel.delete().queue()
                            } else {
                                waiter.shutdown()
                                consumer.message.addReaction("✅").queue()
                            }
                        }, 2, TimeUnit.MINUTES, Runnable { if (waiter.isShutdown) waiter.shutdown() })
            }
        } else if (args[1].equals("user", ignoreCase = true)) {
            if (event.message.mentionedMembers.size == 0) {
                event.timedReply(event.author.asMention + ", Please include a user to purge.", 10, TimeUnit.SECONDS)
                event.deleteCmd(10, TimeUnit.SECONDS)
                return
            }
            val member = event.message.mentionedMembers[0]
            // || member.getUser().isBot() || event.isHigher(member, event.getMember())
            if (member.hasPermission(Permission.ADMINISTRATOR)) {
                event.timedReply(event.message.author.asMention + ", You cannot purge this user's messages.", 10, TimeUnit.SECONDS)
                event.deleteCmd(10, TimeUnit.SECONDS)
                return
            }
            event.guild.textChannels.forEach(Consumer { textChannel: TextChannel ->
                for (message in textChannel.history.retrievePast(100).complete()) {
                    if (message.author == member.user) {
                        message.delete().queue()
                    }
                }
            })
        } else {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Correct Format: ;purge [Msgs/User/Channel] [Number/#Channel/@User]", 10, TimeUnit.SECONDS)
        }

         */
    }
}