package xyz.oribuin.lilori.command.discord.game

import net.dv8tion.jda.api.EmbedBuilder
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.util.*
import java.util.concurrent.TimeUnit

class CmdCoinflip(bot: LilOri) : Command(bot) {
    init {
        name = "Coinflip"
        category = Category(Category.Type.GAMES)
        aliases = listOf("Flip")
        description = "Flip a coin."
        arguments = emptyList()
    }

    override fun executeCommand(event: CommandEvent) {
        // Define the rates
        val lowerInt = 1
        val higherInt = 10

        // Create the first embed.
        val embedBuilder = EmbedBuilder()
                .setAuthor("\uD83D\uDCB0 Flipping the coin")
                .setFooter("Created by Ori#0004", "https://img.oribuin.xyz/profile.png")
                .setImage("https://media1.tenor.com/images/938e1fc4fcf2e136855fd0e83b1e8a5f/tenor.gif")
                .setColor(event.color)
                .build()

        // Send the first embed created.
        event.channel.sendMessage(embedBuilder).queue { message ->
            // Get a random number out of the rate numbers.
            val randomInt = Random().nextInt(higherInt - lowerInt + 1) + lowerInt

            // Create a new embed
            val embed = EmbedBuilder()
                    .setColor(event.color)
                    .setFooter("Created by Ori#0004", "https://img.oribuin.xyz/profile.png")

            // If the chance is higher than 100, Drop the coin.
            if (randomInt > 100) {
                embed.setAuthor("\uD83D\uDCB0 Dropped the coin").setDescription("Oh no! I've dropped the coin, Can we try again?")

                // If the chance is higher than 50, Land on Heads
            } else if (randomInt > 50) {
                embed.setAuthor("\uD83D\uDCB0 Caught the coin").setDescription("You have landed on **Heads**")

                // If the chance is lower than 50, Land on tails
            } else {
                embed.setAuthor("\uD83D\uDCB0 Caught the coin").setDescription("You have landed on **Tails**")
            }

            // Edit the embed with the second embed after 3 seconds.
            message.editMessage(embed.build()).queueAfter(3, TimeUnit.SECONDS)
        }
    }
}