package xyz.oribuin.lilori.listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.Settings;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GeneralEvents extends ListenerAdapter {
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            LilOri.getInstance().getGuildSettingsManager().loadGuildSettings(guild);
        }

        Activity[] activities = {
                Activity.watching("#BlackLivesMatter"),
                Activity.watching("#BLM"),
                Activity.watching("#JusticeForGeorgeFloyd"),
                Activity.watching("#JusticeForBreonnaTaylor")
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
        LilOri.getInstance().getDataManager().createGuild(event.getGuild(), Settings.DEFAULT_PREFIX);
        LilOri.getInstance().getGuildSettingsManager().loadGuildSettings(event.getGuild());
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        LilOri.getInstance().getDataManager().removeGuild(event.getGuild());
        LilOri.getInstance().getGuildSettingsManager().removeGuildSettings(event.getGuild());
    }
}