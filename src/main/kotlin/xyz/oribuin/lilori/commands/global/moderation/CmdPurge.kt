package xyz.oribuin.lilori.commands.global.moderation

import net.dv8tion.jda.api.Permission
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.concurrent.TimeUnit

class CmdPurge(bot: LilOri) : Command(bot) {

    init {
        name = "Purge"
        aliases = listOf("clear")
        description = "Mass clear server messages."
        arguments = listOf("<msg-count>/channel")
        botPermissions = arrayOf(Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
        userPermissions = arrayOf(Permission.MESSAGE_MANAGE, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_CHANNEL)
        this.isEnabled = true
    }

    // ignore all of this
    override fun executeCommand(event: CommandEvent) {

        event.reply("Currently being recoded")

        // Variables
        val args: Array<String?> = event.message.contentRaw.split(" ").toTypedArray()
        if (event.member == null)
            return

        if (args.size <= 2) {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Correct Format: ;purge " + arguments, 10, TimeUnit.SECONDS)
            return
        }


    }
}