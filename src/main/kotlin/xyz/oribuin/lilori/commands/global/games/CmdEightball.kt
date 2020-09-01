package xyz.oribuin.lilori.commands.global.games

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.*
import java.util.concurrent.TimeUnit

class CmdEightball(bot: LilOri) : Command(bot) {
    init {
        name = "Eightball"
        category = Category(Category.Type.GAMES)
        aliases = listOf("Ball")
        description = "Ask the 8ball any question?"
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {

        val args = event.message.contentRaw.split(" ").toTypedArray()
        if (args.size < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Please mention a user to slap.", 10, TimeUnit.SECONDS)
            return
        }

        val ballAnswers = arrayOf(
                "It is certain.",
                "It is decidedly so.",
                "Without a doubt.",
                "Definitely.",
                "You may rely on it.",
                "As I see it, yes.",
                "Most likely.",
                "Outlook good.",
                "Yes.",
                "Signs point to yes.",
                "Reply hazy, try again.",
                "Ask again later.",
                "Better not tell you now.",
                "Cannot predict now.",
                "Concentrate and ask again.",
                "Don't count on it.",
                "My reply is no.",
                "My sources say no.",
                "Outlook not so good.",
                "Very doubtful."
        )

        val randomAnswer = Random().nextInt(ballAnswers.size)
        val input: String

        input = if (event.message.contentRaw.substring(args[0].length + 1).endsWith("?")) {
            event.message.contentRaw.substring(args[0].length + 1)
        } else {
            event.message.contentRaw.substring(args[0].length + 1) + "?"
        }

        val embedBuilder = EmbedBuilder()
                .setAuthor("Ori's Magic 8Ball")
                .setThumbnail("https://imgur.com/FAfsGzj.png")
                .setDescription("**Question**" +
                        input +
                        "**Answer**" +
                        ballAnswers[randomAnswer].trimIndent())
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")


        event.reply(embedBuilder.build())
    }

}