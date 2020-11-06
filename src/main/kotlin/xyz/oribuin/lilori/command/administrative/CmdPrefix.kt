package xyz.oribuin.lilori.command.administrative

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.GuildSettingsManager
import xyz.oribuin.lilori.util.BotUtils

@CommandInfo(
        name = "Prefix",
        description = "Change the command prefix for the bot.",
        category = Category.Type.ADMIN,
        arguments = ["<prefix>"],
        aliases = [],
        userPermissions = [Permission.ADMINISTRATOR],
        botPermissions = [],
        guildId = ""
)

class CmdPrefix(bot: LilOri) : BotCommand(bot) {

    override fun executeCommand(event: CommandEvent) {

        if (event.args.size == 1) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
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