package xyz.oribuin.lilori.commands.music;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;
import xyz.oribuin.lilori.managers.music.TrackScheduler;

import javax.sound.midi.Track;
import java.util.Collections;

public class CmdLoop extends Command {

    public CmdLoop() {
        this.name = "Loop";
        this.aliases = Collections.emptyList();
        this.description = "Toggle song looping on/off";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        TrackManager trackManager = new TrackManager();
        GuildMusicManager musicManager = trackManager.getGuildAudioPlayer(event.getGuild());
        TrackScheduler trackScheduler = new TrackScheduler(musicManager.player);

        if (event.getMember().getVoiceState() == null || !event.getMember().getVoiceState().inVoiceChannel()) {
            event.reply(event.getAuthor().getAsMention() + ", Could not loop since you are not in the voice channel");
            return;
        }

        if (trackScheduler.isLooping())
            event.reply("✅ No longer Looping.");
        else
            event.reply("✅ Now Looping.");

        trackScheduler.setLooping(!trackScheduler.isLooping());
    }
}
