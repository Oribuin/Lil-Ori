package xyz.oribuin.lilori.command.author

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent

@CommandInfo(
        name = "Test",
        description = "Testing general functions for Ori.",
        category = Category.Type.AUTHOR,
        arguments = [],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = "",
        ownerOnly = true
)
class CmdTest(bot: LilOri) : BotCommand(bot) {

    override fun executeCommand(event: CommandEvent) {
        event.reply("Test")

    }
}
