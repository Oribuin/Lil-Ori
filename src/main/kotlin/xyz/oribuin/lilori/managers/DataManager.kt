package xyz.oribuin.lilori.managers

import net.dv8tion.jda.api.entities.Guild
import xyz.oribuin.lilori.LilOri
import java.sql.Connection

class DataManager(bot: LilOri?) : Manager(bot!!) {
    override fun enable() {
        createTables()
    }

    /**
     * Create all the SQLite Tables if they don't exist.
     */
    private fun createTables() {
        bot.connector?.connect { connection: Connection ->
            val queries = arrayOf(
                    "CREATE TABLE IF NOT EXISTS guild_settings (guild_id LONG, prefix TXT, PRIMARY KEY(guild_id))",
                    "CREATE TABLE IF NOT EXISTS quotes (label TXT, author TXT, quote TXT)")

            for (query in queries) {
                connection.prepareStatement(query).use { statement -> statement.executeUpdate() }
            }
        }
    }

    fun createGuild(guild: Guild, prefix: String?) {
        bot.connector?.connect { connection: Connection ->
            val createGuild = "INSERT INTO guild_settings (guild_id, prefix) VALUES (?, ?)"
            connection.prepareStatement(createGuild).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.setString(2, prefix)
                statement.executeUpdate()
            }
        }
    }

    fun removeGuild(guild: Guild) {
        bot.connector?.connect { connection: Connection ->
            val deleteGuild = "REMOVE FROM guild_settings WHERE guild_id = ?"
            connection.prepareStatement(deleteGuild).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.executeUpdate()
            }
        }
    }

    /**
     * Add or Change a quote inside the database
     *
     * @param label  The quote label
     * @param author The quote sender
     * @param quote  The quote text
     */
    fun updateQuote(label: String?, author: String?, quote: String?) {
        bot.connector?.connect { connection: Connection ->
            val addQuote = "REPLACE INTO quotes (label, author, quote) VALUES (?, ?, ?)"
            connection.prepareStatement(addQuote).use { statement ->
                statement.setString(1, label)
                statement.setString(2, author)
                statement.setString(3, quote)
                statement.executeUpdate()
            }
        }
    }

    /**
     * Remove a quote based on the label
     *
     * @param label the label being checked
     */
    fun removeQuote(label: String?) {
        bot.connector?.connect { connection: Connection ->
            val removeQuote = "DELETE FROM quotes WHERE label = ?"
            connection.prepareStatement(removeQuote).use { statement ->
                statement.setString(1, label)
                statement.executeUpdate()
            }
        }
    }
}