package xyz.oribuin.lilori.handler.console

import net.dv8tion.jda.api.JDA
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.utils.ConsoleColors

class ConsoleEvent(private val bot: LilOri, private val command: String) {
    fun print(any: Any) {
        println("${ConsoleColors.BLUE_BOLD_BRIGHT}[${ConsoleColors.RED}ConsoleCommand${ConsoleColors.BLUE_BOLD_BRIGHT}]${ConsoleColors.CYAN_BOLD_BRIGHT} $any")
    }

    val message: String
        get() = command

    val args = command.split(" ").toTypedArray()

    val jda: JDA?
        get() = bot.jdabot
}