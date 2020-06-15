package xyz.oribuin.lilori.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.Settings;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

public class CmdQuote extends Command {

    private int quoteSize;

    public CmdQuote() {
        this.name = "Quote";
        this.description = "Quote command.";
        this.aliases = Collections.emptyList();
        //this.arguments = "";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor("Lil' Ori Quotes")
                    .setColor(Color.decode("#33539e"))
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("To view a quote, type " + event.getPrefix() + "quote get <id>\n \n" +
                            "Quote Id Amount: " + this.getQuoteSize());

            event.reply(embedBuilder);
            return;
        }

        switch (args[1].toLowerCase()) {
            case "add":
                if (!event.getAuthor().getId().equals(Settings.OWNER_ID)) return;

                if (args.length >= 5) {
                    String QUOTE_ID = args[2].toLowerCase();
                    String QUOTE_AUTHOR = args[3].replace("_", " ");
                    String QUOTE = event.getMessage().getContentRaw().substring(args[0].length() + args[1].length() + args[2].length() + args[3].length() + 4);

                    this.bot.getDataManager().updateQuote(QUOTE_ID, QUOTE_AUTHOR, QUOTE);

                    event.reply(event.getAuthor().getAsMention() + ", Added quote to the database!");
                    System.out.println(event.getAuthor().getAsTag() + " Added a quote to the database.\n \n" + args[2] + "\n" + args[3].replace("_", " ") + "\n" + args[3]);
                    return;
                }

                event.reply(event.getAuthor().getAsMention() + ", Correct Usage: " + event.getPrefix() + "quote add <QUOTE_ID> <QUOTE_AUTHOR> <QUOTE>");
                break;
            case "select":

                if (args.length == 3) {

                    LilOri.getInstance().getConnector().connect(connection -> {

                        String query = "SELECT quote FROM quotes WHERE label = ?";
                        try (PreparedStatement statement = connection.prepareStatement(query)) {
                            statement.setString(1, args[2]);

                            ResultSet resultSet = statement.executeQuery();
                            if (resultSet.next())
                                event.reply(resultSet.getString(1));
                        }
                    });
                    return;
                }

                event.reply(event.getAuthor().getAsMention() + ", Correct Usage: " + event.getPrefix() + "quote select <QUOTE_ID>");
                break;
            case "remove":
                if (!event.getAuthor().getId().equals(Settings.OWNER_ID)) return;

                if (args.length == 3) {
                    this.bot.getDataManager().removeQuote(args[2]);

                    event.reply(event.getAuthor().getAsMention() + ", Removed a quote from the database!");
                    System.out.println(event.getAuthor().getAsTag() + " Removed a quote from the database.\nQuote Id: " + args[2]);
                    return;
                }

                event.reply(event.getAuthor().getAsMention() + ", Correct Usage: " + event.getPrefix() + "quote remove <QUOTE_ID>");
                break;

            case "get":
                if (args.length == 3) {
                    LilOri.getInstance().getConnector().connect(connection -> {
                        String query = "SELECT * FROM quotes WHERE label = ?";

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

                    return;
                }

                event.reply(event.getAuthor().getAsMention() + ", Correct Usage: " + event.getPrefix() + "quote get <QUOTE_ID>");
                break;
            default:
                event.reply(event.getAuthor().getAsMention() + ", Invalid Args.");
        }
    }

    private int getQuoteSize() {
        LilOri.getInstance().getConnector().connect(connection -> {
            String query = "SELECT * FROM quotes";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.last();

                quoteSize = resultSet.getRow();
            }
        });


        return quoteSize;
    }
}