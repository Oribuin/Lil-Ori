package xyz.oribuin.lilori

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import org.apache.commons.lang3.StringUtils
import xyz.oribuin.lilori.database.DatabaseConnector
import xyz.oribuin.lilori.database.SQLiteConnector
import xyz.oribuin.lilori.handler.CommandExecutor
import xyz.oribuin.lilori.handler.CommandHandler
import xyz.oribuin.lilori.listener.GeneralEvents
import xyz.oribuin.lilori.listener.support.SupportListeners
import xyz.oribuin.lilori.manager.*
import xyz.oribuin.lilori.util.ConsoleColors
import xyz.oribuin.lilori.util.EventWaiter
import xyz.oribuin.lilori.util.FileUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.security.auth.login.LoginException
import kotlin.reflect.KClass
import kotlin.system.exitProcess


class LilOri : ListenerAdapter() {
    private val managers = mutableMapOf<KClass<out Manager>, Manager>()
    var jdabot: JDA? = null

    // Define handlers
    var connector: DatabaseConnector

    // Define others
    val eventWaiter = EventWaiter()

    init {
        instance = this
        // Setup the SQLite Database
        connector = SQLiteConnector(FileUtils.createFile("data", "lilori.db"))


        // Setup Managers
        getManager(DataManager::class)
        getManager(BotManager::class)
        getManager(CommandHandler::class)
        getManager(EconomyManager::class)
        getManager(GuildSettingsManager::class)
        getManager(QuoteManager::class)
        getManager(TicketManager::class)

        this.managers.values.forEach { manager -> manager.enable() }

        // Register plugin commands
        this.getManager(CommandHandler::class).registerCommands()

        // Login Bot
        val jda = JDABuilder.create(Settings.TOKEN, GatewayIntent.values().toList())
                .addEventListeners(CommandExecutor(this, getManager(CommandHandler::class)), GeneralEvents(this), SupportListeners(), eventWaiter, this)
                .enableIntents(GatewayIntent.values().toList())

        this.jdabot = jda.build()

        // Get the BotManager
        val botManager = getManager(BotManager::class)

        // Register values specifically
        botManager.registerGuilds(jdabot!!)
        botManager.registerStatus(jdabot!!)

        // Startup Log
        this.logStartup(jdabot!!)

        val cmd = BufferedReader(InputStreamReader(System.`in`)).readLine()
        if (cmd == "shutdown") {
            shutdown()
        }
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

    fun <M : Manager> getManager(managerClass: KClass<M>): M {
        synchronized(this.managers) {
            @Suppress("UNCHECKED_CAST")
            if (this.managers.containsKey(managerClass))
                return this.managers[managerClass] as M

            return try {
                val manager = managerClass.constructors.first().call(this)
                manager.enable()
                this.managers[managerClass] = manager
                manager
            } catch (ex: ReflectiveOperationException) {
                error("Failed to load manager for ${managerClass.simpleName}")
            }
        }
    }

    private fun logStartup(jdaBot: JDA) {

        // Startup Command Log
        println(ConsoleColors.BLUE_BOLD_BRIGHT + "*=* Loading Lil' Ori Commands *=*" + ConsoleColors.RESET)
        var i = 0

        // Add every command into the console with a number
        for (command in getManager(CommandHandler::class).commands)
            println(ConsoleColors.BLUE_BRIGHT + "Loaded Command: (${command.getAnnotation(command.javaClass).category.categoryName}) ${StringUtils.capitalize(command.getAnnotation(command.javaClass).name)} | (${++i}/${getManager(CommandHandler::class).commands.size})" + ConsoleColors.RESET)

        println(ConsoleColors.GREEN_UNDERLINED + "*=* Loaded Up ${jdaBot.selfUser.name} with ${getManager(CommandHandler::class).commands.size}  Command(s) *=*" + ConsoleColors.RESET)
    }

    private fun shutdown() {
        println("Disabling all the managers.")
        this.managers.values.forEach { manager -> manager.disable() }
        println("Lil' Ori by Ori now shutting down.")
        exitProcess(0)
    }
}