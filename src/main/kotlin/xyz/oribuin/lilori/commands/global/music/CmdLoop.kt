package xyz.oribuin.lilori.commands.global.music

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdLoop(bot: LilOri) : Command(bot) {
    var isLooping = false
        set


    init {
        name = "Loop"
        category = Category(Category.Type.MUSIC)
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
            event.reply("✅ No Longer Looping. ")
            isLooping = false
        } else {
            event.reply("✅ Now Looping.")
            isLooping = true
        }
    }
}