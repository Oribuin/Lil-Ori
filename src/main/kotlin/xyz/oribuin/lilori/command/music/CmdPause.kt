package xyz.oribuin.lilori.command.music

import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Cmd
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.music.TrackManager.Companion.getInstance

@Cmd(
        name = "Pause",
        description = "Purge the music from playing.",
        category = Category.Type.MUSIC,
        arguments = [],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)
class CmdPause(bot: LilOri) : Command(bot) {
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