package xyz.oribuin.lilori.commands.moderation;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.persist.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CmdPurge extends Command {

    private EventWaiter waiter;

    public CmdPurge(EventWaiter waiter) {
        this.name = "Purge";
        this.aliases = new String[]{"clear"};
        this.help = "Mass clear server messages.";
        this.cooldown = 2;
        this.category = new Category("Moderation");
        this.guildOnly = true;
        this.arguments = "[Msgs/User/Channel] [Number/#Channel/@User]";

        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL};
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL};

        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {

        // Variables
        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel channel = event.getTextChannel();
        OffsetDateTime time = event.getMessage().getTimeCreated();
        if (event.getGuild().getMember(event.getAuthor()) == null) return;

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Correct Format: ;purge [Msgs/User/Channel] [Number/#Channel/@User]", 10, TimeUnit.SECONDS);
            return;
        }

        if (args[1].equalsIgnoreCase("messages") || args[1].equalsIgnoreCase("msgs")) {
            if (args[2] == null) {
                event.deleteCmd(10, TimeUnit.SECONDS);
                event.timedReply(event.getAuthor().getAsMention() + ", Please include the number of messages to delete. (Between 2-100)", 10, TimeUnit.SECONDS);
                return;
            }

            try {
                int msgCount = Integer.parseInt(args[2]);

                if (msgCount <= 1 || msgCount > 100) {
                    event.timedReply(event.getAuthor().getAsMention() + ", You can only purge up to 100 messages at once, You provided **" + msgCount + "**.", 10, TimeUnit.SECONDS);
                    event.deleteCmd(10, TimeUnit.SECONDS);
                    return;
                }

                event.deleteCmd();
                List<Message> msgs = channel.getHistory().retrievePast(msgCount).complete();

                if (msgs.size() > 1)
                    event.getChannel().purgeMessages(msgs);

            } catch (NumberFormatException e) {
                event.deleteCmd(10, TimeUnit.SECONDS);
                event.timedReply(event.getAuthor().getAsMention() + ", Please include valid arguments.", 10, TimeUnit.SECONDS);
            }
        } else if (args[1].equalsIgnoreCase("channel")) {

            if (event.getMessage().getMentionedChannels().size() == 0) {
                event.timedReply(event.getAuthor().getAsMention() + ", Please mention a text channel to purge.", 10, TimeUnit.SECONDS);
                event.deleteCmd(10, TimeUnit.SECONDS);
                return;
            }

            if (event.getMessage().getMentionedChannels().get(0).getType() != ChannelType.TEXT) {
                event.timedReply(event.getAuthor().getAsMention() + ", Please include a text channel to purge.", 10, TimeUnit.SECONDS);
                event.deleteCmd(10, TimeUnit.SECONDS);
                return;
            }

            if (args[2].equalsIgnoreCase(event.getMessage().getMentionedChannels().get(0).getAsMention())) {
                TextChannel textChannel = event.getMessage().getMentionedChannels().get(0);

                if (textChannel == null) {
                    event.timedReply(event.getAuthor().getAsMention() + ", textChannel is null...", 10, TimeUnit.SECONDS);
                    event.deleteCmd(10, TimeUnit.SECONDS);
                    return;
                }

                if (!event.getGuild().getMember(event.getAuthor()).hasPermission(textChannel)) {
                    event.timedReply(event.getAuthor().getAsMention() + ", You cannot purge this channel.", 10, TimeUnit.SECONDS);
                    event.deleteCmd(10, TimeUnit.SECONDS);
                    return;
                }

                event.timedReply(event.getAuthor().getAsMention() + ", You are about to purge " + textChannel.getAsMention() + ", Please type **confirm** to continue. (Note: This deletes existing webhooks)", 2, TimeUnit.MINUTES);
                event.deleteCmd(10, TimeUnit.SECONDS);

                String getWeek = time.getDayOfWeek().name().substring(0, 1).toUpperCase() + time.getDayOfWeek().name().substring(1).toLowerCase();
                waiter.waitForEvent(GuildMessageReceivedEvent.class,
                        predicate -> predicate.getAuthor().equals(event.getMessage().getAuthor())
                                && event.getMessage().getContentRaw().toLowerCase().contains("confirm"),
                        consumer -> {

                            EmbedBuilder embedBuilder = new EmbedBuilder()
                                    .setAuthor("Successfully Purged Channel")
                                    .setDescription("**Channel:** #" + textChannel.getName() + "\n" +
                                            "**Purged By:** " + event.getAuthor().getAsMention() + "\n" +
                                            "**Purged on:** " + getWeek + " at " + time.getHour() + ":" + time.getMinute());

                            consumer.getMessage().delete().queueAfter(2, TimeUnit.MINUTES);
                            event.getGuild().createTextChannel(textChannel.getName())
                                    .setNSFW(textChannel.isNSFW())
                                    .setSlowmode(textChannel.getSlowmode())
                                    .setTopic(textChannel.getTopic())
                                    .setParent(textChannel.getParent())
                                    .setPosition(textChannel.getPosition()).queue(getChannel -> {
                                getChannel.sendMessage(event.getAuthor().getAsMention()).queue();
                                getChannel.sendMessage(embedBuilder.build()).queue();
                            });
                            textChannel.delete().queue();

                        }, 2, TimeUnit.MINUTES, () -> {
                            if (!waiter.isShutdown())
                                waiter.shutdown();
                        });
            }
        } else if (args[1].

                equalsIgnoreCase("user")) {

            if (event.getMessage().getMentionedMembers().size() == 0) {
                event.timedReply(event.getAuthor().getAsMention() + ", Please include a user to purge.", 10, TimeUnit.SECONDS);
                event.deleteCmd(10, TimeUnit.SECONDS);
                return;
            }

            Member member = event.getMessage().getMentionedMembers().get(0);
            if (member.hasPermission(Permission.ADMINISTRATOR) || member.getUser().isBot() || event.isHigher(member, event.getGuild().getMember(event.getAuthor()))) {
                event.timedReply(event.getMessage().getAuthor().getAsMention() + ", You cannot purge this user's messages.", 10, TimeUnit.SECONDS);
                event.deleteCmd(10, TimeUnit.SECONDS);
                return;
            }

            event.getGuild().getTextChannels().forEach(textChannel -> {
                for (Message message : textChannel.getHistory().retrievePast(100).complete()) {
                    if (message.getAuthor().equals(member.getUser())) {
                        message.delete().queue();
                    }
                }
            });
        } else {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Correct Format: ;purge [Msgs/User/Channel] [Number/#Channel/@User]", 10, TimeUnit.SECONDS);
        }
    }
}