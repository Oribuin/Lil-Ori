package xyz.oribuin.lilori.command.discord.author

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

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

        event.deleteCmd()

        val embedColor: Color = try {
            Color.decode("#FF0000")
        } catch (ex: NumberFormatException) {
            Color.WHITE
        }
        val embedColorRgb = embedColor.rgb and 0xFFFFFF // Strips alpha channel from the Color#decode

        val json = JsonObject()

        val embedJson = JsonObject()
        embedJson.addProperty("title", "Ori's Report Module")
        embedJson.addProperty("description", "Welcome to Ori's report module, A player has submitted a report ingame, find information about the report here.\n\nReport module forked from [EternalReports](https://github.com/Oribuin/EternalReports)")
        embedJson.addProperty("color", embedColorRgb)

        /*
        val footerJson = JsonObject()
        footerJson.addProperty("text", "ArsenticReports by Oribuin")

        val fieldArray = JsonArray()
        fieldArray.add("name")

        embedJson.add("fields", fieldArray)

         */

        // Add "embed": {}
        json.add("embed", embedJson)


        val url = URL("https://discordapp.com/api/webhooks/765321095372734484/grFq_QcGOVG4jdNjjRABC7Q-cjVUYB2j52B8DGSIoDiBNafsY1K18SRtXa4gZftW90P2")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"

        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("User-Agent", "Lil' Ori Test")
        connection.doOutput = true

        connection.outputStream.use { out ->
            out.write(json.toString().toByteArray(StandardCharsets.UTF_8))
            println(json.toString())
            out.flush()
        }

        connection.inputStream.close()
        connection.disconnect()
    }


}
