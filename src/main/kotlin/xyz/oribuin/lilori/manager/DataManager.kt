package xyz.oribuin.lilori.manager

import xyz.oribuin.lilori.LilOri
import java.sql.Connection

class DataManager(bot: LilOri) : Manager(bot) {
    override fun enable() {
        createTables()
    }

    /**
     * Create all the SQLite Tables if they don't exist.
     */
    private fun createTables() {
        bot.connector.connect { connection: Connection ->
            val queries = arrayOf(
                    "CREATE TABLE IF NOT EXISTS guild_settings (guild_id LONG, guild_name TXT, prefix TXT, color TXT, PRIMARY KEY(guild_id))",
                    "CREATE TABLE IF NOT EXISTS quotes (id INT, author TXT, quote TXT)",
                    // Support tables
                    "CREATE TABLE IF NOT EXISTS ticket_count (user_id LONG, count INT, PRIMARY KEY(user_id))"
            )

            for (query in queries) {
                connection.prepareStatement(query).use { statement -> statement.executeUpdate() }
            }
        }
    }
}