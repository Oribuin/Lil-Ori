Okaypackage xyz.oribuin.lilori.commands.author.management;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.persist.GuildWhitelist;
import xyz.oribuin.lilori.persist.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CmdTicketCreate extends Command {

    public CmdTicketCreate() {
        this.name = "Ticket";
        this.category = new Category("Support");
        this.help = "Create a personal ticket channel";
        this.guildOnly = true;
        this.arguments = "[None]";
    }

    @Override
    protected void execute(CommandEvent event) {
        /**
         * Permission Check
         * Create a list of Permissions
         * If the bot and sender does not have permission
         * Create the embed and list all the permissions they need
         */

        List<Permission> perms = new ArrayList<>();

        perms.add(Permission.MANAGE_CHANNEL);
        perms.add(Permission.MANAGE_PERMISSIONS);
        perms.add(Permission.MANAGE_ROLES);

        if (!event.getGuild().getSelfMember().getPermissions().containsAll(perms) && !event.getGuild().getMember(event.getAuthor()).getPermissions().containsAll(perms)) {
            EmbedBuilder Embed = new EmbedBuilder()
                    .setColor(Color.decode("#cca8db"))
                    .setDescription("**Permissions Required:** \n" + perms.toString()
                            .replaceAll("\\[", "")
                            .replaceAll("]", "")
                            .replaceAll(",", "\n"));

            event.reply(Embed.build());
            return;
        }

        /*
         * Define username variables
         */
        String username = event.getMessage().getAuthor().getName().replace("/[^a-z0-9]/gi", "");
        String channelName = username.toLowerCase() + "-ticket";


        /*
         * If the command is sent outside of Ori-Plugins discord server
         * tell the sender
         */
        if (!event.getGuild().getId().equals(GuildWhitelist.OPlugins)) {
            event.reply(event.getClient().getWarning() + " You cannot use this command in this server.");
            return;
        }

        /*
         * If the bot finds a channel with the String channelName in the guild
         * tell the sender
         */

        if (event.getGuild().getTextChannelsByName(channelName, false).size() == 1) {
            event.reply(event.getClient().getWarning() + " You already have a ticket channel.");
            return;
        }

        event.getMessage().delete().queue();
		
		/*
		 * Create the embed that is sent to the ticket channel.
		 */

        EmbedBuilder MessageEmbed = new EmbedBuilder()
                .setAuthor("Lil' Ori Tickets")
                .setDescription("Welcome to your personal ticket channel, " + event.getAuthor().getName() + ", \n" +
                        "Admins will assist you soon, If you wish to close this channel, \n" +
                        "Type ;close")
                .setFooter("Created by Ori#0004");

		/*
		 * If the ticket category has been deleted, warn the player when they try to make the ticket.
		 */
        if (event.getGuild().getCategoryById("669940814255751168") == null) {
            event.getGuild().getOwner().getUser().openPrivateChannel().queue(owner -> owner.sendMessage(event.getClient().getWarning() + " The ticket channel has been deleted, tickets will not work."));
            return;
		}
		
		/**
		 * Get the category defined above and create the channel with the String above
		 * in that channel set the topic of the channel and add the permission overrides to it.
		 * Send the embed into the channel and tell the player their channel was created.
		 */

        event.getGuild().getCategoryById("669940814255751168").createTextChannel(channelName).queue(channel -> {
            channel.getManager().setTopic("Your personal ticket channel. - :ticket: - By Ori#0004").queue();
            channel.putPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
            if (event.getGuild().getMember(event.getAuthor()) != null)
                channel.createPermissionOverride(event.getGuild().getMember(event.getAuthor())).setAllow(Permission.VIEW_CHANNEL).queue();

            channel.sendMessage(MessageEmbed.build()).queue();
            event.getChannel().sendMessage(event.getClient().getSuccess() + " You have created a ticket channel. " + channel.getAsMention()).queue();
        });
    }
}
