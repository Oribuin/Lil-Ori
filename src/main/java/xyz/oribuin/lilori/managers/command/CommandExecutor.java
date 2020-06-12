package xyz.oribuin.lilori.managers.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.LilOri;
import xyz.oribuin.lilori.Settings;
import xyz.oribuin.lilori.utils.GuildSettings;

import java.awt.*;
import java.util.Arrays;

public class CommandExecutor extends ListenerAdapter {

    private final LilOri bot;
    private final CommandHandler commandHandler;

    public CommandExecutor(LilOri bot, CommandHandler commandHandler) {
        this.bot = bot;
        this.commandHandler = commandHandler;
    }


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        // TODO: Create a command client to store owner id


        GuildSettings guildSettings = bot.getGuildSettingsManager().getGuildSettings(event.getGuild());

        if (!event.getMessage().getContentRaw().startsWith(guildSettings.getPrefix()))
            return;

        // Filter through each command
        for (Command cmd : commandHandler.getCommands()) {

            try {
                String[] args = event.getMessage().getContentRaw().split(" ");

                if (cmd.getAliases() == null) {
                    throw new IllegalArgumentException("Null Aliases in command " + cmd.getName());
                }

                // Check if command name or alias
                if (!cmd.getName().equalsIgnoreCase(args[0].substring(1)) && cmd.getAliases().stream().noneMatch(x -> x.equalsIgnoreCase(args[0].substring(1))))
                    continue;

                // Check if command is enabled
                if (!cmd.isEnabled())
                    return;

                // Check if the command is owner only and the owner id equals to command author
                if (cmd.isOwnerOnly() && !event.getAuthor().getId().equals(Settings.OWNER_ID)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Sorry! You don't have permission to use this command.").queue();
                    return;
                }

                // Check if the command author is a bot or fake
                if (event.getAuthor().isBot() || event.getAuthor().isFake())
                    return;

                // Check user permissions
                if (cmd.getBotPermissions() != null && !event.getGuild().getSelfMember().getPermissions().containsAll(Arrays.asList(cmd.getBotPermissions()))) {
                    EmbedBuilder botEmbed = new EmbedBuilder()
                            .setAuthor("Missing Permissions!")
                            .setColor(Color.decode("#33539e"))
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                            .setDescription("I am missing the permissions to execute this command.");

                    event.getChannel().sendMessage(botEmbed.build()).queue();
                    return;
                }

                // Check user permissions
                if (cmd.getUserPermissions() != null && event.getMember() != null && !event.getMember().getPermissions().containsAll(Arrays.asList(cmd.getUserPermissions()))) {
                    EmbedBuilder userEmbed = new EmbedBuilder()
                            .setAuthor("Missing Permissions!")
                            .setColor(Color.decode("#33539e"))
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                            .setDescription("You are missing the permissions to execute this command.");

                    event.getChannel().sendMessage(userEmbed.build()).queue();
                    return;
                }

                // Execute this command
                cmd.executeCommand(new CommandEvent(bot, event));
            } catch (PermissionException ex) {
                // Send permission exception log to console
                System.out.println("Error Running Command: " + cmd.getName() +
                        "\nGuild: " + event.getGuild().getName() +
                        "\nAuthor: " + event.getAuthor().getAsTag() +
                        "\nPermission: " + ex.getPermission());
            }
        }
    }
}
