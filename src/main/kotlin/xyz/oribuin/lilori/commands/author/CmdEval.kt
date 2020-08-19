package xyz.oribuin.lilori.commands.author

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.awt.Color
import java.util.concurrent.TimeUnit
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class CmdEval(bot: LilOri) : Command(bot) {
    init {
        name = "Eval"
        aliases = emptyList()
        description = "An evaluation."
        arguments = emptyList()
        isEnabled = false
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {

        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size == 1) {
            event.reply(event.author.asMention + ", Please include some code to evaluate.")
            return
        }

        val eval = event.message.contentRaw.substring(args[0].length + 1)

        val engine = ScriptEngineManager().getEngineByName("nashorn")
        var error: String? = null
        try {
            engine.eval(eval)
        } catch (e: ScriptException) {
            error = e.message
        }
        val embedBuilder = EmbedBuilder()
                .setAuthor("Lil' Ori Evaluation")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**Eval:**```java\n$eval```\n**Exception:**\n$error")

        event.deleteCmd()
        event.timedReply(embedBuilder.build(), 5, TimeUnit.SECONDS)
    }

}