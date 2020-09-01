package xyz.oribuin.lilori.commands.global.games

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.sql.Connection

class CmdQuote(bot: LilOri) : Command(bot) {

    init {
        name = "Quote"
        category = Category(Category.Type.GAMES)
        description = "Quote command."
        aliases = emptyList()
        arguments = listOf("select", "<quote_id>")
    }


    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 2) {
            val embedBuilder = EmbedBuilder()
                    .setAuthor("Lil' Ori Quotes")
                    .setColor(Color.decode("#33539e"))
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("""To view a quote, type ${event.prefix}quote get <id>
                            Quote Id Amount: $quoteCount""".trimMargin())

            event.reply(embedBuilder)
            return
        }


        when (args[1].toLowerCase()) {
            "add" -> {
                if (event.author.id != Settings.OWNER_ID)
                    return

                if (args.size >= 5) {
                    val quoteId = args[2].toLowerCase()
                    val quoteAuthor = args[3].replace("_", " ")
                    val quote = event.message.contentRaw.substring(args[0].length + args[1].length + args[2].length + args[3].length + 4)

                    bot.dataManager.updateQuote(quoteId, quoteAuthor, quote)
                    event.reply(event.author.asMention + ", Added quote to the database!")
                    println("${event.author.asTag} Added a quote to the database." +
                            quoteId +
                            quoteAuthor +
                            quote)
                    return
                }

                event.reply("${event.author.asMention}, Correct usage ${event.prefix}quote add <quote_id> <quote_author> <quote>")
            }
            "select" -> {
                if (args.size == 3) {
                    bot.connector.connect { connection: Connection ->
                        val query = "SELECT quote FROM quotes WHERE label = ?"
                        connection.prepareStatement(query).use { statement ->
                            statement.setString(1, args[2])
                            val resultSet = statement.executeQuery()
                            if (resultSet.next()) event.reply(resultSet.getString(1))
                        }
                    }
                    return
                }

                event.reply("${event.author.asMention}, Correct usage: ${event.prefix}quote select <quote_id>")
            }
            "remove" -> {
                if (event.author.id != Settings.OWNER_ID)
                    return

                if (args.size == 3) {
                    bot.dataManager.removeQuote(args[2])
                    event.reply("${event.author.asMention}, Removed a quote from the database!")
                    println("${event.author.asTag} Removed a quote from the database." +
                            "Quote Id: ${args[2]}")
                    return
                }

                event.reply(event.author.asMention + ", Correct Usage: " + event.prefix + "quote remove <QUOTE_ID>")
            }
            "get" -> {
                if (args.size == 3) {
                    bot.connector.connect { connection: Connection ->
                        val query = "SELECT * FROM quotes WHERE label = ?"
                        connection.prepareStatement(query).use { getStatement ->
                            getStatement.setString(1, args[2])
                            val resultSet = getStatement.executeQuery()
                            if (resultSet.next()) {
                                val embedBuilder = EmbedBuilder()
                                        .setTitle("Lil' Ori Quotes (ID: ${resultSet.getString(1)})")
                                        .setColor(Color.decode("#33539e"))
                                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                                        .setDescription("""**Quote Author:** ${resultSet.getString(2)}
                                                **Quote:** ${resultSet.getString(3)}""".trimIndent())
                                event.reply(embedBuilder.build())
                            }
                        }
                    }
                    return
                }

                event.reply("${event.author.asMention}, Correct usage: ${event.prefix}quote get <quote_id>")
            }

            else -> event.reply("${event.author.asMention} Invalid Args.")
        }
    }

    private var quoteCount = 0
        get() {
            bot.connector.connect { connection: Connection ->
                val query = "SELECT COUNT(*) FROM quotes"
                connection.prepareStatement(query).use { statement ->
                    val result = statement.executeQuery()
                    result.next()
                    field = result.getInt(1)
                }
            }

            return field
        }
}