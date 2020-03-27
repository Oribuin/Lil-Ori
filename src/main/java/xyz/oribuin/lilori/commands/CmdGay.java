package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CmdGay extends Command {
    private EventWaiter waiter;
    //public List<String> userList;

    private Random random = new Random();
    int lowerAmount = 1;
    int upperBound = 100;

    public CmdGay(EventWaiter waiter) {
        this.name = "Gay";
        this.category = new Category("Fun");
        this.arguments = "[None]";
        this.help = "How gay are you?";
        this.waiter = waiter;
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.MESSAGE_ADD_REACTION};

        //this.userList = new ArrayList<>();
    }

    @Override
    protected void execute(CommandEvent event) {
        // Define Arguments
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args.length == 2 && args[1].equalsIgnoreCase("clear")) {
            if (!event.getAuthor().getId().equals(event.getClient().getOwnerId())) {
                event.deleteCmd();
                return;
            }

            // Delete Command, Clear Array List and tell player
            event.deleteCmd();
            event.timedReply(event.getAuthor().getAsMention() + ", Reset the ArrayList.", 5, TimeUnit.SECONDS);
            //userList.clear();
            return;
        }

        // If the array list contains the user, stop them from creating another message.
        /*
        if (userList.contains(event.getAuthor().getId())) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", You already have a calculator active.", 10, TimeUnit.SECONDS);
            return;
        }
         */

        // Tell them their gayness is being calculated
        event.getChannel().sendMessage("Calculating your gayness :rainbow_flag:").queue(message -> {
            // Delete message just sent after 2 seconds
            message.delete().queueAfter(2, TimeUnit.SECONDS);

            // Add user to array list
            //userList.add(event.getAuthor().getId());

            // define a random amount
            int randomAmount = random.nextInt(upperBound - lowerAmount + 1) + lowerAmount;

            // define the embed
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor("Gay Calculator")
                    .setColor(Color.decode("#cca8db"))
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("**You are " + randomAmount + "% Gay** :rainbow_flag:");

            event.getChannel().sendMessage(embedBuilder.build()).queueAfter(2, TimeUnit.SECONDS);
            // TODO: Have the bot reset the number every time the dice is pressed.

            /*
            // Send the embed after 2 seconds and add the appropriate reaction
            event.getChannel().sendMessage(embedBuilder.build()).queueAfter(2, TimeUnit.SECONDS, msg -> {
                msg.addReaction("\uD83C\uDFB2").queue();
                // Wait for a GuildMessageReactionAddEvent, Check for if the member is right and if the emoji is the right one
                waiter.waitForEvent(GuildMessageReactionAddEvent.class, check -> check.getMember().equals(event.getGuild().getMember(event.getAuthor())), action -> {
                    // Define new number
                    int newRandomAmount = random.nextInt(upperBound - lowerAmount + 1) + lowerAmount;

                    // Define new embed.
                    EmbedBuilder newEmbed = new EmbedBuilder()
                            .setAuthor("Gay Calculator")
                            .setColor(Color.decode("#cca8db"))
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                            .setDescription("**You are " + newRandomAmount + "% Gay** :rainbow_flag:");

                    // Clear Reactions, Edit message with new Embed and add reaction again
                    msg.clearReactions().queue();
                    msg.editMessage(newEmbed.build()).queue();
                    //userList.remove(event.getAuthor().getId());
                    //msg.addReaction("\uD83C\uDFB2").queueAfter(1, TimeUnit.SECONDS);
                });
            });
             */
        });
    }

    /*
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getMessageId().equals(reactionMessage.getId())) {
            if (event.getReaction().getReactionEmote().getEmote().equals("\uD83C\uDFB2") && event.getMember().equals(commandEvent.getGuildAuthor())) {
                int newRandomAmount = random.nextInt(upperBound - lowerAmount + 1) + lowerAmount;

                EmbedBuilder newEmbed = new EmbedBuilder()
                        .setAuthor("Gay Calculator")
                        .setColor(Color.decode("#CCA8DB"))
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("**You are " + newRandomAmount + "% Gay** :rainbow_flag:");

                reactionMessage.clearReactions().queue();
                reactionMessage.editMessage(newEmbed.build()).queue();
                reactionMessage.addReaction("\uD83C\uDFB2").queueAfter(1, TimeUnit.SECONDS);
            }
        }
    }

     */
}