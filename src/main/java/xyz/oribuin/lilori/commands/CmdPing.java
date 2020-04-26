package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.awt.*;

public class CmdPing extends Command {
    public CmdPing() {
        this.name = "Ping";
        this.aliases = new String[]{"latency"};
        this.help = "Get the latency ping for the bot.";
        this.category = new Category("Info");
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent event) {
        long ping = event.getJDA().getGatewayPing();

        String emoji = null;

        EmbedBuilder Embed = new EmbedBuilder();

        if (ping < 101) {
            emoji = "<a:PanGLoveG:702322097195712523>";
            Embed.setColor(Color.green);
        }

        // If Ping is Higher than 100, Do yellow
        if (ping > 100) {
            emoji = "<a:PanGLoveY:702322097292181534>";
            Embed.setColor(Color.decode("#ffff00"));
        }

        // If Ping is Higher than 200 Do Orange
        if (ping > 199) {
            emoji = "<a:PanGLoveO:702322097002643577>";
            Embed.setColor(Color.decode("#ffa500"));
        }

        // If Ping is higher than 300, Do Red
        if (ping > 299) {
            emoji = "<a:PanGLove:702322097979916298>";
            Embed.setColor(Color.red);
        }

        Embed.setDescription("**" + ping + "ms latency** " + emoji);
        event.reply(Embed.build());
    }
}