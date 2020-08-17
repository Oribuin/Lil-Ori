package xyz.oribuin.lilori

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import xyz.oribuin.lilori.commands.author.*
import xyz.oribuin.lilori.commands.global.CmdColor
import xyz.oribuin.lilori.commands.global.CmdHelp
import xyz.oribuin.lilori.commands.global.CmdPing
import xyz.oribuin.lilori.commands.global.CmdPrefix
import xyz.oribuin.lilori.commands.global.administrative.CmdPerms
import xyz.oribuin.lilori.commands.global.games.*
import xyz.oribuin.lilori.commands.global.moderation.CmdBan
import xyz.oribuin.lilori.commands.global.moderation.CmdPurge
import xyz.oribuin.lilori.commands.global.music.*
import xyz.oribuin.lilori.commands.support.general.CmdAnnounce
import xyz.oribuin.lilori.commands.support.general.CmdReactionRole
import xyz.oribuin.lilori.commands.support.ticket.CmdClose
import xyz.oribuin.lilori.commands.support.ticket.CmdTicket
import xyz.oribuin.lilori.database.DatabaseConnector
import xyz.oribuin.lilori.database.SQLiteConnector
import xyz.oribuin.lilori.handler.CommandExecutor
import xyz.oribuin.lilori.handler.CommandHandler
import xyz.oribuin.lilori.listeners.GeneralEvents
import xyz.oribuin.lilori.listeners.support.SupportListeners
import xyz.oribuin.lilori.managers.DataManager
import xyz.oribuin.lilori.managers.GuildSettingsManager
import xyz.oribuin.lilori.managers.TicketManager
import xyz.oribuin.lilori.utils.EventWaiter
import java.io.File
import java.io.IOException
import javax.security.auth.login.LoginException

class LilOri private constructor() : ListenerAdapter() {

    // Define handlers
    private val commandHandler: CommandHandler
    lateinit var connector: DatabaseConnector

    // Define managers
    val dataManager: DataManager
    val guildSettingsManager: GuildSettingsManager
    val ticketManager: TicketManager

    // Define others
    private val eventWaiter = EventWaiter()

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
                CmdPurge(), CmdBan(),
                // Author Commands
                CmdEval(), CmdQuery(), CmdTest(), CmdUpdate(),
                // Admin Commands
                CmdPerms(),
                // Support Discord commands
                // General
                CmdAnnounce(), CmdReactionRole(),
                // Ticket
                CmdTicket(), CmdClose(eventWaiter)
        )
    }


    private fun enable() {
        guildSettingsManager.enable()
        dataManager.enable()
        ticketManager.enable()
    }

    init {
        instance = this

        // PDM
        /*
        val classLoader = URLClassLoader(arrayOfNulls<URL>(0), javaClass.classLoader)
        val libraryDirectory = Files.createTempDirectory("PDM").toFile()

        val pdm = PDMBuilder()
                .rootDirectory(libraryDirectory)
                .classLoader(classLoader)
                .applicationName("PDM-Test-Suite")
                .applicationVersion("N/A")
                .build()

        pdm.loadAllDependencies().join()
         */

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
        ticketManager = TicketManager(this)

        this.registerCommands()
        this.enable()

        // Login Bot
        val jda = JDABuilder.createDefault(Settings.TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(CommandExecutor(this, commandHandler), GeneralEvents(this), SupportListeners(), eventWaiter, this)

        val jdaBot = jda.build()

        println("*=* Loading Lil' Ori Commands *=*")
        var i = 0

        for (command in commandHandler.commands)
            if (command.aliases == null)
                throw NullPointerException("Command aliases is null")
            else
                println("Loaded Command: ${command.name} | (${++i}/${commandHandler.commands.size}) ")

        println("*=* Loaded Up ${jdaBot.selfUser.name} with ${commandHandler.commands.size}  Command(s) *=*")
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
}