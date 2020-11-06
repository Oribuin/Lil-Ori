package xyz.oribuin.lilori.command.music

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Cmd
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.music.TrackManager.Companion.getInstance
import xyz.oribuin.lilori.util.BotUtils

@Cmd(
        name = "Volume",
        description = "Change the bot volume.",
        category = Category.Type.MUSIC,
        arguments = ["[volume]"],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)

class CmdVolume(bot: LilOri) : Command(bot) {

    override fun executeCommand(event: CommandEvent) {
        val tm = getInstance(event.guild)?: return

        if (event.args.size < 2) {
            event.sendEmbedReply("\uD83C\uDFA7 Current Volume", "The volume for the bot is set at ${tm.musicManager.player.volume}")
            return
        }


        if (event.member.voiceState == null || !event.member.voiceState?.inVoiceChannel()!!) {
            event.sendEmbedReply("❗ Can't change volume!", "You cannot change the volume because you are not in a voice channel!")
            return
        }

        try {
            val volume = event.args[1].toInt()

            if (volume > 200 || volume < 0) {
                event.sendEmbedReply("❗ Invalid Volume", "The volume can only be between 0 and 200.")
                return
            }

            tm.musicManager.player.volume = volume

            event.sendEmbedReply("\uD83C\uDFA7 Changed Volume", "You have set the music volume to $volume/200!")
        } catch (ex: NumberFormatException) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
        }
    }
}