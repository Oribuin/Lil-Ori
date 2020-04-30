package xyz.oribuin.lilori.commands.author;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CmdTest extends Command {

    public CmdTest() {
        this.name = "Testt";
        this.help = "A test command.";
        this.category = new Category("Test");
        this.arguments = "[None]";

        this.hidden = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Saving Emotes...");
        event.getGuild().getEmotes().forEach(emote -> {
            Guild guild = event.getJDA().getGuildById("702913002663444490");

            if (guild == null) return;

            try {
                URL url = new URL(emote.getImageUrl());
                BufferedImage image = ImageIO.read(url);

                if (!emote.isAnimated()) {
                    ImageIO.write(image, "png", getFile(emote.getName(), "png"));
                    Icon icon = Icon.from(getFile(emote.getName(), "png"));
                    guild.createEmote("emc_" + emote.getName().toLowerCase(), icon, guild.getPublicRole()).queue();
                    getFile(emote.getName(), "png").delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private File getFile(String name, String type) {
        File file = new File("data", name + "." + type);

        try {
            if (file.exists())
                file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
