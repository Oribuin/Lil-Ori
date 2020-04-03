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

public class CmdPause extends Command {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagerMap;

    public CmdPause() {
        this.name = "Pause";
        this.help = "Pause the current track.";
        this.category = new Command.Category("Music");
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

        if (musicManager.player.isPaused()) {
            event.reply(event.getAuthor().getAsMention() + ", Track is no longer paused.");
            musicManager.player.setPaused(false);
            musicManager.player.destroy();
        } else {
            event.reply(event.getAuthor().getAsMention() + ", Track is now paused.");
            musicManager.player.setPaused(true);
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
