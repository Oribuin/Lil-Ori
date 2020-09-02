package xyz.oribuin.lilori.commands.music

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.managers.music.TrackManager.Companion.getInstance
import java.awt.Color

class CmdPause(bot: LilOri) : Command(bot) {
    init {
        name = "Pause"
        category = Category(Category.Type.MUSIC)
        aliases = emptyList()
        description = "Pause the music"
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        val tm = getInstance(event.guild)?: return

        // Check if the user is in a voice channel
        if (event.member.voiceState == null || event.member.voiceState?.inVoiceChannel() == false) {
            event.sendEmbedReply("❗ Unable to pause", "You cannot toggle the pausing because you are not in a voice channel!")
            return
        }

        // Define the player
        val player = tm.musicManager.player

        // Toggle pausing
        if (player.isPaused) {
            player.isPaused = false
            event.sendEmbedReply("\uD83C\uDFA7 Paused Music", "You have paused the music!")
        } else {
            player.isPaused = true
            event.sendEmbedReply("\uD83C\uDFA7 Unpaused Music", "You have unpaused the music!")
        }
    }
}