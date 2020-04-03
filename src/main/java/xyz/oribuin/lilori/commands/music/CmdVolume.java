package xyz.oribuin.lilori.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.oribuin.lilori.managers.GuildMusicManager;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CmdVolume extends Command {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagerMap;

    public CmdVolume() {
        this.name = "Volume";
        this.help = "Change the volume of the music.";
        this.category = new Command.Category("Music");
        this.arguments = "[Volume]";
        this.hidden = true;
        this.musicManagerMap = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(playerManager);
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    @Override
    protected void execute(CommandEvent event) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());

        String[] args = event.getMessage().getContentRaw().split( " ");

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please include the volume __number__.",10, TimeUnit.SECONDS);
            return;
        }

        try {
            int volume = Integer.parseInt(args[1]);
            musicManager.player.setVolume(volume);
            musicManager.player.playTrack(musicManager.player.getPlayingTrack());
            event.reply(event.getAuthor().getAsMention() + ", You have set the volume to " + volume);
        } catch (NumberFormatException e) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please include the volume __number__.", 10, TimeUnit.SECONDS);
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

    private void connectChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            audioManager.openAudioConnection(audioManager.getGuild().getSelfMember().getVoiceState().getChannel());
        }
    }
}
