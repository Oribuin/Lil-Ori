package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.sql.PreparedStatement;

public class CmdColor extends Command {
    private Color embedColor;

    public CmdColor() {
        this.name = "Color";
        this.description = "See a color in an embed.";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) {
            event.reply(event.getAuthor().getAsMention() + ", Please include the correct arguments. " + event.getPrefix() + "color <#HEX-CODE/Red,Green,Blue>");
            return;
        }

        if (args.length > 2 && !args[1].equals("select") && args.length != 4) {
            event.reply(event.getAuthor().getAsMention() + ", Correct usage example: " + event.getPrefix() + "color 255 0 255");
            return;
        }

        try {

            if (args[1].startsWith("#")) {
                embedColor = Color.decode(args[1]);

            } else if (args[1].toLowerCase().equals("select")) {

                LilOri.getInstance().getConnector().connect(connection -> {
                    String query = "REPLACE INTO embed_color (hex) VALUES (?)";

                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, args[2].toUpperCase());
                        statement.executeUpdate();

                        event.reply("Changed Embed color to " + args[2].toUpperCase());
                    }
                });


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
