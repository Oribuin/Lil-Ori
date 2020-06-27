package xyz.oribuin.lilori.database

import org.sqlite.SQLiteException
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class SQLiteConnector(directory: File) : DatabaseConnector {
    private val connectionString = "jdbc:sqlite:" + directory.absolutePath
    private var connection: Connection? = null

    init {
        Class.forName("org.sqlite.JDBC")
    }

    override fun closeConnection() {
        try {
            if (this.connection != null) {
                this.connection?.close()
            }
        } catch (ex: SQLiteException) {
            error("An error occured closing the SQLTE database connection: ${ex.message}")
        }
    }

    override fun connect(callback: (Connection) -> Unit) {
        if (this.connection == null) {
            try {
                this.connection = DriverManager.getConnection(this.connectionString)
            } catch (ex: SQLException) {
                error("An error occurred retrieving the SQLite database connection: " + ex.message)
            }
        }

        try {
            this.connection?.let { callback(it) }
        } catch (ex: Exception) {
            error("An error occurred executing an SQLite query: " + ex.message)
        }
    }
}