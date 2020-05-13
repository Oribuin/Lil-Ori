package xyz.oribuin.lilori.commands.author;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

import java.awt.*;
import java.sql.PreparedStatement;

public class CmdQuery extends Command {

    public CmdQuery() {
        this.name = "Query";
        this.description = "Query a command in MySQL.";
        this.category = new Command.Category("Author");
        this.arguments = "<Query>";

        this.hidden = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) return;
        LilOri.getInstance().getConnector().connect(connection -> {
            final String query = event.getMessage().getContentRaw().substring(args[0].length() + 1);

            try (PreparedStatement getStatement = connection.prepareStatement(query)) {
                getStatement.executeUpdate();
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor("Executed SQL Query")
                        .setColor(Color.decode("#33539e"))
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("**Database:** (SQLite) lilori.db\n" +
                                "\n " +
                                "**Query: **" + query);

                event.reply(embedBuilder.build());
                System.out.println("Executed Query: " + query);
            }
        });
    }
}