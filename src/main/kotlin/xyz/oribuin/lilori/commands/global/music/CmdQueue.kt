package xyz.oribuin.lilori.commands.global.music

import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.managers.music.TrackManager.Companion.getInstance
import java.util.concurrent.TimeUnit

class CmdQueue(bot: LilOri) : Command(bot) {
    init {
        name = "Queue"
        description = "Queue music onto the playlist"
        aliases = emptyList()
        arguments = listOf("<youtube_url>")
        botPermissions = arrayOf(Permission.MESSAGE_MANAGE)
    }

    override fun executeCommand(event: CommandEvent) {
        val tm = getInstance(event.guild)
        val musicManager = (tm ?: return).musicManager
        val args = event.message.contentRaw.split(" ").toTypedArray()

        (event.member ?: return)

        if (event.member?.voiceState == null && event.member?.voiceState?.inVoiceChannel() == false) {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", You must be in a voice channel to execute this command.", 10, TimeUnit.SECONDS)
            return
        }

        if (args.size < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Please input a url for the music.", 10, TimeUnit.SECONDS)
            return
        }

        val url = event.message.contentRaw.substring(args[0].length + 1)

        if (musicManager.player.isPaused) {
            musicManager.player.isPaused = false
            event.reply(event.author.asMention + ", Playback has now been resumed.")
        }

        event.deleteCmd()
        tm.loadAndPlay(event.member!!, event.textChannel, url, false, true)

        musicManager.getAudioManager(event.guild).openAudioConnection(event.member?.voiceState?.channel)
        tm.trackScheduler.onTrackEnd(tm.musicManager.player, musicManager.player.playingTrack, AudioTrackEndReason.FINISHED)
    }
}