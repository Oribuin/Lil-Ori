package xyz.oribuin.lilori.command.discord.administrative

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.GuildSettingsManager
import xyz.oribuin.lilori.util.BotUtils

class CmdPrefix(bot: LilOri) : Command(bot) {
    init {
        name = "Prefix"
        category = Category(Category.Type.ADMIN)
        description = "Change the bot permission"
        aliases = emptyList()
        arguments = listOf("<prefix>")
        userPermissions = arrayOf(Permission.ADMINISTRATOR)
    }

    override fun executeCommand(event: CommandEvent) {

        if (event.args.size == 1) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${name.toLowerCase()} ${arguments?.let { BotUtils.formatList(it) }}")
            return
        }

        val embedBuilder = EmbedBuilder()
                .setTitle("❗ Changed Server Prefix")
                .setColor(event.color)
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("""**Old Prefix** ${event.prefix}
                        
                        **New Prefix** ${event.args[1]}""".trimMargin())

        event.reply(embedBuilder)
        bot.getManager(GuildSettingsManager::class).updateGuild(event.guild, event.args[1], event.color)
        println(event.author.asTag + " Updated \"" + event.guild.name + "\" Prefix to " + event.args[1])
    }
}