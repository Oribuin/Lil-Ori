package xyz.oribuin.lilori

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.oribuin.lilori.commands.author.*
import xyz.oribuin.lilori.commands.global.CmdColor
import xyz.oribuin.lilori.commands.global.CmdHelp
import xyz.oribuin.lilori.commands.global.CmdPing
import xyz.oribuin.lilori.commands.global.CmdPrefix
import xyz.oribuin.lilori.commands.global.administrative.CmdPerms
import xyz.oribuin.lilori.commands.global.games.*
import xyz.oribuin.lilori.commands.global.moderation.CmdPurge
import xyz.oribuin.lilori.commands.global.music.*
import xyz.oribuin.lilori.database.DatabaseConnector
import xyz.oribuin.lilori.database.SQLiteConnector
import xyz.oribuin.lilori.handler.CommandExecutor
import xyz.oribuin.lilori.handler.CommandHandler
import xyz.oribuin.lilori.listeners.GeneralEvents
import xyz.oribuin.lilori.listeners.support.SupportListener
import xyz.oribuin.lilori.managers.DataManager
import xyz.oribuin.lilori.managers.GuildSettingsManager
import java.io.File
import java.io.IOException
import javax.security.auth.login.LoginException

class LilOri private constructor() : ListenerAdapter() {

    // Define handlers
    val commandHandler: CommandHandler
    var connector: DatabaseConnector? = null

    // Define managers
    val dataManager: DataManager
    val guildSettingsManager: GuildSettingsManager

    // Define others
    // EventWaiter waiter = new EventWaiter()

    // Register all commands
    private fun registerCommands() {
        commandHandler.registerCommands(
                // General Commands
                CmdHelp(), CmdPing(), CmdPrefix(),
                // Music Commands
                CmdLoop(), CmdPause(), CmdPlay(), CmdStop(), CmdVolume(),
                // Game Commands
                CmdCoinflip(), CmdColor(), CmdEightball(), CmdFeed(), CmdGay(), CmdQuote(), CmdSlap(),
                // Moderation Commands
                CmdPurge(),
                // Author Commands
                CmdEval(), CmdQuery(), CmdShutdown(), CmdTest(), CmdUpdate(),
                // Admin Commands
                CmdPerms()
        )
    }


    private fun enable() {
        guildSettingsManager.enable()
        dataManager.enable()
    }

    companion object {
        @JvmStatic
        lateinit var instance: LilOri

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                LilOri()
            } catch (e: LoginException) {
                e.printStackTrace()
            }
        }

    }

    init {
        instance = this

        // Setup the SQLite Database
        val file = File("data", "lilori.db")
        try {
            if (!file.exists()) {
                file.createNewFile()
                println("Created SQLite Database File: lilori.db")
            }

            // Register SQLite Connector
            connector = SQLiteConnector(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Setup Managers
        guildSettingsManager = GuildSettingsManager(this)
        commandHandler = CommandHandler()
        dataManager = DataManager(this)

        this.registerCommands()
        this.enable()

        // Login Bot
        val jda = JDABuilder.createDefault(Settings.TOKEN)
                .addEventListeners(CommandExecutor(this, commandHandler), GeneralEvents(), SupportListener(), this)
                .build()

        println("*=* Loading Lil' Ori Commands *=*")

        var i = 0

        for (command in commandHandler.commands)
            if (command.aliases == null)
                throw NullPointerException("Command aliases is null")
            else
                println("Loaded Command: ${command.name} | (${++i}/${commandHandler.commands.size}) ")

        println("*=* Loaded Up ${jda.selfUser.name} with ${commandHandler.commands.size}  Command(s) *=*")
    }

}