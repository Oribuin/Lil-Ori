package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;

public class CmdStop extends Command {
    public CmdStop() {
        this.name = "Stop";
        this.description = "Stops playing Music.";
        this.category = new Category("Music");
        this.arguments = "";
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        TrackManager trackManager = new TrackManager();
        GuildMusicManager musicManager = trackManager.getGuildAudioPlayer(event.getGuild());

        if (!event.getGuild().getAudioManager().isConnected()) {
            event.reply(event.getAuthor() + ", There is no active Audio Track.");
            return;
        }

        musicManager.player.stopTrack();
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reply(event.getAuthor().getAsMention() + ", Ended the audio track. :wave:");
    }
}
