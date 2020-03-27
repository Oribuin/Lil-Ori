package xyz.oribuin.lilori.commands.author.management;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.persist.GuildWhitelist;

public class CmdTicketDelete extends Command {

    public CmdTicketDelete() {
        this.name = "Close";
        this.guildOnly = true;
        this.help = "Close your ticket channel.";
        this.arguments = "[None]";
        this.category = new Category("Support");
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!event.getMessage().getGuild().getId().equals(GuildWhitelist.OPlugins)) {
            event.getChannel().sendMessage(":warning: You cannot use this command in this guild.").queue();
            return;
        }

        if (!event.getTextChannel().getParent().equals("669940814255751168") && !event.getTextChannel().getName().endsWith("-ticket")) {
            event.getChannel().sendMessage(event.getClient().getWarning() + " You are not in the correct channel to use this command.").queue();
            return;
        }

        event.getTextChannel().delete().queue();
        event.getAuthor().openPrivateChannel().queue(author -> author.sendMessage(event.getClient().getSuccess() + " You have successfully deleted your ticket channel. Type `;ticket` to reopen your channel.").queue());
    }
}