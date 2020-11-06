package xyz.oribuin.lilori.manager

import net.dv8tion.jda.api.entities.Guild
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.data.GuildSettings
import xyz.oribuin.lilori.data.GuildSettings.Companion.default
import java.awt.Color
import java.sql.Connection
import java.util.*

class GuildSettingsManager(bot: LilOri) : Manager(bot) {
    private val guildSettings: MutableMap<Long, GuildSettings?>

    init {
        guildSettings = HashMap()
    }

    override fun enable() {
        // Unused
    }

    override fun disable() {
        guildSettings.clear()
    }

    /**
     * Load the guild settings for the guild
     *
     * @param guild The guild being loaded.
     */

    fun createGuild(guild: Guild) {
        bot.connector.connect { connection ->
            val settings = default
            guildSettings[guild.idLong] = settings

            val hex: String = String.format("#%02x%02x%02x", settings.getColor().red, settings.getColor().green, settings.getColor().blue)

            val query = "REPLACE INTO guild_settings (guild_id, guild_name, prefix, color) VALUES (?, ?, ?, ?)"
            connection.prepareStatement(query).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.setString(2, guild.name)
                statement.setString(3, settings.getPrefix())
                statement.setString(4, hex)
                statement.executeUpdate()
            }
        }
    }

    fun loadGuildSettings(guild: Guild) {
        if (!guildSettings.containsKey(guild.idLong)) {
            this.createGuild(guild)
        }

        bot.connector.connect { connection: Connection ->
            val settings = default
            guildSettings[guild.idLong] = settings
            val commandPrefix = "SELECT * FROM guild_settings WHERE guild_id = ?"

            connection.prepareStatement(commandPrefix).use { statement ->
                statement.setLong(1, guild.idLong)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    settings.setPrefix(resultSet.getString("prefix"))
                    settings.setColor(resultSet.getString("color"))
                }
            }
        }
    }

    private fun unloadGuildSettings(guild: Guild) {
        guildSettings.remove(guild.idLong)
    }

    /**
     * Remove the Guild from guild_settings table
     *
     * @param guild the guild being deleted.
     */
    fun removeGuildSettings(guild: Guild) {
        this.unloadGuildSettings(guild)
        val removeGuild = "DELETE FROM guild_settings WHERE guild_id = ?"
        bot.connector.connect { connection ->
            connection.prepareStatement(removeGuild).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.executeUpdate()
            }
        }
    }


    fun updateGuild(guild: Guild, prefix: String, color: Color) {
        bot.connector.connect { connection: Connection ->
            val updateSettings = "REPLACE INTO guild_settings (guild_id, guild_name, prefix, color) VALUES (?, ?, ?, ?)"
            val hex = String.format("#%02x%02x%02x", color.red, color.green, color.blue)

            connection.prepareStatement(updateSettings).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.setString(2, guild.name)
                statement.setString(3, prefix)
                statement.setString(4, hex)
                statement.executeUpdate()
            }
        }
    }

    fun removeGuild(guild: Guild) {
        bot.connector.connect { connection: Connection ->
            val deleteGuild = "DELETE FROM guild_settings WHERE guild_id = ?"
            connection.prepareStatement(deleteGuild).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.executeUpdate()
            }
        }
    }
}