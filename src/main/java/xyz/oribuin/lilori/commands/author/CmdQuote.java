package xyz.oribuin.lilori.commands.author;

import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CmdQuote extends Command {

    private final static Gson gson = new Gson();
    private Map<Integer, String> jsonMap = new HashMap<>();

    public CmdQuote() {
        this.name = "Quote";
        this.help = "Gain a saved quote..";
        this.category = new Category("Test");
        this.arguments = "";
        this.hidden = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        File dataFile = new File("data", "quotes.json");
        createFile();

        if (args.length == 1) {

            if (jsonMap.size() == 0) {
                event.reply(event.getAuthor().getAsMention() + ", There are no quotes currently saved.");
                return;
            }

            int randomInt = new Random().nextInt(jsonMap.size() + 1);

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor("Quotes")
                    .setColor(Color.decode("#33539e"))
                    .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                    .setDescription("Quote #" + randomInt + 1 + " " + jsonMap.get(randomInt + 1));

            event.reply(embedBuilder.build());
            return;
        }

        if (args.length >= 3) {
            final String quote = event.getMessage().getContentDisplay().substring(args[0].length() + args[1].length() + 2);

            if (args[1].equalsIgnoreCase("add")) {
                /**
                try (FileWriter fileWriter = new FileWriter(dataFile)) {
                    jsonMap.put(jsonMap.size() + 1, quote);
                    gson.toJson(jsonMap, fileWriter);

                    event.reply(event.getAuthor().getAsMention() + ", Added Quote **" + quote + "**");
                    System.out.println(event.getAuthor().getAsTag() + " Just added the quote " + quote + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 */

                LilOri.getInstance().getConnector().connect(connection -> {
                    String insert = "INSERT INTO quotes_table (quote_id, quote_text, quote_author) VALUES (?, ?)";

                    try (PreparedStatement statement = connection.prepareStatement(insert)) {
                        statement.setString(1, "test");
                        statement.setString(2, quote);
                        statement.setString(3, "ori");
                        statement.executeUpdate();
                    }

                    System.out.println(connection.prepareStatement("SELECT * FROM quote_table").toString());
                });

                System.out.println("Done");
            }
        }
    }

    private void createFile() {
        File dataFile = new File("data", "quotes.json");
        try {
            if (!dataFile.exists())
                dataFile.getParentFile().mkdirs();

            if (!dataFile.exists())
                dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}