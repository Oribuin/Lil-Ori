package xyz.oribuin.lilori

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import xyz.oribuin.lilori.database.DatabaseConnector
import xyz.oribuin.lilori.database.SQLiteConnector
import xyz.oribuin.lilori.handler.CommandExecutor
import xyz.oribuin.lilori.handler.CommandHandler
import xyz.oribuin.lilori.handler.console.ConsoleCMDHandler
import xyz.oribuin.lilori.handler.console.ConsoleCmdExecutor
import xyz.oribuin.lilori.listener.GeneralEvents
import xyz.oribuin.lilori.listener.support.SupportListeners
import xyz.oribuin.lilori.manager.*
import xyz.oribuin.lilori.util.ConsoleColors
import xyz.oribuin.lilori.util.EventWaiter
import xyz.oribuin.lilori.util.FileUtils
import java.io.File
import javax.security.auth.login.LoginException
import kotlin.reflect.KClass


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
        val file = File("data", "lilori.db")
        FileUtils.createFile(file)
        connector = SQLiteConnector(file)


        // Setup Managers
        getManager(DataManager::class)
        getManager(BotManager::class)
        getManager(CommandHandler::class)
        getManager(ConsoleCMDHandler::class)
        getManager(EconomyManager::class)
        getManager(GuildSettingsManager::class)
        getManager(QuoteManager::class)
        getManager(TicketManager::class)

        this.managers.values.forEach { manager -> manager.enable() }

        // Register plugin commands
        this.getManager(CommandHandler::class).registerCommands()
        this.getManager(ConsoleCMDHandler::class).registerCommands()

        // Login Bot
        val jda = JDABuilder.createDefault(
                Settings.TOKEN,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGE_TYPING
        ).addEventListeners(CommandExecutor(this, getManager(CommandHandler::class)), GeneralEvents(this), SupportListeners(), eventWaiter, this)

        for (intent in GatewayIntent.values())
            jda.enableIntents(intent)

        this.jdabot = jda.build()

        // Get the BotManager
        val botManager = getManager(BotManager::class)

        // Register values specifically
        botManager.registerGuilds(jdabot!!)
        botManager.registerStatus(jdabot!!)

        // Startup Log
        this.logStartup(jdabot!!)

        // Register console commands
        ConsoleCmdExecutor(this, getManager(ConsoleCMDHandler::class))
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
            if (command.aliases == null)
                throw IllegalArgumentException(ConsoleColors.RED_BOLD_BRIGHT + "Command aliases does not exists")
            else
                println(ConsoleColors.BLUE_BRIGHT + "Loaded Command: (${command.category.type.categoryName}) ${command.name} | (${++i}/${getManager(CommandHandler::class).commands.size})" + ConsoleColors.RESET)

        println(ConsoleColors.GREEN_UNDERLINED + "*=* Loaded Up ${jdaBot.selfUser.name} with ${getManager(CommandHandler::class).commands.size}  Command(s) *=*" + ConsoleColors.RESET)
    }
}