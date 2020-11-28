package xyz.oribuin.lilori.command.author

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.Icon
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.util.BotUtils
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

@CommandInfo(
        name = "Test",
        description = "Testing general functions for Ori.",
        category = Category.Type.AUTHOR,
        arguments = [],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = "",
        ownerOnly = false
)
class CmdTest(bot: LilOri) : BotCommand(bot) {

    override fun executeCommand(event: CommandEvent) {

        if (event.args.size < 2) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        val msg = java.lang.String.join(" ", *event.args).substring(event.args[0].length + 1)
        event.deleteCmd()

        event.textChannel.createWebhook(event.author.name)
                .setAvatar(Icon.from(URL(event.author.avatarUrl ?: event.author.defaultAvatarUrl).openStream()))
                .queue {
                    val json = JsonObject()
                    json.addProperty("content", msg)

                    val connection = URL(it.url).openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true
                    connection.outputStream.use { out ->
                        out.write(json.toString().toByteArray(StandardCharsets.UTF_8))
                        out.flush()
                    }

                    connection.inputStream.close()
                    connection.disconnect()

                    it.delete().queueAfter(3, TimeUnit.SECONDS)
                }
    }
}
