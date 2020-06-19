package xyz.oribuin.lilori.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;
import xyz.oribuin.lilori.managers.music.TrackScheduler;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CmdPlay extends Command {

    public CmdPlay() {
        this.name = "Play";
        this.description = "Play Music";
        this.aliases = Collections.emptyList();
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    public void executeCommand(CommandEvent event) {
        TrackManager tm = TrackManager.getInstance(event.getGuild());
        GuildMusicManager musicManager = tm.getMusicManager();

        String[] args = event.getMessage().getContentRaw().split(" ");

        if (event.getMember().getVoiceState() == null && !event.getMember().getVoiceState().inVoiceChannel()) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", You must be in a voice channel to execute this command.", 10, TimeUnit.SECONDS);
            return;
        }

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please input a url for the music.", 10, TimeUnit.SECONDS);
            return;
        }

        String url = event.getMessage().getContentRaw().substring(args[0].length() + 1);

        if (musicManager .player.isPaused()) {
            musicManager.player.setPaused(false);
            event.reply(event.getAuthor().getAsMention() + ", Playback has now been resumed.");
        }

        musicManager.getAudioManager(event.getGuild()).openAudioConnection(event.getMember().getVoiceState().getChannel());


         tm.loadAndPlay(event.getTextChannel(), url, false);
        AudioTrack track = musicManager.player.getPlayingTrack();

        long totalSeconds = track.getDuration() / 1000;
        totalSeconds %= 3600;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        event.reply(event.getAuthor().getAsMention() + ", Now playing " + url);

    }

}
