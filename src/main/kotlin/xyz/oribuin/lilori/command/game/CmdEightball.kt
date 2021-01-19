package xyz.oribuin.lilori.command.game

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.util.BotUtils
import java.util.*

@BotCommand.CommandInfo(
        name = "eightball",
        description = "Get your current balance for the bot.",
        category = Category.Type.GAMES,
        arguments = ["<question>?"],
        aliases = ["8ball"],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)
class CmdEightball(bot: LilOri) : BotCommand(bot, bot.eventWaiter) {

    override fun executeCommand(event: CommandEvent) {

        // Check if the right amount of args were provided
        if (event.args.size < 2) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        // Define the list of answers possible inside the 8ball
        val ballAnswers = listOf(
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

        // Get a random answer from the list
        val randomAnswer = Random().nextInt(ballAnswers.size)

        // Define the question
        val stringBuilder = StringBuilder(java.lang.String.join(" ", *event.args).substring(event.args[0].length + 1))

        // Add a question mark at the end for grammar.
        if (!stringBuilder.toString().endsWith("?")) {
            stringBuilder.append("?")
        }

        // Define the embed.
        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDD2E Lil' Ori's Magic 8Ball")
                .setThumbnail("http://img.oribuin.xyz/lilori/8ball.png")
                .setDescription("""**»** Question
                    $stringBuilder
                    
                    **»** Answer
                    ${ballAnswers[randomAnswer]}""".trimIndent())
                .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")
                .setColor(event.color)


        // Send the embed to the channel
        event.channel.sendMessage(embedBuilder.build()).queue()
    }

}