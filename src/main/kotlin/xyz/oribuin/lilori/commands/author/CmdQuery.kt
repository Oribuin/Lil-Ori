package xyz.oribuin.lilori.commands.author

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.lang.Exception
import java.sql.Connection
import java.sql.ResultSet
import kotlin.math.max

class CmdQuery(bot: LilOri) : Command(bot) {
    init {
        name = "Query"
        category = Category(Category.Type.AUTHOR)
        aliases = emptyList()
        description = "Query a command in SQLite."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 3) {
            event.reply("${event.author.asMention}, Please provide valid arguments, ${event.prefix}query <update/result> <query>")
            return
        }

        bot.connector.connect { connection: Connection ->

            val query = event.message.contentRaw.substring(args[0].length + args[1].length + 2)
            try {
                connection.prepareStatement(query).use { statement ->
                    if (args[1] == "update") {
                        statement.executeUpdate()
                        event.reply("${event.author.asMention}, You have executed the query, \"$query\"")
                    } else if (args[1].toLowerCase() == "result") {
                        for (result in getResultStrings(statement.executeQuery())) {
                            event.reply("```$result```")
                        }
                    } else {
                        event.reply("${event.author.asMention}, Invalid input type.")
                    }
                }
            } catch (ex: Exception) {
                event.reply("${event.author.asMention}, Exception Occurred: ${ex.message}")
            }
        }
    }

    /**
     * @author Esophose
     */
    private fun getResultStrings(result: ResultSet): List<String> {
        val rsmd = result.metaData
        val columnCount = rsmd.columnCount
        var rowCount = 0
        val messages = mutableListOf<String>()
        val message = StringBuilder()

        fun addToMessage(text: String) {
            val escapedText = text.replace("`", "\\`")
            if (message.length + escapedText.length > 2000 - 10) {
                messages.add(message.toString())
                message.clear()
            }
            message.append(escapedText)
        }

        // Create a list of row values
        // Add the column labels
        val rowValues = mutableListOf<MutableList<String>>()
        for (i in 0 until columnCount)
            rowValues.add(mutableListOf(rsmd.getColumnName(i + 1)))

        // Add data values
        while (result.next()) {
            for (i in 0 until columnCount) {
                var value = result.getString(i + 1).replace("\n", "")
                if (value.length >= 50)
                    value = value.substring(0..50) + "..."
                rowValues[i].add(value)
            }
            rowCount++
        }

        if (rowCount == 0)
            return mutableListOf("\nNo rows were found matching your query")

        // Create a list of max column length strings for padding
        val columnLengthPaddings = mutableListOf<String>()

        // Add max column lengths
        for (i in 0 until columnCount) {
            var length = 0
            for (value in rowValues[i])
                length = max(length, value.length)
            val sb = StringBuilder()
            for (k in 0 until length)
                sb.append(' ')
            columnLengthPaddings.add(sb.toString())
        }

        // Insert column header separators
        for (i in 0 until columnCount)
            rowValues[i].add(1, columnLengthPaddings[i].replace(' ', '-'))

        // TODO: First value should be centered

        // Build rows
        for (i in 0 until rowCount + 2) { // +2 for column headers
            val line = StringBuilder("| ")
            for (n in 0 until columnCount) {
                if (n > 0)
                    line.append(" | ")

                val rowValue = rowValues[n][i]
                line.append(rowValue).append(columnLengthPaddings[n].substring(rowValue.length))
            }
            line.append(" |\n");
            addToMessage(line.toString())
        }

        addToMessage("\nTotal rows: $rowCount")

        messages.add(message.toString())

        return messages
    }
}