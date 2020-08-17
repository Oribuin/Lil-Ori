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

            val query = "REPLACE INTO guild_settings (guild_id, prefix) VALUES (?, ?"
            connection.prepareStatement(query).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.setString(2, settings.getPrefix())
                statement.executeUpdate()
            }
        }
    }

    fun loadGuildSettings(guild: Guild) {
        if (!guildSettings.containsKey(guild.idLong)) {
            this.createGuild(guild)
            println("Created Guild in Database: ${guild.name} (${guild.id})")
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

    /**
     * Get the guild settings if they exist, if not, load them
     *
     * @param guild the guild being loaded
     * @return Guild's Settings
     */
    fun getGuildSettings(guild: Guild): GuildSettings? {
        if (!guildSettings.containsKey(guild.idLong))
            loadGuildSettings(guild)

        return guildSettings[guild.idLong]
    }

    /**
     * Update the command prefix in a guild
     *
     * @param guild  represents the guild the command is sent in
     * @param prefix represents the new command prefix
     */
    fun updateGuild(guild: Guild, prefix: String?) {
        if (prefix != null) {
            getGuildSettings(guild)?.setPrefix(prefix)
        }

        bot.connector.connect { connection: Connection ->
            val updateSettings = "REPLACE INTO guild_settings (guild_id, prefix) VALUES (?, ?)"

            connection.prepareStatement(updateSettings).use { statement ->
                statement.setLong(1, guild.idLong)
                statement.setString(2, prefix)
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