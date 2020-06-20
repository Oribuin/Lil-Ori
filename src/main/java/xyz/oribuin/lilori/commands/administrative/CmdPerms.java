package xyz.oribuin.lilori.commands.administrative;

import net.dv8tion.jda.api.EmbedBuilder;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdPerms extends Command {
    public CmdPerms() {
        this.name = "Permissions";
        this.aliases = Collections.singletonList("Perms");
        this.description = "List of permissions the bot has.";
        this.aliases = Collections.emptyList();
    }

    @Override
    public void executeCommand(CommandEvent event) {

        List<String> perms = new ArrayList<>();
        event.getSelfMember().getPermissions().forEach(permission -> perms.add(permission.getName().toLowerCase().replace("_", " ")));

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Lil' Ori Permissions")
                .setColor(Color.decode("#babaeb"))
                .setDescription(perms.toString().replace("\\[", "").replace("]", "").replace( ",", "\n •"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png");

        event.reply(embedBuilder.build());
    }
}
