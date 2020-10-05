package xyz.oribuin.lilori.handler

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.command.discord.administrative.CmdPerms
import xyz.oribuin.lilori.command.discord.administrative.CmdPrefix
import xyz.oribuin.lilori.command.discord.author.CmdQuery
import xyz.oribuin.lilori.command.discord.author.CmdTest
import xyz.oribuin.lilori.command.discord.author.CmdUpdate
import xyz.oribuin.lilori.command.discord.economy.CmdBalance
import xyz.oribuin.lilori.command.discord.economy.CmdFeed
import xyz.oribuin.lilori.command.discord.game.*
import xyz.oribuin.lilori.command.discord.general.CmdColor
import xyz.oribuin.lilori.command.discord.general.CmdHelp
import xyz.oribuin.lilori.command.discord.general.CmdPing
import xyz.oribuin.lilori.command.discord.moderation.CmdBan
import xyz.oribuin.lilori.command.discord.moderation.CmdPurge
import xyz.oribuin.lilori.command.discord.music.*
import xyz.oribuin.lilori.command.discord.support.general.CmdAnnounce
import xyz.oribuin.lilori.command.discord.support.general.CmdReactionRole
import xyz.oribuin.lilori.command.discord.support.ticket.CmdClose
import xyz.oribuin.lilori.command.discord.support.ticket.CmdTicket
import xyz.oribuin.lilori.manager.Manager

class CommandHandler(bot: LilOri) : Manager(bot) {
    val commands = mutableListOf<Command>()

    fun registerCommands() {
        commands.addAll(listOf(
                // General Commands
                CmdHelp(bot), CmdPing(bot),
                // Music Commands
                CmdPause(bot), CmdPlay(bot), CmdQueue(bot), CmdStop(bot), CmdVolume(bot),
                // Economy Commands
                CmdBalance(bot),
                // Game Commands
                CmdCoinflip(bot), CmdColor(bot), CmdEightball(bot), CmdFeed(bot), CmdQuote(bot), CmdSlap(bot),
                // Moderation Commands
                CmdPurge(bot, bot.eventWaiter), CmdBan(bot),
                // Author Commands
                CmdQuery(bot), CmdTest(bot), CmdUpdate(bot),
                // Admin Commands
                CmdPerms(bot), CmdPrefix(bot),
                // Support Discord commands
                // General
                CmdAnnounce(bot), CmdReactionRole(bot),
                // Ticket
                CmdTicket(bot), CmdClose(bot, bot.eventWaiter)

        ))
    }

    fun getCommand(name: String): Command {
        return commands.stream().filter { command: Command -> command.name.toLowerCase() == name }.findFirst().get()
    }

    override fun enable() {
        // Unused
    }
}