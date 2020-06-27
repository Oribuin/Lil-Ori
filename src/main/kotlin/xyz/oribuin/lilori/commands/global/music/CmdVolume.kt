package xyz.oribuin.lilori.commands.global.music

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.managers.music.TrackManager.Companion.getInstance

class CmdVolume : Command() {
    init {
        name = "Volume"
        aliases = emptyList()
        description = "Change the volume of the volume"
    }

    override fun executeCommand(event: CommandEvent?) {
        (event?: return)
        val tm = getInstance(event.guild)
        (tm?: return)

        val args = event.message.contentRaw.split(" ").toTypedArray()
        if (args.size < 2) {
            event.reply("**Current Volume: ${tm.musicManager.player.volume}**")
            return
        }
        if (event.member!!.voiceState == null || !event.member!!.voiceState?.inVoiceChannel()!!) {
            event.reply("${event.author.asMention}, Could not change volume since you are not in the voice channel")
            return
        }

        try {
            val volume = args[1].toInt()
            tm.musicManager.player.volume = volume

            event.reply(event.author.asMention + ", Successfully changed volume to " + volume)
        } catch (ex: NumberFormatException) {
            event.reply(event.author.asMention + ", Please include the correct arguments." + event.prefix + name + " <volume>")
        }
    }
}