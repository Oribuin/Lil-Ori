package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.TrackManager;

import java.util.Collections;

public class CmdLoop extends Command {

    private boolean looping;

    public CmdLoop() {
        this.name = "Loop";
        this.aliases = Collections.emptyList();
        this.description = "Toggle song looping on/off";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel()) {
            event.reply(event.getAuthor().getAsMention() + ", Could not loop since you are not in the voice channel");
            return;
        }

        if (this.isLooping()) {
            event.reply("Now ");
            this.setLooping(false);
        } else {
            event.reply("✅ Now Looping.");
            this.setLooping(true);
        }
    }

    private void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean isLooping() {
        return looping;
    }
}
