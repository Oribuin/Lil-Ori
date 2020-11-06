package xyz.oribuin.lilori.command.music

import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.music.TrackManager.Companion.getInstance
import xyz.oribuin.lilori.util.BotUtils
import java.text.SimpleDateFormat
import java.util.*

@CommandInfo(
        name = "Play",
        description = "Play audio from a youtube video.",
        category = Category.Type.MUSIC,
        arguments = ["<Youtube-URL>"],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)
class CmdPlay(bot: LilOri) : BotCommand(bot) {

    override fun executeCommand(event: CommandEvent) {
        val tm = getInstance(event.guild)?: return
        val musicManager = tm.musicManager

        // Check if the author is in a voice channel
        if (event.member.voiceState == null && event.member.voiceState?.inVoiceChannel() == false) {
            event.sendEmbedReply("❗ Can't Play Song", "You cannot play music because you are not in a voice channel!")
            return
        }

        // Check if the author has provided the right amount of args
        if (event.args.size < 2) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        // Define the url
        val url = event.message.contentRaw.substring(event.args[0].length + 1)

        // If the music has been paused, unpause it
        if (musicManager.player.isPaused) {
            musicManager.player.isPaused = false
            event.sendEmbedReply("\uD83C\uDFA7 Unpaused Music", "You have unpaused the music!")
        }

        // Delete command
        event.deleteCmd()

        // Debugging
        println(SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis())))

        // Play the song
        tm.loadAndPlay(event.member, event.textChannel, url, false, false)

        // Add the bot to a voice channel
        musicManager.getAudioManager(event.guild).openAudioConnection(event.member.voiceState?.channel)

        // Await track end.
        tm.trackScheduler.onTrackEnd(tm.musicManager.player, musicManager.player.playingTrack, AudioTrackEndReason.FINISHED)
    }
}