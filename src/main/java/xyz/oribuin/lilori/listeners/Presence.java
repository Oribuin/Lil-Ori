package xyz.oribuin.lilori.listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.LilOri;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Presence extends ListenerAdapter {
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            LilOri.getInstance().getGuildSettingsManager().loadGuildSettings(guild);
        }

        Activity[] activities = {
                Activity.watching("Ori Code"),
                Activity.watching("My RAM nervously"),
                Activity.watching("Ori fail at MySQL"),
                Activity.watching("My Database")
        };


        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int randomAnswer = new Random().nextInt(activities.length);
                event.getJDA().getPresence().setActivity(activities[randomAnswer]);
            }
        };

        timer.schedule(timerTask, 0, 10000);
    }

    public void onDisconnect(DisconnectEvent event) {
        event.getJDA().getGuilds().forEach(guild -> guild.getAudioManager().closeAudioConnection());
    }

    public void onGuildJoin(GuildJoinEvent event) {
        LilOri.getInstance().getGuildSettingsManager().loadGuildSettings(event.getGuild());
    }

    public void onEmoteAdded(EmoteAddedEvent event) {
        if (!event.getGuild().getId().equals("676064472720212028")) return;

        Emote emote = event.getEmote();
        emote.getManager().setName("emc_" + emote.getName()).queue();
    }
}