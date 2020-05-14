package xyz.oribuin.lilori.managers;

import net.dv8tion.jda.api.entities.Guild;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.utils.GuildSettings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class GuildSettingsManager extends Manager {

    private Map<String, GuildSettings> guildSettings;

    public GuildSettingsManager(LilOri bot) {
        super(bot);
        this.guildSettings = new HashMap<>();
    }

    @Override
    public void enable() {
        // Unused
    }

    public void loadGuildSettings(Guild guild) {
        if (this.guildSettings.containsKey(guild.getId()))
            return;

        this.bot.getConnector().connect(connection -> {
            GuildSettings settings = GuildSettings.getDefault();

            this.guildSettings.put(guild.getId(), settings);

            String commandPrefix = "SELECT prefix FROM command_prefixes WHERE guild_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(commandPrefix)) {
                statement.setString(1, guild.getId());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                    settings.setPrefix(resultSet.getString(1));
            }
        });
    }

    public void unloadGuildSettings(Guild guild) {
        this.guildSettings.remove(guild.getId());
    }

    public GuildSettings getGuildSettings(Guild guild) {
        if (!this.guildSettings.containsKey(guild.getId()))
            this.loadGuildSettings(guild);

        return this.guildSettings.get(guild.getId());
    }

    public void updateCommandPrefix(Guild guild, String prefix) {
        this.getGuildSettings(guild).setPrefix(prefix);

        this.bot.getConnector().connect(connection -> {
            String update = "REPLACE INTO command_prefixes (guild_id, prefix) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(update)) {
                statement.setString(1, guild.getId());
                statement.setString(2, prefix);
                statement.executeUpdate();
            }
        });
    }
}
