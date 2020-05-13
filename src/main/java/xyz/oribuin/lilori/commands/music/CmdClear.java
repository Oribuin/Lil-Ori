package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.TrackManager;

public class CmdClear extends Command {
    private boolean loop;

    public CmdClear() {
        this.name = "loop";
        this.description = "fuck.";
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
