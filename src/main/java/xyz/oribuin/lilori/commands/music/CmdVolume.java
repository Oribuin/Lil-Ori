package xyz.oribuin.lilori.commands.music;

import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;
import xyz.oribuin.lilori.managers.music.TrackScheduler;

import java.util.Collections;

public class CmdVolume extends Command {

    public CmdVolume() {
        this.name = "Volume";
        this.aliases = Collections.emptyList();
        this.description = "Change the volume of the volume";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        TrackManager tm = new TrackManager(event.getGuild());
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel()) {
            event.reply(event.getAuthor().getAsMention() + ", Could not change volume since you are not in the voice channel");
            return;
        }

        try {
            int volume = Integer.parseInt(args[1]);

            tm.getMusicManager().player.setVolume(volume);
            event.reply(event.getAuthor().getAsMention() + "Successfully changed volume to " + volume);
        } catch (NumberFormatException ex) {
            event.reply(event.getAuthor().getAsMention() + ", Please include the correct arguments." + event.getPrefix() + this.getName() + " <volume>");
        }
    }
}
