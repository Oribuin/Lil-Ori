package xyz.oribuin.lilori.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;

public class CmdFeed extends Command {
    private int cookies;

    public CmdFeed() {
        this.name = "Feed";
        this.aliases = new String[]{"snack"};
        this.description = "Feed' Lil Ori Cookies.";
        //this.arguments = "";
    }

    @Override
    public void executeCommand(CommandEvent event) {
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
