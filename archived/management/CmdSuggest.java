package xyz.oribuin.lilori.commands.author.management;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import xyz.oribuin.lilori.persist.GuildWhitelist;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CmdSuggest extends Command {

    private EventWaiter waiter;
    public List<String> plugins = new ArrayList<>();

    public CmdSuggest(EventWaiter waiter) {
        this.name = "Suggest";
        this.arguments = "[Plugin] [Suggestion]";
        this.category = new Category("Suggestion");
        this.guildOnly = true;
        this.help = "Create a suggestion for the plugins.";

        this.waiter = waiter;
    }

    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        plugins.add("adminnotice");
        plugins.add("chatemoji");
        plugins.add("craftholograms");
        plugins.add("headowner");
        plugins.add("itemsettings");
        plugins.add("flighttrails");

        if (!event.getGuild().getId().equals(GuildWhitelist.OPlugins)) {
            event.reply(event.getAuthor().getAsMention() + ", You cannot execute this command in this guild.");
            return;
        }


        if (args.length < 3) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Correct usage `;suggest [Plugin] [Suggestion]`").queue(then -> then.delete().queueAfter(10, TimeUnit.SECONDS));
            event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
            return;
        }

        if (!plugins.contains(args[1].toLowerCase())) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", You have not input a correct plugin. Type ;plugins to see the list of available plugins.").queue(then -> then.delete().queueAfter(20, TimeUnit.SECONDS));
            event.getMessage().delete().queueAfter(20, TimeUnit.SECONDS);
            return;
        }


        final String input = event.getMessage().getContentRaw().substring(args[0].length() + args[1].length() + 2);

        if (event.getGuild().getTextChannelsByName("suggestions", true).size() == 0) {
            event.reply(event.getAuthor().getAsMention() + ", There is no suggestion channel defined.");
            return;
        }

        EmbedBuilder Embed = new EmbedBuilder()
                .setAuthor("Lil' Ori Suggestions")
                .setDescription("A new suggestion has been submitted.\n" +
                        "Use the reactions to upvote or downvote the suggestion")

                .setFooter("Lil' Ori Bot", event.getGuild().getMemberById(event.getClient().getOwnerId()).getUser().getAvatarUrl())
                .addField("Plugin", args[1], false)
                .addField("Suggestion", input, false);


        event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Please click \":white_check_mark:\" to submit or :x: to cancel suggestion.").queue(then -> {
            then.addReaction("✅").queue();
            then.addReaction("❌").queue();
            then.delete().queueAfter(30, TimeUnit.SECONDS);
        });


        waiter.waitForEvent(GuildMessageReactionAddEvent.class, e -> e.getUser().equals(event.getAuthor())
                && e.getReaction().getChannel().equals(event.getChannel()), action -> {

            if (action.getReactionEmote().getEmoji().equals("✅")) {
                event.getGuild().getTextChannelsByName("suggestions", true).get(0).sendMessage(Embed.build()).queue(message -> {
                    message.addReaction(":Upvote:669269751599726625").queue();
                    message.addReaction(":Downvote:669269751767367690").queue();
                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                });

                try {
                    event.getAuthor().openPrivateChannel().queue(author -> author.sendMessage(event.getAuthor().getAsMention() + ", You have submitted your suggestion into " + event.getGuild().getTextChannelsByName("suggestions", true).get(0).getAsMention() + "!").queue());
                } catch (Exception ignored) {
                }
            } else if (action.getReactionEmote().getEmoji().equals("❌")) {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", You have cancelled your suggestion").queue(then -> then.delete().queueAfter(10, TimeUnit.SECONDS));


            } else {
                Message message = action.getChannel().retrieveMessageById(action.getMessageId()).complete();

                if (message.getAuthor().equals(event.getJDA().getSelfUser())
                        && !action.getReactionEmote().getEmoji().equals("❌")
                        && !action.getReactionEmote().getEmoji().equals("✅")
                        && message.getMentionedMembers().get(0).equals(event.getAuthor())
                        && !action.getUser().equals(event.getAuthor())) {

                    action.getReaction().removeReaction(action.getUser()).queue();
                }
            }

        }, 1, TimeUnit.MINUTES, () -> event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", You have cancelled your suggestion.").queue(then -> {
            then.delete().queueAfter(30, TimeUnit.SECONDS);
        }));

        event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
    }
}