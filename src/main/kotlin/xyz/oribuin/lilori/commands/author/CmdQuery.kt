package xyz.oribuin.lilori.commands.author

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.sql.Connection

class CmdQuery : Command() {
    init {
        name = "Query"
        aliases = emptyList()
        description = "Query a command in SQLite."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 2) {
            event.reply("${event.author.asMention}, Please include the correct arguments.")
            return
        }

        LilOri.instance.connector?.connect { connection: Connection ->

            val query = event.message.contentRaw.substring(args[0].length + 1)
            connection.prepareStatement(query).use { getStatement ->
                getStatement.executeUpdate()

                val embedBuilder = EmbedBuilder()
                        .setAuthor("Executed SQL Query")
                        .setColor(Color.decode("#33539e"))
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("""**Database:** (SQLite) lilori.db\n**Query: **$query""".trimMargin())

                event.channel.sendMessage(event.author.asMention).embed(embedBuilder.build())

                println("Executed Query: $query")
            }
        }
    }
}