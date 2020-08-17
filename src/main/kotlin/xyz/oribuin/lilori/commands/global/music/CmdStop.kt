package xyz.oribuin.lilori.commands.global.music

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.managers.music.TrackManager.Companion.getInstance
import java.awt.Color

class CmdStop : Command() {
    init {
        name = "Stop"
        description = "Stops playing Music."
        aliases = emptyList()
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {

        val tm = getInstance(event.guild)
        (tm?: return)

        if (!event.guild.audioManager.isConnected) {
            event.reply(event.author.toString() + ", There is no active Audio Track.")
            return
        }

        tm.playerManager.shutdown()
        tm.musicManager.getAudioManager(event.guild).sendingHandler = null
        tm.musicManager.getAudioManager(event.guild).closeAudioConnection()
        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83C\uDFB5 Stopping music")
                .setColor(Color.RED)
                .setDescription("Successfully stopped playing music, Leaving Voice Channel!")
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")

        event.channel.sendMessage(event.author.asMention).embed(embedBuilder.build()).queue()
    }
}