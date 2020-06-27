package xyz.oribuin.lilori.utils

import xyz.oribuin.lilori.Settings

class GuildSettings(val guildIdLong: Long, private var prefix: String) {

    fun getPrefix(): String {
        return prefix.toLowerCase()
    }

    fun setPrefix(prefix: String) {
        this.prefix = prefix
    }

    companion object {
        @JvmStatic
        val default: GuildSettings
            get() = GuildSettings(0, Settings.DEFAULT_PREFIX)
    }

}