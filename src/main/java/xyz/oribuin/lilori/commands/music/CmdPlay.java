package xyz.oribuin.lilori.commands.music;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;
import xyz.oribuin.lilori.managers.music.TrackManager;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CmdPlay extends Command {

    public CmdPlay() {
        this.name = "Play";
        this.description = "Play Music";
        this.aliases = Collections.emptyList();
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
    }

    public void executeCommand(CommandEvent event) {
        TrackManager trackManager = new TrackManager();
        String[] args = event.getMessage().getContentRaw().split(" ");


        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please input a url for the music.", 10, TimeUnit.SECONDS);
            return;
        }

        if (event.getMember().getVoiceState() == null && !event.getMember().getVoiceState().inVoiceChannel()) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", You must be in a voice channel to execute this command.", 10, TimeUnit.SECONDS);
            return;
        }

        String input = event.getMessage().getContentRaw().substring(args[0].length() + 1);

        event.getMessage().delete().queue();
        event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        trackManager.loadAndPlay((TextChannel) event.getChannel(), input);
    }
}
