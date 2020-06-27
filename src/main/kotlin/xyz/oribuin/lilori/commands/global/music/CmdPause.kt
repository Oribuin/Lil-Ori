package xyz.oribuin.lilori.commands.global.music

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.managers.music.TrackManager.Companion.getInstance
import java.awt.Color

class CmdPause : Command() {
    init {
        name = "Pause"
        aliases = emptyList()
        description = "Pause the music"
    }

    override fun executeCommand(event: CommandEvent?) {
        (event?: return)

        val tm = getInstance(event.guild)

        if (event.member!!.voiceState == null || !event.member!!.voiceState!!.inVoiceChannel()) {
            event.reply(event.author.asMention + ", Could not change volume since you are not in the voice channel")
            return
        }

        val player = tm!!.musicManager.player
        val msg: String

        if (player.isPaused) {
            player.isPaused = false
            msg = "Playback is no longer paused"
        } else {
            player.isPaused = true
            msg = "Playback is now paused"
        }

        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83C\uDFB5 $msg")
                .setColor(Color.RED)
                .setDescription("Type " + event.prefix + "pause to pause/unpause!")
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")

        event.textChannel.sendMessage(event.author.asMention).embed(embedBuilder.build()).queue()
    }
}