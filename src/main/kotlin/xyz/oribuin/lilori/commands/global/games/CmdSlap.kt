package xyz.oribuin.lilori.commands.global.games

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class CmdSlap(bot: LilOri) : Command(bot) {
    init {
        name = "Slap"
        category = Category(Category.Type.GAMES)
        aliases = emptyList()
        description = "Slap a user"
        arguments = listOf("<@users>")
    }

    override fun executeCommand(event: CommandEvent) {


        val args = event.message.contentRaw.split(" ").toTypedArray()

        // Check argument length
        if (args.size < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Please mention a user to slap.", 10, TimeUnit.SECONDS)
            return
        }

        // Check member size
        if (event.message.mentionedMembers.size == 0) {
            event.deleteCmd(10, TimeUnit.SECONDS)
            event.timedReply(event.author.asMention + ", Please mention a user to slap.", 10, TimeUnit.SECONDS)
            return
        }

        // Check if member is author
        if (event.message.mentionedMembers.contains(event.member)) {
            event.reply(event.author.asMention + ", Why would you slap yourself?")
            return
        }

        // Check if member is bot
        if (event.message.mentionedMembers.contains(event.selfMember)) {
            event.reply(event.author.asMention + ", Why would you want to slap me?? <a:PepeSad:616330488063328266>")
            return
        }

        // Define gifs
        val gifUrls = arrayOf(
                "https://media1.tenor.com/images/612e257ab87f30568a9449998d978a22/tenor.gif",
                "https://media1.tenor.com/images/153b2f1bfd3c595c920ce60f1553c5f7/tenor.gif",
                "https://media1.tenor.com/images/3fd96f4dcba48de453f2ab3acd657b53/tenor.gif",
                "https://media1.tenor.com/images/0892a52155ac70d401126ede4d46ed5e/tenor.gif",
                "https://media1.tenor.com/images/8b7788813720b2db4a28c64458f3bd81/tenor.gif",
                "https://media1.tenor.com/images/477821d58203a6786abea01d8cf1030e/tenor.gif"
        )

        // Generate random gif
        val randomGif = Random().nextInt(gifUrls.size)
        val userMentions: MutableList<String> = ArrayList()
        event.message.mentionedMembers.forEach(Consumer { member: Member -> userMentions.add(member.asMention) })

        val embedBuilder = EmbedBuilder()
                .setDescription(event.author.asMention + " **Slapped** " + userMentions.toString()
                        .replace("\\[".toRegex(), "").replace("]", "") + "**!**")
                .setImage(gifUrls[randomGif])
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")

        event.reply(embedBuilder.build())
    }
}