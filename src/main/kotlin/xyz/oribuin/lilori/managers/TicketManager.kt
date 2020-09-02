package xyz.oribuin.lilori.managers

import net.dv8tion.jda.api.entities.User
import xyz.oribuin.lilori.LilOri

class TicketManager(bot: LilOri) : Manager(bot) {
    private var ticketCount = 0

    override fun enable() {
        // Unused
    }


    fun updateUser(user: User, ticketAmount: Int) {
        this.bot.connector.connect { connection ->
            val updateUser = "REPLACE INTO ticket_count (user_id, count) VALUES (?, ?)"
            connection.prepareStatement(updateUser).use { statement ->
                statement.setLong(1, user.id.toLong())
                statement.setInt(2, ticketAmount)
                statement.executeUpdate()
            }
        }
    }

    fun resetUser(user: User) {
        this.bot.connector.connect { connection ->
            val updateUser = "REPLACE INTO ticket_count (user_id, count) VALUES (?, ?)"
            connection.prepareStatement(updateUser).use { statement ->
                statement.setLong(1, user.id.toLong())
                statement.setInt(2, 0)
                statement.executeUpdate()
            }
        }
    }

    fun getTicketCount(user: User): Int {
        this.bot.connector.connect { connection ->
            val getTicketCount = "SELECT count FROM ticket_count WHERE user_id = ?"
            connection.prepareStatement(getTicketCount).use { statement ->
                statement.setLong(1, user.id.toLong())
                val result = statement.executeQuery()
                if (result.next())
                    ticketCount = result.getInt(1)
            }
        }

        return ticketCount
    }
}