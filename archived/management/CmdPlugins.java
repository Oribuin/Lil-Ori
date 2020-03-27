package xyz.oribuin.lilori.commands.author.management;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class CmdPlugins extends Command {

    public List<String> plugins = new ArrayList<>();

    public CmdPlugins() {
        this.name = "Plugins";
        this.arguments = "[None]";
        this.help = "Get a list of all the available plugins.";
        this.category = new Category("Misc");
    }

    protected void execute(CommandEvent event) {
        plugins.add("adminnotice");
        plugins.add("chatemoji");
        plugins.add("craftholograms");
        plugins.add("flighttrails");
        plugins.add("headowner");
        plugins.add("itemsettings");

        EmbedBuilder Embed = new EmbedBuilder()
                .setAuthor("Plugin List")
                .setDescription(":loudspeaker: - AdminNotice\n" +
                        ":heart: - ChatEmoji\n" +
                        ":tools: - CraftHolograms\n" +
                        ":airplane_small: - FlightTrails\n" +
                        ":grin: - HeadOwner\n" +
                        ":axe: - ItemSettings")
                .setFooter("Created by Ori#0004");

        event.reply(Embed.build());
    }
}
