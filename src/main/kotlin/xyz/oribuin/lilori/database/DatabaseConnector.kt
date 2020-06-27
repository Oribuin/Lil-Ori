package xyz.oribuin.lilori.database

import java.sql.Connection
import java.sql.SQLException

interface DatabaseConnector {

    /**
     * Closes all connections to the database
     */

    fun closeConnection()

    /**
     * Executes a callback with a Connection passed and automatically closes it when finished
     */

    fun connect(callback: (Connection) -> Unit)
}