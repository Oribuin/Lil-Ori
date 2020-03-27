package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class CmdPing extends Command {
    public CmdPing() {
        this.name = "Ping";
        this.aliases = new String[]{"latency"};
        this.help = "Get the latency ping for the bot.";
        this.category = new Category("Info");
        this.arguments = "[None]";
    }

    @Override
    protected void execute(CommandEvent event) {
        long ping = event.getJDA().getGatewayPing();

        String emoji = null;

        EmbedBuilder Embed = new EmbedBuilder();

        if (ping < 101) {
            emoji = "<a:PanGLoveG:644305899682398208>";
            Embed.setColor(Color.green);
        }

        // If Ping is Higher than 100, Do yellow
        if (ping > 100) {
            emoji = "<a:PanGLoveY:644305952966967316>";
            Embed.setColor(Color.decode("#ffff00"));
        }

        // If Ping is Higher than 200 Do Orange
        if (ping > 199) {
            emoji = "<a:PanGLoveO:644305938873974784> ";
            Embed.setColor(Color.decode("#ffa500"));
        }

        // If Ping is higher than 300, Do Red
        if (ping > 299) {
            emoji = "<a:PanGLove:644305899292459009>";
            Embed.setColor(Color.red);
        }

        Embed.setDescription("**" + ping + "ms latency** " + emoji);
        event.reply(Embed.build());
    }
}