package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

import java.util.concurrent.TimeUnit;

public class CmdVolume extends Command {
    public CmdVolume() {
        this.name = "Volume";
        this.help = "Change the volume of the music.";
        this.category = new Command.Category("Music");
        this.arguments = "<Volume>";
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        TrackManager trackManager = new TrackManager();
        GuildMusicManager musicManager = trackManager.getGuildAudioPlayer(event.getGuild());

        String[] args = event.getMessage().getContentRaw().split( " ");

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please include the volume __number__.",10, TimeUnit.SECONDS);
            return;
        }

        try {
            int volume = Integer.parseInt(args[1]);
            musicManager.player.setVolume(volume);
            event.reply(event.getAuthor().getAsMention() + ", You have set the volume to " + volume);
        } catch (NumberFormatException e) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please include the volume __number__.", 10, TimeUnit.SECONDS);
        }
    }
}
