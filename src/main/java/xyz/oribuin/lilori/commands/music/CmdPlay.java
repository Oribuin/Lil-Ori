package xyz.oribuin.lilori.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import xyz.oribuin.lilori.managers.GuildMusicManager;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CmdPlay extends Command {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagerMap;
    private CommandEvent commandEvent;

    public CmdPlay() {
        this.name = "Play";
        this.help = "Play Music";
        this.category = new Category("Music");
        this.arguments = "[URL]";
        this.hidden = true;

        this.musicManagerMap = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(playerManager);
        AudioSourceManagers.registerRemoteSources(playerManager);
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

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");


        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please input a url for the music.", 10, TimeUnit.SECONDS);
            return;
        }

        if (event.getGuildMember(event.getAuthor()).getVoiceState() == null && !event.getGuildMember(event.getAuthor()).getVoiceState().inVoiceChannel()) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", You must be in a voice channel to execute this command.", 10, TimeUnit.SECONDS);
            return;
        }

        String input = event.getMessage().getContentRaw().substring(args[0].length() + 1);

        if (musicManagerMap.size() >= 2)
            skip(event.getTextChannel());


        event.getGuild().getAudioManager().openAudioConnection(event.getGuildMember(event.getAuthor()).getVoiceState().getChannel());
        loadAndPlay(event.getTextChannel(), input);
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());


        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                long totalSeconds = track.getDuration() / 1000;
                totalSeconds %= 3600;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                EmbedBuilder playEmbed = new EmbedBuilder()
                        .setAuthor("« Lil' Ori Music »")
                        .setColor(Color.decode("#cca8db"))
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("Now Playing: " + track.getInfo().title + " (" + minutes + ":" + seconds + ")\n" +
                                "Url: " + track.getInfo().uri);

                channel.sendMessage(playEmbed.build()).queue();
                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null)
                    firstTrack = playlist.getTracks().get(0);

                long totalSeconds = firstTrack.getDuration() / 1000;
                totalSeconds %= 3600;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                channel.sendMessage(commandEvent.getAuthor().getAsMention() + ", Added " + firstTrack.getInfo().title + " (" + minutes + ":" + seconds + ") to the playlist.").queue();
                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Cannot Find " + trackUrl).queue();
            }


            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Couldn't Play " + exception.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectChannel(guild.getAudioManager());
        musicManager.player.playTrack(track);
    }

    private void skip(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("**Ori Music** ~ Skipped Song").queue();
    }

    private static void connectChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            audioManager.openAudioConnection(audioManager.getGuild().getSelfMember().getVoiceState().getChannel());
        }
    }
}
