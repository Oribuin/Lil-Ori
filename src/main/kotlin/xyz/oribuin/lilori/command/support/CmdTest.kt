package xyz.oribuin.lilori.command.support

import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Cmd
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent

@Cmd(
        name = "Test",
        category = Category.Type.ECONOMY,
        arguments = ["arg1", "arg2"],
        aliases = ["alias1", "alias2"],
        userPermissions = [Permission.ADMINISTRATOR],
        botPermissions = [Permission.ADMINISTRATOR],
        guildId = "731659405958971413"
)

class CmdTest(bot: LilOri) : Command(bot) {
    override fun executeCommand(event: CommandEvent) {
        event.reply("$${event.author.asMention}, You successfully executed this command.")
        event.message.addReaction("\uD83D\uDC4D").queue()
    }
}