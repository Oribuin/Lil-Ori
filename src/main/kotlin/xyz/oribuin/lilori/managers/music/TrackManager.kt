package xyz.oribuin.lilori.managers.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import java.awt.Color
import java.util.*
import java.util.function.Consumer

class TrackManager private constructor(guild: Guild) {
    val playerManager: AudioPlayerManager
    private val musicManagers: MutableMap<String, GuildMusicManager>
    private val guild: Guild
    var trackList: List<AudioTrack> = ArrayList()
    val musicManager: GuildMusicManager
        get() {
            var musicManager = musicManagers[guild.id]
            if (musicManager == null) {
                musicManager = GuildMusicManager(playerManager)
                musicManagers[guild.id] = musicManager
            }
            musicManager.getAudioManager(guild).sendingHandler = musicManager.getSendHandler()
            return musicManager
        }

    fun loadAndPlay(author: Member, textChannel: TextChannel, trackUrl: String, addPlaylist: Boolean) {

        playerManager.loadItemOrdered(musicManager, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                // Message Here
                var totalSeconds = track.duration / 1000
                totalSeconds %= 3600
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                val embedBuilder = EmbedBuilder()
                        .setAuthor("\uD83C\uDFB5 Now Playing " + track.info.title)
                        .setColor(Color.RED)
                        .setDescription("""**Song URL** ${track.info.uri}
                                        **Song Duration** $minutes minutes & $seconds seconds""")
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                textChannel.sendMessage(author.asMention).embed(embedBuilder.build()).queue()
                musicManager.scheduler.queue(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                var firstTrack = playlist.selectedTrack
                val trackList = playlist.tracks
                if (firstTrack == null) {
                    firstTrack = trackList[0]
                }
                if (addPlaylist) {
                    textChannel.sendMessage(author.asMention + ", Adding " + playlist.tracks.size + " to the playlist").queue()
                    trackList.forEach(Consumer { track: AudioTrack? -> musicManager.scheduler.queue(track!!) })
                    return
                }
                musicManager.scheduler.queue(firstTrack)
            }

            override fun noMatches() {
                textChannel.sendMessage("Could not find $trackUrl").queue()
            }

            override fun loadFailed(exception: FriendlyException) {
                textChannel.sendMessage("Exception: " + exception.message).queue()
                exception.printStackTrace()
            }
        })
    }

    val playlist: AudioPlaylist
        get() = object : AudioPlaylist {
            override fun getName(): String {
                return guild.name.toLowerCase() + "_playlist"
            }

            override fun getTracks(): List<AudioTrack> {
                return trackList
            }

            override fun getSelectedTrack(): AudioTrack {
                return musicManager.player.playingTrack
            }

            override fun isSearchResult(): Boolean {
                return false
            }
        }

    val trackScheduler: TrackScheduler
        get() = TrackScheduler(musicManager.player)

    companion object {
        private var instance: TrackManager? = null
        @JvmStatic
        fun getInstance(guild: Guild): TrackManager? {
            if (instance == null) {
                instance = TrackManager(guild)
            }
            return instance
        }
    }

    init {
        playerManager = DefaultAudioPlayerManager()
        playerManager.registerSourceManager(YoutubeAudioSourceManager())
        playerManager.registerSourceManager(HttpAudioSourceManager())
        playerManager.registerSourceManager(LocalAudioSourceManager())
        this.guild = guild
        musicManagers = HashMap()
    }
}