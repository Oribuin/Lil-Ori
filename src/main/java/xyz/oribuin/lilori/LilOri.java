package xyz.oribuin.lilori;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.commands.CmdCommand;
import xyz.oribuin.lilori.commands.CmdHelp;
import xyz.oribuin.lilori.commands.CmdPing;
import xyz.oribuin.lilori.commands.CmdPrefix;
import xyz.oribuin.lilori.commands.administrative.CmdPerms;
import xyz.oribuin.lilori.commands.author.CmdEval;
import xyz.oribuin.lilori.commands.author.CmdQuery;
import xyz.oribuin.lilori.commands.author.CmdShutdown;
import xyz.oribuin.lilori.commands.author.CmdTest;
import xyz.oribuin.lilori.commands.games.*;
import xyz.oribuin.lilori.commands.moderation.CmdBan;
import xyz.oribuin.lilori.commands.moderation.CmdKick;
import xyz.oribuin.lilori.commands.moderation.CmdMute;
import xyz.oribuin.lilori.commands.moderation.CmdPurge;
import xyz.oribuin.lilori.commands.music.CmdPlay;
import xyz.oribuin.lilori.commands.music.CmdStop;
import xyz.oribuin.lilori.database.DatabaseConnector;
import xyz.oribuin.lilori.database.SQLiteConnector;
import xyz.oribuin.lilori.listeners.EventMentionOri;
import xyz.oribuin.lilori.listeners.Presence;
import xyz.oribuin.lilori.managers.GuildSettingsManager;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandExecutor;
import xyz.oribuin.lilori.managers.command.CommandHandler;
import xyz.oribuin.lilori.utils.EventWaiter;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class LilOri extends ListenerAdapter {

    private EventWaiter waiter = new EventWaiter();
    private static LilOri instance;
    private DatabaseConnector connector;
    private GuildSettingsManager guildSettingsManager;
    private CommandHandler commandHandler;

    private LilOri() throws LoginException {
        instance = this;

        // Setup the SQLite Database
        File file = new File("data", "lilori.db");
        try {
            if (!file.exists()) {
                file.createNewFile();

                System.out.println("Created SQLite Database File: lilori.db");
            }

            // Register SQLite Connector
            this.connector = new SQLiteConnector(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup Managers
        this.guildSettingsManager = new GuildSettingsManager(this);
        this.commandHandler = new CommandHandler();

        this.registerCommands();
        enable();

        // Login Bot
        JDA jda = JDABuilder.createDefault(Settings.TOKEN)
                .addEventListeners(waiter, new CommandExecutor(this, commandHandler),
                        new Presence(),
                        new EventMentionOri()
                ).build();


        System.out.println("*=* Loading Lil' Ori Commands *=*");
        // Startup Message
        int i = 0;
        for (Command command : this.getCommandHandler().getCommands()) {

            System.out.println("Loaded Command: " + command.getName() + " | (" + ++i + "/" + this.getCommandHandler().getCommands().size() + ")");
        }

        System.out.println("*=* Loaded Up " + jda.getSelfUser().getName() + " with " + this.getCommandHandler().getCommands().size() + " Command(s) *=*");
    }

    public static void main(String... args) {
        try {
            new LilOri();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static LilOri getInstance() {
        return instance;
    }

    private void registerCommands() {
        this.getCommandHandler().registerCommands(
                // General Commands
                new CmdHelp(),
                new CmdPing(),
                new CmdPrefix(),
                new CmdCommand(),

                // Music Commands
                new CmdPlay(),
                new CmdStop(),

                // Game Commands
                new CmdCoinflip(),
                new CmdColor(),
                new CmdEightball(),
                new CmdFeed(),
                new CmdGay(),
                new CmdQuote(),
                new CmdSlap(),

                // Moderation
                new CmdBan(),
                new CmdKick(),
                new CmdMute(),
                new CmdPurge(this.waiter),

                // Author
                new CmdEval(),
                new CmdQuery(),
                new CmdShutdown(),
                new CmdTest(),

                // Admin
                new CmdPerms()
        );
    }

    private void enable() {
        this.guildSettingsManager.enable();
    }

    public DatabaseConnector getConnector() {
        return connector;
    }

    public GuildSettingsManager getGuildSettingsManager() {
        return guildSettingsManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}