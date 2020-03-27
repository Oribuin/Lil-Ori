package xyz.oribuin.lilori.commands.author.management;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import xyz.oribuin.lilori.persist.GuildWhitelist;

import java.awt.*;

public class CmdCancelSuggest extends Command {

    public CmdCancelSuggest() {
        this.name = "CancelSuggest";
        this.aliases = new String[] {"csuggest"};
        this.help = "Deny a suggestion";
        this.category = new Category("Admin");
        this.arguments = "[SuggestionId] [Reason]";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (!event.getAuthor().getId().equals(event.getClient().getOwnerId())) {
            event.reply(event.getAuthor().getAsMention() + ", You cannot execute this command.");
            return;
        }

        if (!event.getGuild().getId().equals(GuildWhitelist.OPlugins)) {
            event.reply(event.getAuthor().getAsMention() + ", You cannot execute this command in this guild.");
            return;
        }

        if (!event.getChannel().getName().equalsIgnoreCase("suggestions")) {
            event.reply(event.getAuthor().getAsMention() + ", You cannot execute this command in this channel.");
            return;
        }

        if (args.length <= 2) {
            event.getMessage().delete().queue();
            return;
        }

        final String input = event.getMessage().getContentRaw().substring(args[0].length() + args[1].length() + 2);
        EmbedBuilder Embed = new EmbedBuilder();

        event.getChannel().retrieveMessageById(args[1]).queue(message -> {
            MessageEmbed sugEmbed = message.getEmbeds().get(0);
            if (message.getReactions().size() > 0) {
                message.clearReactions().queue();
            }

            Embed.setDescription(sugEmbed.getDescription() + "\n\n**Deny Reason**\n" + input);
            Embed.setColor(Color.RED);
            if (message.getEmbeds().get(0).getAuthor().getName() != null)
                Embed.setAuthor(sugEmbed.getAuthor().getName());

            if (sugEmbed.getFooter().getText() != null && sugEmbed.getFooter().getIconUrl() != null)
                Embed.setFooter(sugEmbed.getFooter().getText(), sugEmbed.getFooter().getIconUrl());


            Embed.addField(sugEmbed.getFields().get(0));
            Embed.addField(sugEmbed.getFields().get(1));

            message.editMessage(Embed.build()).queue();
        });

        event.getChannel().retrieveMessageById(args[1]).queue(message -> {
            message.addReaction("\\u274C").queue();
            event.getMessage().delete().queue();
        });
    }
}
