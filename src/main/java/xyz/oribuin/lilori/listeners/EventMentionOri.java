package xyz.oribuin.lilori.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.LilOri;

import java.awt.*;

public class EventMentionOri extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Lil' Ori Bot | Prefix: " + LilOri.getInstance().getGuildSettingsManager().getGuildSettings(event.getGuild()).getPrefix(), "https://discordapp.com/oauth2/authorize?client_id=581203970203189269&permissions=121498961&scope=bot")
                .setColor(Color.decode("#33539e"))
                .setDescription("» Discord JDA Utility Bot Created by Oribuin «\n" +
                        "\n" +
                        "• Find all my commands using **;help**\n" +
                        "• Find my source code on https://github.com/Oribuin/Lil-Ori/\n" +
                        "• Website: https://oribuin.xyz/")
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png");

        if (event.getMessage().getContentRaw().equals("<@!581203970203189269>"))
            event.getChannel().sendMessage(embedBuilder.build()).queue();

        if (event.getMessage().getContentRaw().contains("<@!345406020450779149>") && !event.getAuthor().getId().equals("345406020450779149"))
            event.getMessage().addReaction(":AngySnoot1:690641610983079967").queue();
    }
}
