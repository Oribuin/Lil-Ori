package xyz.oribuin.lilori.command.author

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.util.BotUtils
import java.sql.ResultSet
import kotlin.math.max

@CommandInfo(
        name = "Query",
        description = "Execute an SQL Statement into the database.",
        category = Category.Type.AUTHOR,
        arguments = ["<update/result>", "<statement>"],
        aliases = ["sql"],
        userPermissions = [],
        botPermissions = [],
        guildId = "",
        ownerOnly = true
)
class CmdQuery(bot: LilOri) : BotCommand(bot) {
    override fun executeCommand(event: CommandEvent) {

        // Check arguments
        if (event.args.size < 3) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        // Check if author included the right query type
        if (!event.args[1].equals("update", true) && !event.args[1].equals("result", false)) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        val query = event.message.contentRaw.substring(event.args[0].length + event.args[1].length + 2)

        // Connect to the database
        bot.connector.connect { connection ->

            // Get the query that is being executed
            try {
                // Execute query into the database
                connection.prepareStatement(query).use { statement ->
                    when (event.args[1].toLowerCase()) {

                        // If the second argument is "update", Execute an update to the database
                        "update" -> {
                            statement.executeUpdate()
                            event.reply("${event.author.asMention}, You have executed the query, \"$query\"")
                        }

                        // If the second argument is "result". Return the result of the query
                        "result" -> {
                            for (result in getResultStrings(statement.executeQuery())) {
                                event.reply("```$result```")
                            }
                        }

                        // If the second argument is not update or result, Send error message.
                        else -> {
                            event.sendEmbedReply("❗ Invalid Query Type", "Please replace ${event.args[1]} with either \"result\" or \"update\"")
                        }
                    }
                }

                // Catch exception and print it in chat
            } catch (ex: Exception) {
                event.sendEmbedReply("❗ An exception has occured", "Here is the exception message.\n \n${ex.message}")
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
            line.append(" |\n")
            addToMessage(line.toString())
        }

        addToMessage("\nTotal rows: $rowCount")

        messages.add(message.toString())

        return messages
    }
}