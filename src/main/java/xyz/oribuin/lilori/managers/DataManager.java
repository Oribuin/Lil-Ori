package xyz.oribuin.lilori.managers;

import net.dv8tion.jda.api.entities.Guild;
import xyz.oribuin.lilori.LilOri;

import java.sql.PreparedStatement;

public class DataManager extends Manager {

    public DataManager(LilOri bot) {
        super(bot);
    }

    @Override
    public void enable() {
        this.createTables();
    }

    /**
     * Create all the SQLite Tables if they don't exist.
     */

    private void createTables() {
        bot.getConnector().connect(connection -> {

            String[] queries = {
                    "CREATE TABLE IF NOT EXISTS guild_settings (guild_id LONG, prefix TXT)",
                    "CREATE TABLE IF NOT EXISTS quotes (label TXT, author TXT, quote TXT)",
            };

            for (String query : queries) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        });
    }


    public void createGuild(Guild guild, String prefix) {
        bot.getConnector().connect(connection -> {
            String createGuild = "INSERT INTO guild_settings (guild_id, prefix) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createGuild)) {
                statement.setLong(1, guild.getIdLong());
                statement.setString(2, prefix);
                statement.executeUpdate();
            }
        });
    }

    /**
     * Add or Change a quote inside the database
     *
     * @param label  The quote label
     * @param author The quote sender
     * @param quote  The quote text
     */
    public void updateQuote(String label, String author, String quote) {
        bot.getConnector().connect(connection -> {
            String addQuote = "REPLACE INTO quotes (label, author, quote) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(addQuote)) {
                statement.setString(1, label);
                statement.setString(2, author);
                statement.setString(3, quote);
                statement.executeUpdate();
            }
        });
    }

    /**
     * Remove a quote based on the label
     *
     * @param label the label being checked
     */
    public void removeQuote(String label) {
        bot.getConnector().connect(connection -> {
            String removeQuote = "DELETE FROM quotes WHERE label = ?";
            try (PreparedStatement statement = connection.prepareStatement(removeQuote)) {
                statement.setString(1, label);
                statement.executeUpdate();
            }
        });
    }

}
