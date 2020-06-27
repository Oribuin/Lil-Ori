package xyz.oribuin.lilori.commands.global.music

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.managers.music.TrackManager.Companion.getInstance

class CmdStop : Command() {
    init {
        name = "Stop"
        description = "Stops playing Music."
        aliases = emptyList()
    }

    override fun executeCommand(event: CommandEvent?) {
        (event?: return)
        val tm = getInstance(event.guild)
        (tm?: return)

        if (!event.guild.audioManager.isConnected) {
            event.reply(event.author.toString() + ", There is no active Audio Track.")
            return
        }

        tm.musicManager.getAudioManager(event.guild).sendingHandler = null
        tm.musicManager.getAudioManager(event.guild).closeAudioConnection()

        event.reply(event.author.asMention + ", Ended the audio track. :wave:")
    }
}