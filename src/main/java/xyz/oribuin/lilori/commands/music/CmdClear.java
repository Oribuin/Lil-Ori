package xyz.oribuin.lilori.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import xyz.oribuin.lilori.managers.music.TrackManager;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

public class CmdClear extends Command {
    private boolean loop;

    public CmdClear() {
        this.name = "loop";
        this.help = "fuck.";
        this.category = new Command.Category("Music");
        this.arguments = "";
        this.ownerCommand = true;

        loop = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        TrackManager trackManager = new TrackManager();
        event.getMessage().delete().queue();

        loop = !loop;
        event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        trackManager.loop(event.getTextChannel(), loop);
    }
}
