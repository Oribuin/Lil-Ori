package xyz.oribuin.lilori.commands.global

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.utils.EventWaiter

class CmdHelp(bot: LilOri, private val waiter: EventWaiter) : Command(bot) {
    init {
        name = "Help"
        category = Category(Category.Type.GENERAL)
        aliases = listOf("Commands")
        description = "Get the list of commands for the bot."
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 2)
            return

        this.paginate(event, bot.commandHandler.commands, args[1].toInt())
    }

    private fun paginate(event: CommandEvent, list: List<Command>, page: Int) {

        val contentLinesPerPage = 2
        var totalPageCount = 1

        if ((list.size % contentLinesPerPage) == 0) {
            if (list.isNotEmpty()) {
                totalPageCount = list.size / contentLinesPerPage
            }
        } else {
            totalPageCount = (list.size / contentLinesPerPage) + 1
        }

        if (page <= totalPageCount) {
            if (list.isEmpty()) {
                println("You do not have any friends, Type /friend add <username> to make some!")
            } else {
                var i = 0
                var k = 0
                println("----------------[ Page: $page/$totalPageCount ]----------------")
                println("[LilOri] ➜ Command List List ")
                for (entry in list) {
                    k++
                    if (page * contentLinesPerPage + i + 1 == k && k != page * contentLinesPerPage + contentLinesPerPage + 1)
                        i++

                    println("(${entry.category.type.categoryName}) ${event.prefix}${entry.name} - ${entry.description}")
                }
                println("----------------[ Page: $page/$totalPageCount ]----------------")
            }
        } else {
           println("There are only $totalPageCount pages available!")
        }
    }
}