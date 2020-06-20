package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.TrackManager;

import java.util.Collections;

public class CmdStop extends Command {

    public CmdStop() {
        this.name = "Stop";
        this.description = "Stops playing Music.";
        this.aliases = Collections.emptyList();
    }

    public void executeCommand(CommandEvent event) {
        TrackManager tm = TrackManager.getInstance(event.getGuild());

        if (!event.getGuild().getAudioManager().isConnected()) {
            event.reply(event.getAuthor() + ", There is no active Audio Track.");
            return;
        }

        tm.getMusicManager().getAudioManager(event.getGuild()).setSendingHandler(null);
        tm.getMusicManager().getAudioManager(event.getGuild()).closeAudioConnection();
        event.reply(event.getAuthor().getAsMention() + ", Ended the audio track. :wave:");
    }
}
