package xyz.oribuin.lilori.manager.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.command.music.CmdLoop
import xyz.oribuin.lilori.handler.CommandHandler
import java.util.*

class TrackScheduler(private val bot: LilOri, private val player: AudioPlayer) : AudioEventAdapter() {
    private val queue: Queue<AudioTrack>
    fun queue(track: AudioTrack, override: Boolean) {
        if (!player.startTrack(track, !override)) {
            queue.offer(track)
        }
    }

    fun nextTrack() {
        player.startTrack(queue.poll(), true)
    }

    override fun onTrackEnd(player: AudioPlayer, audioTrack: AudioTrack, endReason: AudioTrackEndReason) {

        val command = bot.getManager(CommandHandler::class).getCommand("loop") as CmdLoop

        if (command.isLooping) {
            player.playTrack(audioTrack)
            return
        }

        if (endReason == AudioTrackEndReason.FINISHED)
            nextTrack()
    }

    init {
        player.volume = 75
        queue = LinkedList()
    }
}