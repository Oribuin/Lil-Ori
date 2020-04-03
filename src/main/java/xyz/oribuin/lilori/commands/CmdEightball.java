package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CmdEightball extends Command {

    public CmdEightball() {
        this.name = "Eightball";
        this.category = new Category("Fun");
        this.aliases = new String[]{"Ball"};
        this.arguments = "[Question]";
        this.help = "Ask the 8ball any question?";
        this.cooldown = 3;
        this.cooldownScope = CooldownScope.CHANNEL;
    }

    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please mention a user to slap.", 10, TimeUnit.SECONDS);
            return;
        }

        String[] ballAnswers = {
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
        };

        int randomAnswer = new Random().nextInt(ballAnswers.length);

        final String input;
        if (event.getMessage().getContentRaw().substring(args[0].length() + 1).endsWith("?"))
            input = event.getMessage().getContentRaw().substring(args[0].length() + 1);
        else
            input = event.getMessage().getContentRaw().substring(args[0].length() + 1) + "?";

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Ori's Magic 8Ball")
                .setThumbnail("https://imgur.com/FAfsGzj.png")
                .setDescription("**Question**\n" + input + "\n**Answer**\n" + ballAnswers[randomAnswer] + "")
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png");

        event.reply(embedBuilder.build());
    }
}
