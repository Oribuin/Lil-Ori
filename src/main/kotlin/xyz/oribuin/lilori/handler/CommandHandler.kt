package xyz.oribuin.lilori.handler

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.command.administrative.CmdPerms
import xyz.oribuin.lilori.command.administrative.CmdPrefix
import xyz.oribuin.lilori.command.author.CmdQuery
import xyz.oribuin.lilori.command.author.CmdTest
import xyz.oribuin.lilori.command.economy.CmdBalance
import xyz.oribuin.lilori.command.economy.CmdFeed
import xyz.oribuin.lilori.command.game.CmdCoinflip
import xyz.oribuin.lilori.command.game.CmdEightball
import xyz.oribuin.lilori.command.game.CmdQuote
import xyz.oribuin.lilori.command.game.CmdSlap
import xyz.oribuin.lilori.command.general.CmdColor
import xyz.oribuin.lilori.command.general.CmdHelp
import xyz.oribuin.lilori.command.general.CmdPing
import xyz.oribuin.lilori.command.moderation.CmdBan
import xyz.oribuin.lilori.command.moderation.CmdPurge
import xyz.oribuin.lilori.command.music.*
import xyz.oribuin.lilori.command.support.general.CmdAnnounce
import xyz.oribuin.lilori.command.support.general.CmdReactionRole
import xyz.oribuin.lilori.command.support.ticket.CmdClose
import xyz.oribuin.lilori.command.support.ticket.CmdTicket
import xyz.oribuin.lilori.manager.Manager

class CommandHandler(bot: LilOri) : Manager(bot) {
    val commands = mutableListOf<BotCommand>()

    fun registerCommands() {
        commands.addAll(listOf(
                // General Commands
                CmdHelp(bot), CmdPing(bot),
                // Music Commands
                CmdLoop(bot), CmdPause(bot), CmdPlay(bot), CmdQueue(bot), CmdStop(bot), CmdVolume(bot),
                // Economy Commands
                CmdBalance(bot),
                // Game Commands
                CmdCoinflip(bot), CmdColor(bot), CmdEightball(bot), CmdFeed(bot), CmdQuote(bot), CmdSlap(bot),
                // Moderation Commands
                CmdPurge(bot, bot.eventWaiter), CmdBan(bot),
                // Author Commands
                CmdQuery(bot), CmdTest(bot),
                // Admin Commands
                CmdPerms(bot), CmdPrefix(bot),
                // Support Discord commands
                // General
                CmdAnnounce(bot), CmdReactionRole(bot),
                // Ticket
                CmdTicket(bot), CmdClose(bot, bot.eventWaiter)

        ))
    }

    fun getCommand(name: String): BotCommand {
        return commands.stream().filter { command -> command.getAnnotation(command.javaClass).name.toLowerCase() == name.toLowerCase() }.findFirst().get()
    }

    override fun enable() {
        // Unused
    }

    override fun disable() {
        commands.clear()
    }
}