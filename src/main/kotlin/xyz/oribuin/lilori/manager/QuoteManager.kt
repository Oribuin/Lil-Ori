package xyz.oribuin.lilori.manager

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.data.Quote
import java.sql.Connection
import java.util.*

class QuoteManager(bot: LilOri) : Manager(bot) {
    val quotes = mutableListOf<Quote>()

    override fun enable() {
        this.quotes.clear()
        this.registerQuotes()
    }

    override fun disable() {
        this.quotes.clear()
    }

    /**
     * Register all the quotes into the quote list for easier quote access.
     */
    private fun registerQuotes() {
        // Connect to database
        bot.connector.connect { connection ->
            val query = "SELECT * FROM quotes"

            // Get all the quotes from the database
            connection.prepareStatement(query).use { statement ->
                val result = statement.executeQuery()

                // Filter through each quote in the database
                while (result.next()) {
                    val quote = Quote(result.getInt("id"), result.getString("author"), result.getString("quote"))

                    // If the list does not contain the quote, add it
                    if (!quotes.contains(quote)) {
                        quotes.add(quote)
                    }

                    // Get the list of quotes in the database that match val quote
                    val count = quotes.stream().filter { t -> t == quote}.count()

                    // Remove any quotes that managed to slip through the first check every 0.25 seconds
                    val timer = Timer()
                    val timerTask: TimerTask = object : TimerTask() {
                        override fun run() {
                            while (count > 1) {
                                // Remove quote
                                quotes.stream().filter { t -> t == quote}.forEach { t -> quotes.remove(t) }
                            }
                        }
                    }

                    // Schedule task
                    timer.schedule(timerTask, 250)
                }
            }
        }
    }

    /**
     * Add or Change a quote inside the database
     *
     * @param author The quote sender
     * @param quote  The quote text
     */
    fun updateQuote(author: String, quote: String) {
        bot.connector.connect { connection: Connection ->
            val addQuote = "REPLACE INTO quotes (id, author, quote) VALUES (?, ?, ?)"
            connection.prepareStatement(addQuote).use { statement ->
                statement.setInt(1, quotes.size + 1)
                statement.setString(2, author)
                statement.setString(3, quote)
                statement.executeUpdate()
                quotes.add(Quote(quotes.size + 1, author, quote))
            }
        }
    }

    /**
     * Remove a quote from the database
     *
     * @param quote The quote being removed
     */
    fun removeQuote(quote: Quote) {
        bot.connector.connect { connection: Connection ->
            val removeQuote = "DELETE FROM quotes WHERE id = ? AND author = ? AND quote = ?"
            connection.prepareStatement(removeQuote).use { statement ->
                statement.setInt(1, quote.id)
                statement.setString(2, quote.author)
                statement.setString(3, quote.quote)
                statement.executeUpdate()
                if (quotes.contains(quote)) {
                    quotes.remove(quote)
                }
            }
        }
    }
}