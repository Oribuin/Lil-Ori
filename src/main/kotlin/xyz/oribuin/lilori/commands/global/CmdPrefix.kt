package xyz.oribuin.lilori.commands.global

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

class CmdPrefix(bot: LilOri) : Command(bot) {
    init {
        name = "Prefix"
        description = "Change the bot permission"
        aliases = emptyList()
        arguments = listOf("<prefix>")
        userPermissions = arrayOf(Permission.ADMINISTRATOR)
    }

    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size == 1) {
            event.reply("${event.author.asMention}, Please provide the correct args!");
            return
        }

        if (args[0].equals(event.prefix, ignoreCase = true)) {
            event.reply(event.author.asMention + ", That prefix is already set.")
            return
        }


        if (event.prefix.length >= 2) {
            event.reply(event.author.asMention + ", Due to current odd issues, you cannot have a prefix longer than 1 character")
            return
        }

        val embedBuilder = EmbedBuilder()
                .setTitle("Changed Lil' Ori Prefix")
                .setColor(event.color)
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**Old Prefix** ${event.prefix}" +
                        " " +
                        "**New Prefix** ${args[1]}".trimIndent())

        event.reply(embedBuilder)
        bot.guildSettingsManager.updateGuild(event.guild, args[1])
        println(event.author.asTag + " Updated \"" + event.guild.name + "\" Prefix to " + args[1])
    }
}