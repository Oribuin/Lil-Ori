package xyz.oribuin.lilori.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import xyz.oribuin.lilori.commands.global.music.CmdLoop
import java.util.*

class TrackScheduler(private val player: AudioPlayer) : AudioEventAdapter() {
    private val queue: Queue<AudioTrack>
    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) {
            queue.offer(track)
        }
    }

    fun nextTrack() {
        player.startTrack(queue.poll(), true)
    }

    override fun onTrackEnd(player: AudioPlayer, audioTrack: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) if (CmdLoop().isLooping) {
            player.startTrack(audioTrack, false)
        } else {
            nextTrack()
        }
    }

    init {
        player.volume = 75
        queue = LinkedList()
    }
}