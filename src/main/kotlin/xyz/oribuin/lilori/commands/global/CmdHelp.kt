package xyz.oribuin.lilori.commands.global

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

class CmdHelp : Command() {
    override fun executeCommand(event: CommandEvent?) {
        val args = event!!.message.contentRaw.split(" ").toTypedArray()
        val embedBuilder = EmbedBuilder()
                .setAuthor("Lil' Ori Help Menu")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
        if (args.size < 2) {
            embedBuilder.setDescription("""
    » Info - ${event.prefix}**help info** «
    » Music - ${event.prefix}**help music** «
    » Games - ${event.prefix}**help games** «
    » Moderation - ${event.prefix}**help moderation** «
    » Admin - ${event.prefix}**help admin**
    """.trimIndent()
            )
            event.reply(embedBuilder)
            return
        }
        when (args[1].toLowerCase()) {
            "info" -> embedBuilder.setDescription("""
    » Color Command - ${event.prefix}**color <#hex/rgb>**
    » Help Command - ${event.prefix}**help** «
    » Ping Command - ${event.prefix}**ping** «
    """.trimIndent())
            "music" -> embedBuilder.setDescription("""
    » Loop Command (WIP)- ${event.prefix}**loop** «
    » Pause Command - ${event.prefix}**pause** «
    » Play Command - ${event.prefix}**play <Youtube-URL>** «
    » Stop Command - ${event.prefix}**stop** «
    » Volume Command - ${event.prefix}**volume <Volume>** «
    """.trimIndent())
            "games" -> embedBuilder.setDescription("""
    » Coinflip Command - ${event.prefix}**coinflip** «
    » Eightball Command - ${event.prefix}**ball <question>** «
    » Feed Command - ${event.prefix}**feed** «
    » Gay Command - ${event.prefix}**gay** «
    » Quote Command - ${event.prefix}**quote** «
    » Slap Command - ${event.prefix}**slap <users>** «
    """.trimIndent())
            "moderation" -> embedBuilder.setDescription("""
    » Ban Command - ${event.prefix}**ban <@User> <Messages> <Reason>**
    » Kick Command - ${event.prefix}**<@User> <Reason>** «
    » Mute Command - ${event.prefix}**mute <Setup/@User>** «
    » Purge Command - ${event.prefix}**purge <Channel/Msgs/User> <#Channel/Number/@User>** «
    """.trimIndent())
            "admin" -> embedBuilder.setDescription("""
    » Prefix Command - ${event.prefix}**prefix <prefix>** «
    » Perm Command - ${event.prefix}**perms**
    """.trimIndent())
            else -> {
                event.reply(event.author.asMention + ", Please use the correct arguments.")
                return
            }
        }
        event.reply(embedBuilder)
    }

    init {
        name = "Help"
        aliases = listOf("Support")
        description = "Get the list of commands for the bot."
    }
}