package xyz.oribuin.lilori.managers.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import xyz.oribuin.lilori.LilOri;

import java.util.concurrent.TimeUnit;

public class CommandEvent {

    private final LilOri bot;
    private final GuildMessageReceivedEvent event;

    public CommandEvent(LilOri bot, GuildMessageReceivedEvent event) {
        this.bot = bot;
        this.event = event;
    }

    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    public void reply(String message) {
        event.getChannel().sendMessage(message).queue();
    }

    public void reply(EmbedBuilder embedBuilder) {
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public void reply(MessageEmbed messageEmbed) {
        event.getChannel().sendMessage(messageEmbed).queue();
    }

    public void timedReply(String message, int time, TimeUnit timeUnit) {
        event.getChannel().sendMessage(message).queue(msg -> msg.delete().queueAfter(time, timeUnit));
    }

    public void timedReply(EmbedBuilder embedBuilder, int time, TimeUnit timeUnit) {
        event.getChannel().sendMessage(embedBuilder.build()).queue(msg -> msg.delete().queueAfter(time, timeUnit));
    }

    public void timedReply(MessageEmbed messageEmbed, int time, TimeUnit timeUnit) {
        event.getChannel().sendMessage(messageEmbed).queue(msg -> msg.delete().queueAfter(time, timeUnit));
    }

    public void deleteCmd() {
        event.getMessage().delete().queue();
    }

    public void deleteCmd(int time, TimeUnit timeUnit) {
        try {
            event.getMessage().delete().queueAfter(time, timeUnit);
        } catch (ErrorResponseException ignored) {
        }
    }

    public String getPrefix() {
        return bot.getGuildSettingsManager().getGuildSettings(event.getGuild()).getPrefix();
    }

    public Member getSelfMember() {
        return event.getGuild().getSelfMember();
    }

    public void kickMember(User user) {
        event.getGuild().getMember(user).kick().queue();
    }

    public void kickMember(User user, String reason) {
        event.getGuild().getMember(user).kick(reason).queue();
    }

    public void banMember(User user, int days) {
        event.getGuild().getMember(user).ban(days).queue();
    }

    public void banMember(User user, int days, String reason) {
        event.getGuild().getMember(user).ban(days, reason).queue();
    }

    public void banMember(Guild guild, User user, int days) {
        guild.getMember(user).ban(days).queue();
    }

    public void banMember(Guild guild, User user, int days, String reason) {
        guild.getMember(user).ban(days, reason).queue();
    }


    // GuildMessageReceivedEvent shortcuts

    public User getAuthor() {
        return event.getAuthor();
    }

    public MessageChannel getChannel() {
        return event.getChannel();
    }

    public TextChannel getTextChannel() {
        return (TextChannel) this.getChannel();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public JDA getJDA() {
        return event.getJDA();
    }

    public Member getMember() {
        return event.getMember();
    }

    public Message getMessage() {
        return event.getMessage();
    }

    public PrivateChannel getPrivateChannel() {
        return event.getAuthor().openPrivateChannel().complete();
    }

    public long getResponseNumber() {
        return event.getResponseNumber();
    }
}
