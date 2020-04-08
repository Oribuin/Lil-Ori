package xyz.oribuin.lilori.commands.administrative;

import com.google.gson.Gson;
import xyz.oribuin.lilori.managers.QuoteManager;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CmdQuote extends Command {
    public CmdQuote() {
        this.name = "Quote";
        this.help = "Gain a saved quote..";
        this.category = new Category("Test");
        this.arguments = "[None]";
        this.hidden = true;
        this.ownerCommand = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        File dataFile = new File("data", "quotes.json");
        createFile();
        QuoteManager quoteManager = new QuoteManager();
        Map<Integer, String> jsonMap = quoteManager.jsonMap;


        if (args.length == 1) {
            event.reply("soon:tm:");
            return;
        }

        if (args.length >= 3) {
            final String quote = event.getMessage().getContentDisplay().substring(args[0].length() + args[1].length() + 2);

            if (args[1].equalsIgnoreCase("add")) {
                try {
                    jsonMap.put(1, quote);

                    quoteManager.save();
                    event.reply(event.getAuthor().getAsMention() + ", Added Quote \"" + quote + "\"");
                    System.out.println(event.getAuthor().getAsTag() + " Just added the quote " + quote + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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