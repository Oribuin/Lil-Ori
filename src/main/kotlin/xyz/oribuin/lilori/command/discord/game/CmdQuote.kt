package xyz.oribuin.lilori.command.discord.game

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.manager.QuoteManager
import xyz.oribuin.lilori.util.BotUtils

class CmdQuote(bot: LilOri) : Command(bot) {

    init {
        name = "Quote"
        category = Category(Category.Type.GAMES)
        description = "Get a quote saved into lil ori"
        aliases = emptyList()
        arguments = listOf("get", "<id/author>")
    }

    override fun executeCommand(event: CommandEvent) {

        // Get the quote manager & quote list
        val quoteManager = bot.getManager(QuoteManager::class)
        val quotes = quoteManager.quotes

        // Check if they have the right amount of arguments
        if (event.args.size < 2) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${name.toLowerCase()} ${arguments?.let { BotUtils.formatList(it) }}")
            return
        }


        // Switch case the first argument
        when (event.args[1].toLowerCase()) {

            // If args[1] is add
            "add", "update" -> {
                // Check author id, If they are not Ori, Tell them they cannot do the command.
                if (event.author.id != Settings.OWNER_ID) {
                    event.sendEmbedReply("\uD83D\uDC94 Owner Only Sub Command.", "You cannot add a quote to the bot because you are not Ori.")
                    return
                }

                // Check if they have the right amount of arguments
                if (event.args.size >= 4) {
                    // Define quote author
                    val quoteAuthor = event.args[2].replace("_", " ")

                    // Define the quote
                    val quote = event.message.contentRaw.substring(event.args[0].length + event.args[1].length + event.args[2].length + 3)

                    // Add the quote into the database
                    bot.getManager(QuoteManager::class).updateQuote(quoteAuthor, quote)

                    // Send embed message telling them they have added the quote.
                    event.sendEmbedReply("\uD83D\uDC96 Added Quote", """You have added a quote to Lil' Ori's Memory!
                        
                        **»** Author: $quoteAuthor
                        
                        **»** Quote: $quote""".trimIndent())
                    return
                }

                // Send invalid arguments message if they haven't provided enough arguments
                event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${name.toLowerCase()} add <author> <quote>")
            }

            "remove" -> {
                // Check author id, If they are not Ori, Tell them they cannot do the command.
                if (event.author.id != Settings.OWNER_ID) {
                    event.sendEmbedReply("\uD83D\uDC94 Owner Only Sub Command.", "You cannot remove a quote to the bot because you are not Ori.")
                    return
                }

                // Check if they have the right amount of arguments
                if (event.args.size >= 3) {

                    // Get a quote based on their provided arguments
                    val quoteList = quotes.stream().filter { quote -> quote.id == event.args[2].toInt() || quote.author == java.lang.String.join(" ", *event.args).substring(event.args[0].length + event.args[1].length + 2) }.findFirst()

                    // Check if the quote they provided doesn't exist
                    if (quoteList.isEmpty) {
                        event.sendEmbedReply("\uD83D\uDC94 Invalid Quote", "I could not find that quote in my memory!.")
                        return
                    }

                    // Get the quote if it exists
                    val quote = quoteList.get()

                    // Send quote removed message
                    event.sendEmbedReply("\uD83D\uDC96 Removed Quote", """You have removed a quote to Lil' Ori's Memory!
                        
                        **»** Author: ${quote.author}
                        
                        **»** Quote: ${quote.quote}""".trimIndent())

                    // Remove Quote
                    quoteManager.removeQuote(quote)
                    return
                }

                // Send invalid arguments message if they haven't provided enough arguments
                event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${name.toLowerCase()} remove <id/author>")

            }

            "get" -> {
                if (event.args.size >= 3) {
                    // Get a quote based on their provided argument
                    val quoteList = quotes.stream().filter { quote -> quote.id == event.args[2].toInt() || quote.author == java.lang.String.join(" ", *event.args).substring(event.args[0].length + event.args[1].length + 2) }.findFirst()

                    // Check if the quote they provided doesn't exist
                    if (quoteList.isEmpty) {
                        event.sendEmbedReply("\uD83D\uDC94 Invalid Quote", "I could not find that quote in my memory!.")
                        return
                    }

                    // Get the quote provided
                    val quote = quoteList.get()

                    // Send quote message
                    event.sendEmbedReply("\uD83D\uDC96 Found the quote (ID: ${quote.id})", """**»** Author: ${quote.author}
                        
                        **»** Quote: ${quote.quote}""".trimIndent())
                    return
                }

                // Send invalid arguments message if they haven't provided enough arguments
                event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${name.toLowerCase()} get <id/author>")
            }

            // Send invalid arguments message if they haven't provided a correct sub command
            else -> event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${name.toLowerCase()} ${arguments?.let { BotUtils.formatList(it) }}")
        }
    }
}