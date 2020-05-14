package xyz.oribuin.lilori.utils;

import xyz.oribuin.lilori.Settings;

public class GuildSettings {

    private String guildId;
    private String prefix;

    public GuildSettings(String guildId, String prefix) {
        this.guildId = guildId;
        this.prefix = prefix;
    }

    public static GuildSettings getDefault() {
        return new GuildSettings(null, Settings.DEFAULT_PREFIX);
    }

    public String getGuildId() {
        return guildId;
    }

    public String getPrefix() {
        return prefix.toLowerCase();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
