package xyz.oribuin.lilori

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import xyz.oribuin.lilori.commands.author.CmdEval
import xyz.oribuin.lilori.commands.author.CmdQuery
import xyz.oribuin.lilori.commands.author.CmdTest
import xyz.oribuin.lilori.commands.author.CmdUpdate
import xyz.oribuin.lilori.commands.global.CmdColor
import xyz.oribuin.lilori.commands.global.CmdHelp
import xyz.oribuin.lilori.commands.global.CmdPing
import xyz.oribuin.lilori.commands.global.administrative.CmdPrefix
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
import xyz.oribuin.lilori.handler.ConsoleCommandEvent
import xyz.oribuin.lilori.listeners.GeneralEvents
import xyz.oribuin.lilori.listeners.support.SupportListeners
import xyz.oribuin.lilori.managers.DataManager
import xyz.oribuin.lilori.managers.GuildSettingsManager
import xyz.oribuin.lilori.managers.TicketManager
import xyz.oribuin.lilori.utils.ConsoleColors
import xyz.oribuin.lilori.utils.EventWaiter
import xyz.oribuin.lilori.utils.FileUtils
import java.io.File
import java.util.*
import javax.security.auth.login.LoginException


class LilOri : ListenerAdapter() {

    // Define handlers
    var commandHandler: CommandHandler
    var connector: DatabaseConnector

    // Define managers
    var dataManager: DataManager
    var guildSettingsManager: GuildSettingsManager
    var ticketManager: TicketManager

    // Define others
    private val eventWaiter = EventWaiter()

    // Register all commands
    private fun registerCommands() {
        commandHandler.registerCommands(
                // General Commands
                CmdHelp(this, eventWaiter), CmdPing(this), CmdPrefix(this),
                // Music Commands
                CmdLoop(this), CmdPause(this), CmdPlay(this), CmdQueue(this), CmdStop(this), CmdVolume(this),
                // Game Commands
                CmdCoinflip(this), CmdColor(this), CmdEightball(this), CmdFeed(this), CmdGay(this), CmdQuote(this), CmdSlap(this),
                // Moderation Commands
                CmdPurge(this, eventWaiter), CmdBan(this),
                // Author Commands
                CmdEval(this), CmdQuery(this), CmdTest(this), CmdUpdate(this),
                // Admin Commands
                CmdPerms(this),
                // Support Discord commands
                // General
                CmdAnnounce(this), CmdReactionRole(this),
                // Ticket
                CmdTicket(this), CmdClose(eventWaiter, this)
        )
    }

    private fun enable() {
        dataManager.enable()
        guildSettingsManager.enable()
        ticketManager.enable()
    }

    init {
        instance = this

        // Setup the SQLite Database
        val file = File("data", "lilori.db")
        FileUtils.createFile(file)
        connector = SQLiteConnector(file)

        // Setup Managers
        dataManager = DataManager(this)
        guildSettingsManager = GuildSettingsManager(this)
        commandHandler = CommandHandler()
        ticketManager = TicketManager(this)

        this.registerCommands()
        this.enable()

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
        ).addEventListeners(CommandExecutor(this, commandHandler), GeneralEvents(this), SupportListeners(), eventWaiter, this)

        for (intent in GatewayIntent.values())
            jda.enableIntents(intent)

        val jdaBot = jda.build()

        this.registerStatus(jdaBot)

        println(ConsoleColors.BLUE_BOLD_BRIGHT + "*=* Loading Lil' Ori Commands *=*" + ConsoleColors.RESET)
        var i = 0

        for (command in commandHandler.commands)
            if (command.aliases == null)
                throw IllegalArgumentException(ConsoleColors.RED_BOLD_BRIGHT + "Command aliases does not exists")
            else
                println(ConsoleColors.BLUE_BRIGHT + "Loaded Command: (${command.category.type.categoryName}) ${command.name} | (${++i}/${commandHandler.commands.size})" + ConsoleColors.RESET)

        println(ConsoleColors.GREEN_UNDERLINED + "*=* Loaded Up ${jdaBot.selfUser.name} with ${commandHandler.commands.size}  Command(s) *=*" + ConsoleColors.RESET)
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

    private fun registerStatus(jda: JDA) {
        jda.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)

        val activities = listOf(
                Activity.watching("https://oribuin.xyz/"),
                Activity.watching("#BlackLivesMatter"),
                Activity.watching("https://jars.oribuin.xyz/"),
                Activity.watching("#JusticeForBreonnaTaylor"),
                Activity.watching("https://oribuin.xyz/support"),
                Activity.watching("#BLM"),
                Activity.watching("https://oribuin.xyz/donate")
        )

        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                val randomAnswer = Random().nextInt(activities.size)
                jda.presence.activity = activities[randomAnswer]
            }
        }
        timer.schedule(timerTask, 0, 20000)
    }
}