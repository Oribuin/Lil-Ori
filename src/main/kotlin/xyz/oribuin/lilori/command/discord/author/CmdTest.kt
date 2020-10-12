package xyz.oribuin.lilori.command.discord.author

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

class CmdTest(bot: LilOri) : Command(bot) {
    init {
        name = "Test"
        category = Category(Category.Type.GENERAL)
        aliases = emptyList()
        description = "A test command."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        /*
        if (event.args.size < 2) {
            event.reply("Invalid arguments.")
            return
        }

        val content = java.lang.String.join(" ", *event.args).substring(event.args[0].length)

        event.textChannel.createWebhook("Example Webhook").queue { webhook ->

            val json = JsonObject()
            json.addProperty("content", content)

            val embedJson = JsonObject()
            json.addProperty("title", "Title")
            json.addProperty("description", "Description")

            val embedArray = JsonArray()
            embedArray.add(embedJson)
            json.add("embeds", embedArray)

            val url = URL(webhook.url)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("User-Agent", "Lil' Ori Test")
            connection.doOutput = true
            connection.outputStream.use { out ->
                out.write(json.toString().toByteArray(StandardCharsets.UTF_8))
                out.flush()
            }
            connection.inputStream.close()
            connection.disconnect()
        }
         */
    }


}
