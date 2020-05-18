package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;

public class CmdColor extends Command {
    public CmdColor() {
        this.name = "Color";
        this.description = "See a color in an embed.";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        Color embedColor;

        if (args.length < 2) {
            event.reply(event.getAuthor().getAsMention() + ", Please include the correct arguments. " + event.getPrefix() + "color <#HEX-CODE/Red,Green,Blue>");
            return;
        }

        if (args.length > 2 && args.length != 4) {
            event.reply(event.getAuthor().getAsMention() + ", Correct usage example: " + event.getPrefix() + "color 255 0 255");
            return;
        }

        try {
            if (args[1].startsWith("#")) {
                embedColor = Color.decode(args[1]);
            } else {
                int red = Integer.parseInt(args[1]);
                int green = Integer.parseInt(args[2]);
                int blue = Integer.parseInt(args[3]);

                embedColor = new Color(red, green, blue);
            }

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Lil' Ori Colors")
                    .setColor(embedColor)
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("Use this command to show your favourite hex codes!\n \nColor: " + embedColor.getRed() + "," + embedColor.getGreen() + "," + embedColor.getBlue());

            event.getChannel().sendMessage(embedBuilder.build()).queue();

        } catch (NumberFormatException ex) {
            event.reply(event.getAuthor().getAsMention() + ", Correct usage example: " + event.getPrefix() + "color 255 0 255");
        }
    }
}
