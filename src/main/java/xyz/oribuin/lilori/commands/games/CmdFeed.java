package xyz.oribuin.lilori.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.awt.*;

public class CmdFeed extends Command {
    private int cookies;

    public CmdFeed() {
        this.name = "Feed";
        this.aliases = new String[]{"snack"};
        this.help = "Feed' Lil Ori Cookies.";
        this.category = new Category("Games");
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length >= 2) {
            event.reply("**Lil' Ori now has " + cookies + " cookies!**");
            return;
        }

        cookies++;
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("You gave Lil' Ori A Cookie!")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**He now has " + cookies + " Cookies**");

        event.reply(embedBuilder.build());
    }
}