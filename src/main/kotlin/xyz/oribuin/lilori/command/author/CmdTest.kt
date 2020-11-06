package xyz.oribuin.lilori.command.author

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Cmd
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color

@Cmd(
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
class CmdTest(bot: LilOri) : Command(bot) {

    override fun executeCommand(event: CommandEvent) {
        println(Color.decode("#0ca4ff").rgb and 0xFFFFFF)

    }
}
