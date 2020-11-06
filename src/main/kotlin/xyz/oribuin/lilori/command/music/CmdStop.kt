package xyz.oribuin.lilori.command.music

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Cmd
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.music.TrackManager.Companion.getInstance

@Cmd(
        name = "Stop",
        description = "Stop the music entirely.",
        category = Category.Type.MUSIC,
        arguments = ["<Youtube-URL>"],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)

class CmdStop(bot: LilOri) : Command(bot) {
    override fun executeCommand(event: CommandEvent) {

        val tm = getInstance(event.guild)?: return

        if (!event.guild.audioManager.isConnected) {
            event.sendEmbedReply("❗ No Track", "There is currently no active audio track playing!")
            return
        }

        // Shutdown player manager
        tm.playerManager.shutdown()

        // Disconnect from voice channel
        tm.musicManager.getAudioManager(event.guild).sendingHandler = null
        tm.musicManager.getAudioManager(event.guild).closeAudioConnection()

        // Define the embed
        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83C\uDFB5 Stopping music")
                .setColor(event.color)
                .setDescription("Successfully stopped playing music, Leaving Voice Channel!")
                .setFooter("Created by Ori", "http://img.oribuin.xyz/profile.png")

        // Send embed to channel
        event.channel.sendMessage(event.author.asMention).embed(embedBuilder.build()).queue()
    }
}