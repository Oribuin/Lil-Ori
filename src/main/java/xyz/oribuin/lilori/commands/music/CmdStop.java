package xyz.oribuin.lilori.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;
import xyz.oribuin.lilori.managers.GuildMusicManager;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CmdStop extends Command {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagerMap;

    public CmdStop() {
        this.name = "Stop";
        this.help = "Stops playing Music.";
        this.category = new Category("Music");
        this.arguments = "[None]";
        this.hidden = true;

        this.musicManagerMap = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(playerManager);
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    @Override
    protected void execute(CommandEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());

        if (event.getMember().getVoiceState() != null && event.getMember().getVoiceState().inVoiceChannel()) {
            musicManager.player.stopTrack();
            musicManager.player.destroy();
            event.getGuild().getAudioManager().closeAudioConnection();

            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(":wave:", 10, TimeUnit.SECONDS);
        }
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagerMap.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(guild, playerManager);
            musicManagerMap.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }
}
