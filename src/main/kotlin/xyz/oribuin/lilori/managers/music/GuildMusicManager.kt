package xyz.oribuin.lilori.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.managers.AudioManager

class GuildMusicManager(manager: AudioPlayerManager) {
    @JvmField
    val player: AudioPlayer = manager.createPlayer()
    val scheduler: TrackScheduler
    private val sendHandler: AudioPlayerSendHandler

    init {
        scheduler = TrackScheduler(player)
        sendHandler = AudioPlayerSendHandler(player)
        player.addListener(scheduler)
    }

    fun getSendHandler(): AudioPlayerSendHandler {
        return AudioPlayerSendHandler(player)
    }

    fun getAudioManager(guild: Guild): AudioManager {
        return guild.audioManager
    }

}