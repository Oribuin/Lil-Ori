package xyz.oribuin.lilori.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.Settings;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CmdQuote extends Command {

    public CmdQuote() {
        this.name = "Quote";
        this.description = "Quote command.";
        //this.arguments = "";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        switch (args[1].toLowerCase()) {
            case "add":
                if (!event.getAuthor().getId().equals(Settings.OWNER_ID)) return;

                if (args.length >= 5) {
                    String QUOTE_ID = args[2].toLowerCase();
                    String QUOTE_AUTHOR = args[3];
                    String QUOTE = event.getMessage().getContentRaw().substring(args[0].length() + args[1].length() + args[2].length() + args[3].length() + 4);

                    LilOri.getInstance().getConnector().connect(connection -> {
                        String query = "INSERT INTO quote_table (quote_id, quote_author, quote)" +
                                "VALUES (?, ?, ?)";

                        try (PreparedStatement statement = connection.prepareStatement(query)) {
                            statement.setString(1, QUOTE_ID);
                            statement.setString(2, QUOTE_AUTHOR.replace("_", " "));
                            statement.setString(3, QUOTE);
                            statement.executeUpdate();
                        }
                    });

                    event.reply(event.getAuthor().getAsMention() + ", Added quote to the database!");
                    System.out.println(event.getAuthor().getAsTag() + " Added a quote to the database.\n \n" + QUOTE_ID + "\n" + QUOTE_AUTHOR + "\n" + QUOTE);
                }
                break;
            case "select":

                if (args.length == 3) {
                    LilOri.getInstance().getConnector().connect(connection -> {

                        String query = "SELECT quote FROM quote_table WHERE quote_id = ?";
                        try (PreparedStatement statement = connection.prepareStatement(query)) {
                            statement.setString(1, args[2]);

                            ResultSet resultSet = statement.executeQuery();
                            if (resultSet.next())
                                event.reply(resultSet.getString(1));
                        }
                    });
                }
                break;
            case "remove":
                if (!event.getAuthor().getId().equals(Settings.OWNER_ID)) return;

                if (args.length == 3) {
                    LilOri.getInstance().getConnector().connect(connection -> {
                        String query = "DELETE FROM quote_table WHERE quote_id = ?";
                        try (PreparedStatement removeStatement = connection.prepareStatement(query)) {
                            removeStatement.setString(1, args[2]);
                            removeStatement.executeUpdate();
                        }

                        event.reply(event.getAuthor().getAsMention() + ", Removed a quote from the database!");
                        System.out.println(event.getAuthor().getAsTag() + " Removed a quote from the database.\nQuote Id: " + args[2]);
                    });
                }
                break;

            case "get":
                if (args.length == 3) {
                    LilOri.getInstance().getConnector().connect(connection -> {
                        String query = "SELECT * FROM quote_table WHERE quote_id = ?";

                        try (PreparedStatement getStatement = connection.prepareStatement(query)) {
                            getStatement.setString(1, args[2]);
                            ResultSet resultSet = getStatement.executeQuery();
                            if (resultSet.next()) {
                                EmbedBuilder embedBuilder = new EmbedBuilder()
                                        .setTitle("Lil' Ori Quotes (ID: " + resultSet.getString(1) + ")")
                                        .setColor(Color.decode("#33539e"))
                                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                                        .setDescription("**Quote Author:** " + resultSet.getString(2) +
                                                "\n\n**Quote:** " + resultSet.getString(3));

                                event.reply(embedBuilder.build());
                            }
                        }
                    });
                }
                break;
            default:
                event.reply(event.getAuthor().getAsMention() + ", Invalid Args.");
        }
    }
}