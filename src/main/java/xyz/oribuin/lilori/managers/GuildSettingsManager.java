package xyz.oribuin.lilori.managers;

import net.dv8tion.jda.api.entities.Guild;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.utils.GuildSettings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class GuildSettingsManager extends Manager {

    private final Map<Long, GuildSettings> guildSettings;

    public GuildSettingsManager(LilOri bot) {
        super(bot);
        this.guildSettings = new HashMap<>();
    }

    @Override
    public void enable() {
        // Unused
    }

    /**
     * Load the guild settings for the guild
     *
     * @param guild The guild being loaded.
     */

    public void loadGuildSettings(Guild guild) {

        if (this.guildSettings.containsKey(guild.getIdLong()))
            return;

        this.bot.getConnector().connect(connection -> {
            GuildSettings settings = GuildSettings.getDefault();
            this.guildSettings.put(guild.getIdLong(), settings);

            String commandPrefix = "SELECT prefix FROM guild_settings WHERE guild_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(commandPrefix)) {
                statement.setLong(1, guild.getIdLong());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                    settings.setPrefix(resultSet.getString(1));
            }
        });
    }

    public void unloadGuildSettings(Guild guild) {
        this.guildSettings.remove(guild.getIdLong());
    }

    /**
     * Remove the Guild from guild_settings table
     *
     * @param guild the guild being deleted.
     */
    public void removeGuildSettings(Guild guild) {
        this.unloadGuildSettings(guild);

        String removeGuild = "DELETE FROM guild_settings WHERE guild_id = ?";
        this.bot.getConnector().connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(removeGuild)) {
                statement.setLong(1, guild.getIdLong());
            }
        });
    }

    /**
     * Get the guild settings if they exist, if not, load them
     *
     * @param guild the guild being loaded
     * @return Guild's Settings
     */
    public GuildSettings getGuildSettings(Guild guild) {
        if (!this.guildSettings.containsKey(guild.getIdLong()))
            this.loadGuildSettings(guild);

        return this.guildSettings.get(guild.getIdLong());
    }

    /**
     * Update the command prefix in a guild
     *
     * @param guild  represents the guild the command is sent in
     * @param prefix represents the new command prefix
     */

    public void updateGuild(Guild guild, String prefix) {
        this.getGuildSettings(guild).setPrefix(prefix);

        this.bot.getConnector().connect(connection -> {
            String updateSettings = "REPLACE INTO guild_settings (guild_id, prefix) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(updateSettings)) {
                statement.setLong(1, guild.getIdLong());
                statement.setString(2, prefix);
                statement.executeUpdate();
            }
        });
    }
}
