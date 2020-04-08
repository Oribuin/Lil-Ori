package xyz.oribuin.lilori.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CmdSlap extends Command {

    public CmdSlap() {
        this.name = "Slap";
        this.category = new Category("Fun");
        this.arguments = "[@User]";
        this.help = "Slap a user";
    }

    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please mention a user to slap.", 10, TimeUnit.SECONDS);
            return;
        }

        if (event.getMessage().getMentionedMembers().size() == 0) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please mention a user to slap.", 10, TimeUnit.SECONDS);
            return;
        }

        if (event.getMessage().getMentionedMembers().contains(event.getMember())) {
            event.reply(event.getAuthor().getAsMention() + ", Why would you slap yourself?");
            return;
        }

        if (event.getMessage().getMentionedMembers().contains(event.getSelfMember())) {
            event.reply(event.getAuthor().getAsMention() + ", Why would you want to slap me?? <a:PepeSad:616330488063328266>");
            return;
        }

        String[] gifUrls = {
                "https://media1.tenor.com/images/612e257ab87f30568a9449998d978a22/tenor.gif",
                "https://media1.tenor.com/images/153b2f1bfd3c595c920ce60f1553c5f7/tenor.gif",
                "https://media1.tenor.com/images/3fd96f4dcba48de453f2ab3acd657b53/tenor.gif",
                "https://media1.tenor.com/images/0892a52155ac70d401126ede4d46ed5e/tenor.gif",
                "https://media1.tenor.com/images/8b7788813720b2db4a28c64458f3bd81/tenor.gif"
        };

        int randomGif = new Random().nextInt(gifUrls.length);

        List<String> userMentions = new ArrayList<>();

        event.getMessage().getMentionedMembers().forEach(member -> userMentions.add(member.getAsMention()));

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setDescription(event.getAuthor().getAsMention() + " **Slapped** " + userMentions.toString()
                        .replaceAll("\\[", "").replaceAll("]", "") + "**!**")
                .setImage(gifUrls[randomGif])
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png");

        event.reply(embedBuilder.build());
    }
}
