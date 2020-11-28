package xyz.oribuin.lilori.command.music

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.manager.music.TrackManager

@CommandInfo(
        name = "Loop",
        description = "Set the bot to loop",
        category = Category.Type.MUSIC,
        arguments = [""],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)

class CmdLoop(bot: LilOri) : BotCommand(bot) {

    var isLooping = false

    override fun executeCommand(event: CommandEvent) {
        val tm = TrackManager.getInstance(event.guild) ?: return

        if (event.author.id != Settings.OWNER_ID) {
            event.sendEmbedReply("❗ Can't run this command", "You cannot run this command.")
            return
        }

        if (event.member.voiceState == null || !event.member.voiceState?.inVoiceChannel()!!) {
            event.sendEmbedReply("❗ Can't set looping!", "You cannot use this command if you are not in a voice channel!")
            return
        }

        isLooping = !isLooping
        event.sendEmbedReply("❗ Changed Looping State", "You have set song looping to **__${formatBoolean(isLooping)}__**")
    }

    private fun formatBoolean(boolean: Boolean): String {
        return if (boolean)
            "enabled"
        else
            "disabled"
    }
}