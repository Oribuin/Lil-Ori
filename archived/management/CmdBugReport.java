package xyz.oribuin.lilori.commands.author.management;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import xyz.oribuin.lilori.persist.GuildWhitelist;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CmdBugReport extends Command {

    public CmdBugReport() {
        this.name = "Report";
        this.arguments = "[PastebinURL]";
        this.category = new Category("Support");
        this.help = "Create a plugin bug report";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (!event.getGuild().getId().equals(GuildWhitelist.OPlugins)) {
            event.reply(event.getAuthor().getAsMention() + ", You cannot execute this command in this guild.");
            return;
        }

        if (args.length < 2) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Correct usage `;report [PastebinURL] `").queue(then -> then.delete().queueAfter(10, TimeUnit.SECONDS));
            event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);
            return;
        }


        if (event.getGuild().getTextChannelsByName("bug-reports", true).size() == 0) {
            if (event.getGuild().getOwner() != null)
                event.reply(event.getGuild().getOwner().getAsMention() + ", There is no #bug-reports channel.");
            return;
        }


        if (!args[1].startsWith("https://pastebin.com/")) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Please put your bug report error pastebin Example: `;report https://pastebin.com/123456`").queue(then -> {
                then.delete().queueAfter(20, TimeUnit.SECONDS);
            });
            event.getMessage().delete().queueAfter(20, TimeUnit.SECONDS);
            return;
        }

        EmbedBuilder Embed = new EmbedBuilder()
                .setAuthor("Bug Report Incoming")
                .addField("Author", event.getAuthor().getAsTag(), false)
                .addField("URL", args[1], false)
                .setColor(Color.RED);

        TextChannel channel = event.getGuild().getTextChannelsByName("bug-reports", true).get(0);
        channel.sendMessage(event.getGuild().getRolesByName("Admin", true).get(0).getAsMention()).queue();
        channel.sendMessage(Embed.build()).queue();

        event.getMessage().delete().queue();
        event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Thank you for reporting a bug! Developers have been notified. <a:PanGLoveG:644305899682398208>").queue(then -> then.delete().queueAfter(10, TimeUnit.SECONDS));

        System.out.println(event.getAuthor().getAsTag() + " Just reported a bug (" + args[1] + ")");
    }
}
