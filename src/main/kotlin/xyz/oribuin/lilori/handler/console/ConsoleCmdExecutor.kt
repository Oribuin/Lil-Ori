package xyz.oribuin.lilori.handler.console

import xyz.oribuin.lilori.LilOri
import java.io.BufferedReader
import java.io.InputStreamReader

// damn long class name
class ConsoleCmdExecutor(private val bot: LilOri, private val commandHandler: ConsoleCMDHandler) {

    init {
        this.executeCommand()
    }

    fun executeCommand() {
        val reader = BufferedReader(InputStreamReader(System.`in`))
        val command = reader.readLine()

        if (command.startsWith("/")) {
            for (cmd in commandHandler.commands) {
                // Define the arguments
                val args = command.split(" ").toTypedArray()

                // Check if command name or alias
                if (!cmd.name.equals(args[0].substring(1).toLowerCase(), true))
                    continue

                // Execute Command
                cmd.executeCommand(ConsoleEvent(bot, java.lang.String.join(" ", *args).substring(1)))
            }
        }
    }
}