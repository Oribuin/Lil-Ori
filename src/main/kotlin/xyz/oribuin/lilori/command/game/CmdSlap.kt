package xyz.oribuin.lilori.command.game

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.CommandInfo
import xyz.oribuin.lilori.handler.BotCommand
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.util.BotUtils
import java.util.*

@CommandInfo(
        name = "Slap",
        description = "Slap all your friends in weeb style.",
        category = Category.Type.GAMES,
        arguments = ["<@users>"],
        aliases = [],
        userPermissions = [],
        botPermissions = [],
        guildId = ""
)
class CmdSlap(bot: LilOri) : BotCommand(bot) {

    override fun executeCommand(event: CommandEvent) {

        // Check if the author has provided the right amount of arguments
        if (event.args.size < 2) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${getAnnotation(javaClass).name.toLowerCase()} ${BotUtils.formatList(getAnnotation(javaClass).arguments.toList())}")
            return
        }

        // Check if the author has mentioned a user
        if (event.message.mentionedMembers.size == 0) {
            event.sendEmbedReply("❗ Invalid Arguments", "Please include a user(s) to slap!")
            return
        }

        // Check if the members they mentioned contains themself
        if (event.message.mentionedMembers.contains(event.member)) {
            event.sendEmbedReply("\uD83D\uDE2C Confused Ori", "Why would you slap yourself?")
            return
        }

        // Check if the members they mentioned contains the bot.
        if (event.message.mentionedMembers.contains(event.selfMember)) {
            event.sendEmbedReply("\uD83D\uDE2D Sad Ori", "Why would you slap me? :(")
            return
        }

        // Define the list of gifs for the slap command
        val gifUrls = listOf(
                "https://media1.tenor.com/images/612e257ab87f30568a9449998d978a22/tenor.gif",
                "https://media1.tenor.com/images/153b2f1bfd3c595c920ce60f1553c5f7/tenor.gif",
                "https://media1.tenor.com/images/3fd96f4dcba48de453f2ab3acd657b53/tenor.gif",
                "https://media1.tenor.com/images/0892a52155ac70d401126ede4d46ed5e/tenor.gif",
                "https://media1.tenor.com/images/8b7788813720b2db4a28c64458f3bd81/tenor.gif",
                "https://media1.tenor.com/images/477821d58203a6786abea01d8cf1030e/tenor.gif"
        )

        // Get a random gif url from the list
        val randomGif = Random().nextInt(gifUrls.size)

        // Define a new mutable list
        val userMentions = mutableListOf<String>()

        // Add all the users mentions into the list
        event.message.mentionedMembers.forEach { member -> userMentions.add(member.asMention) }

        // Define the embed with the gif
        val embedBuilder = EmbedBuilder()
                .setDescription("${event.author.asMention} **Slapped** ${BotUtils.formatList(userMentions)}**!**")
                .setImage(gifUrls[randomGif])
                .setFooter("Created by Ori#0004", "http://img.oribuin.xyz/profile.png")
                .setColor(event.color)

        // Send the embed to chat.
        event.reply(embedBuilder.build())
    }
}