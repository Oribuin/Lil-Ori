package xyz.oribuin.lilori.managers

import net.dv8tion.jda.api.entities.Guild
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.utils.GuildSettings
import xyz.oribuin.lilori.utils.GuildSettings.Companion.default
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

    /**
     * Load the guild settings for the guild
     *
     * @param guild The guild being loaded.
     */

    fun createGuild(guild: Guild) {
        bot.connector.connect { connection ->
            val settings = default
            guildSettings[guild.idLong] = settings

            val hex: String = String.format("%02x%02x%02x", settings.getColor().red, settings.getColor().green, settings.getColor().blue)

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
            val commandPrefix = "SELECT prefix FROM guild_settings WHERE guild_id = ?"

            connection.prepareStatement(commandPrefix).use { statement ->
                statement.setLong(1, guild.idLong)
                val resultSet = statement.executeQuery()
                if (resultSet.next())
                    settings.setPrefix(resultSet.getString(1))
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


    fun updateGuild(guild: Guild, prefix: String?) {
        if (prefix != null) {
            GuildSettings(guild).setPrefix(prefix)
        }

        bot.connector.connect { connection: Connection ->
            val updateSettings = "REPLACE INTO guild_settings (guild_id, guild_name, prefix) VALUES (?, ?, ?)"

            connection.prepareStatement(updateSettings).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.setString(2, guild.name)
                statement.setString(3, prefix)
                statement.executeUpdate()
            }
        }
    }

    fun removeGuild(guild: Guild) {
        bot.connector.connect { connection: Connection ->
            val deleteGuild = "REMOVE FROM guild_settings WHERE guild_id = ?"
            connection.prepareStatement(deleteGuild).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.executeUpdate()
            }
        }
    }
}