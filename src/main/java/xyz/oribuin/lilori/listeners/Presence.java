package xyz.oribuin.lilori.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Presence extends ListenerAdapter {
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            guild.getVoiceChannels().forEach(voiceChannel -> {
                if (voiceChannel.getMembers().contains(guild.getSelfMember())) {
                    guild.getAudioManager().closeAudioConnection();
                }
            });
        }
    }

    public void onDisconnect(DisconnectEvent event) {
        event.getJDA().getGuilds().forEach(guild -> guild.getAudioManager().closeAudioConnection());
    }
}