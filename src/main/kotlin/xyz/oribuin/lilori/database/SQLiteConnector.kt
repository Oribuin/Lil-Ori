package xyz.oribuin.lilori.database

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class SQLiteConnector(directory: File) : DatabaseConnector {
    private val connectionString = "jdbc:sqlite:" + directory.absolutePath
    private var connection: Connection? = null

    init {
        try {
            Class.forName("org.sqlite.JDBC")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun closeConnection() {
        try {
            if (connection != null) {
                connection?.close()
            }
        } catch (ex: SQLException) {
            println("An error occurred closing the SQLite database connection: " + ex.message)
        }
    }

    override fun connect(callback: (Connection) -> Unit) {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(connectionString)
            } catch (ex: SQLException) {
                println("An error occurred retrieving the SQLite database connection: " + ex.message)
            }
        }
        try {
            this.connection?.let { callback(it) }
        } catch (ex: Exception) {
            println("An error occurred executing an SQLite query: " + ex.message)
            ex.printStackTrace()
        }
    }
}