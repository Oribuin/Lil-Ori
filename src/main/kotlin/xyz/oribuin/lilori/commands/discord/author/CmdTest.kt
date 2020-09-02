package xyz.oribuin.lilori.commands.discord.author

import net.dv8tion.jda.api.entities.Icon
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.io.File

class CmdTest(bot: LilOri) : Command(bot) {
    init {
        name = "Test"
        category = Category(Category.Type.AUTHOR)
        aliases = emptyList()
        description = "A test command."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        event.deleteCmd()

        /*
        // Steal emote method
        for (emote in event.guild.emotes) {

            val inputStream = URL(emote.imageUrl)
            val file = File("images/emotes/${emote.name}${emote.imageUrl.substring(emote.imageUrl.length - 4)}")
            if (file.exists())
                continue

            Files.copy(inputStream.openStream(), Paths.get(file.path))
            event.reply("Created emote ${emote.asMention}")
        }
         */
    }
}
