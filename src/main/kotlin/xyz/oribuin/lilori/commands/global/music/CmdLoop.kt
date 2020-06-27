package xyz.oribuin.lilori.commands.global.music

import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdLoop : Command() {
    var isLooping = false
        private set


    init {
        name = "Loop"
        aliases = emptyList()
        description = "Toggle song looping on/off"
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        if (event.member?.voiceState == null || event.member?.voiceState?.inVoiceChannel() == false) {
            event.reply(event.author.asMention + ", Could not loop since you are not in the voice channel")
            return
        }

        if (isLooping) {
            event.reply("Now ")
            isLooping = false
        } else {
            event.reply("✅ Now Looping.")
            isLooping = true
        }
    }
}