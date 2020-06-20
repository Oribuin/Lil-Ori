package xyz.oribuin.lilori.utils;

import xyz.oribuin.lilori.Settings;

public class GuildSettings {

    private final long guildId;
    private String prefix;

    public GuildSettings(long guildId, String prefix) {
        this.guildId = guildId;
        this.prefix = prefix;
    }

    public static GuildSettings getDefault() {
        return new GuildSettings(0, Settings.DEFAULT_PREFIX);
    }

    public long getGuildIdLong() {
        return guildId;
    }

    public String getPrefix() {
        return prefix.toLowerCase();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
