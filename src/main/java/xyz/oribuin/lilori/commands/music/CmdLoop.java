package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;
import xyz.oribuin.lilori.managers.music.TrackScheduler;

import java.util.Collections;

public class CmdLoop extends Command {

    public CmdLoop() {
        this.name = "Loop";
        this.aliases = Collections.emptyList();
        this.description = "Toggle song looping on/off";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        TrackManager tm = new TrackManager(event.getGuild());

        if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel()) {
            event.reply(event.getAuthor().getAsMention() + ", Could not loop since you are not in the voice channel");
            return;
        }

        if (tm.getTrackScheduler().isLooping())
            event.reply("✅ No longer Looping.");
        else
            event.reply("✅ Now Looping.");

        tm.getTrackScheduler().setLooping(!tm.getTrackScheduler().isLooping());
    }
}
