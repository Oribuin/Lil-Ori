package xyz.oribuin.lilori.database.migrations;

import xyz.oribuin.lilori.database.DataMigration;
import xyz.oribuin.lilori.database.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _1_Create_Table_Cookies extends DataMigration {
    public _1_Create_Table_Cookies() {
        super(1);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection) throws SQLException {
        String query =
                "CREATE TABLE cookie_table (" +
                        "    cookie-amount INTEGER NOT NULL," +
                        "    PRIMARY KEY(cookie-amount)" +
                        ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }
}