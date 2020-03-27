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

public class CmdClear extends Command {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagerMap;

    public CmdClear() {
        this.name = "OhFuck";
        this.help = "fuck.";
        this.category = new Command.Category("Music");
        this.arguments = "[None]";
        this.ownerCommand = true;

        this.musicManagerMap = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(playerManager);
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    @Override
    protected void execute(CommandEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
        if (event.getGuild().getMember(event.getAuthor()) == null) return;

        if (event.getGuild().getMember(event.getAuthor()).getVoiceState() != null && event.getGuild().getMember(event.getAuthor()).getVoiceState().inVoiceChannel()) {
            musicManager.player.stopTrack();
            event.getGuild().getAudioManager().closeAudioConnection();

            event.reply(musicManagerMap.toString());
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply("huh", 10, TimeUnit.SECONDS);

        }
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagerMap.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagerMap.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }
}
