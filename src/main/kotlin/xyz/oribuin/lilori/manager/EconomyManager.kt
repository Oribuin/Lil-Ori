package xyz.oribuin.lilori.manager

import net.dv8tion.jda.api.entities.User
import org.sqlite.SQLiteException
import xyz.oribuin.lilori.LilOri

class EconomyManager(bot: LilOri) : Manager(bot) {
    override fun enable() {
        //
    }

    fun updateBalance(user: User, balance: Int) {
        bot.connector.connect { connection ->
            val query = "REPLACE INTO balances (id, coins) VALUES (?, ?)"
            connection.prepareStatement(query).use { statement ->
                statement.setLong(1, user.idLong)
                statement.setInt(2, balance)
                statement.executeUpdate()
            }
        }
    }

    fun getBalance(user: User): Int {
        var balance = 0

        try {
            bot.connector.connect { connection ->
                val query = "SELECT coins FROM balances WHERE id = ?"
                connection.prepareStatement(query).use { statement ->
                    statement.setLong(1, user.idLong)
                    val result = statement.executeQuery()
                    if (result.next()) {
                        balance = result.getInt(1)
                    }
                }
            }
        } catch (ex: SQLiteException) {
            this.updateBalance(user, balance)
        }

        return balance
    }
}