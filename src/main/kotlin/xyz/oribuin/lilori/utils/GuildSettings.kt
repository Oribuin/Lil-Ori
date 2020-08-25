package xyz.oribuin.lilori.utils

import net.dv8tion.jda.api.entities.Guild
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import java.awt.Color

class GuildSettings(var guild: Guild?) {

    fun getPrefix(): String {
        var prefix = Settings.DEFAULT_PREFIX

        LilOri.instance.connector.connect { connection ->
            val getPrefix = "SELECT prefix FROM guild_settings WHERE guild_id = ?"
            connection.prepareStatement(getPrefix).use { statement ->
                guild?.idLong?.let { statement.setLong(1, it) }
                val result = statement.executeQuery()
                if (result.next()) {
                    prefix = result.getString(1)
                }
            }
        }

        return prefix
    }

    fun setPrefix(prefix: String) {
        LilOri.instance.connector.connect { connection ->
            val getPrefix = "REPLACE INTO guild_settings (guild_id, prefix) VALUES (?, ?)"
            connection.prepareStatement(getPrefix).use { statement ->
                guild?.idLong?.let { statement.setLong(1, it) }
                statement.setString(2, prefix)

                statement.executeUpdate()
            }
        }
    }

    fun getColor(): Color {
        var color = Settings.EMBED_COLOR

        LilOri.instance.connector.connect { connection ->
            val getColor = "SELECT color FROM guild_settings WHERE guild_id = ?"
            connection.prepareStatement(getColor).use { statement ->
                guild?.idLong?.let { statement.setLong(1, it) }

                val result = statement.executeQuery()
                if (result.next()) {
                    color = Color.decode(result.getString(1))
                }
            }
        }

        return color
    }

    fun setColor(color: String) {
        LilOri.instance.connector.connect { connection ->
            val getPrefix = "REPLACE INTO guild_settings (guild_id, color) VALUES (?, ?)"
            connection.prepareStatement(getPrefix).use { statement ->
                guild?.idLong?.let { statement.setLong(1, it) }
                statement.setString(2, color)

                statement.executeUpdate()
            }
        }
    }

    companion object {
        @JvmStatic
        val default: GuildSettings
            get() = GuildSettings(guild = null)
    }
}