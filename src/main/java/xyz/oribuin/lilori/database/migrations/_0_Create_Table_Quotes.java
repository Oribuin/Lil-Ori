package xyz.oribuin.lilori.database.migrations;

import xyz.oribuin.lilori.database.DataMigration;
import xyz.oribuin.lilori.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _0_Create_Table_Quotes extends DataMigration {
    public _0_Create_Table_Quotes() {
        super(0);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection) throws SQLException {
        String query =
                "CREATE TABLE quote_table (" +
                        "    quote-id TEXT NOT NULL," +
                        "    quote-text TEXT NOT NULL," +
                        "    quote-author TEXT NOT NULL," +
                        "    PRIMARY KEY(quote-id, quote-text, quote-author)" +
                        ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }
}
