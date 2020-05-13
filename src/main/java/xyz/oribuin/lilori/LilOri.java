package xyz.oribuin.lilori;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.commands.CmdCommand;
import xyz.oribuin.lilori.commands.CmdHelp;
import xyz.oribuin.lilori.commands.CmdPing;
import xyz.oribuin.lilori.commands.administrative.CmdPerms;
import xyz.oribuin.lilori.commands.author.*;
import xyz.oribuin.lilori.commands.games.*;
import xyz.oribuin.lilori.commands.moderation.CmdBan;
import xyz.oribuin.lilori.commands.moderation.CmdKick;
import xyz.oribuin.lilori.commands.moderation.CmdMute;
import xyz.oribuin.lilori.commands.moderation.CmdPurge;
import xyz.oribuin.lilori.commands.music.*;
import xyz.oribuin.lilori.database.DatabaseConnector;
import xyz.oribuin.lilori.database.SQLiteConnector;
import xyz.oribuin.lilori.listeners.EventMentionOri;
import xyz.oribuin.lilori.listeners.Presence;
import xyz.oribuin.lilori.managers.commands.command.CommandClient;
import xyz.oribuin.lilori.managers.commands.command.CommandClientBuilder;
import xyz.oribuin.lilori.managers.commands.commons.waiter.EventWaiter;
import xyz.oribuin.lilori.managers.music.GuildMusicManager;
import xyz.oribuin.lilori.managers.music.TrackManager;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class LilOri extends ListenerAdapter {

    private static LilOri instance;
    private DatabaseConnector connector;

    private LilOri() throws LoginException {
        instance = this;

        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder cmdBuilder = new CommandClientBuilder();

        // Define Command Builder
        cmdBuilder.setOwnerId("345406020450779149");
        cmdBuilder.setPrefix(";");
        cmdBuilder.useHelpBuilder(false);
        cmdBuilder.setEmojis("<:tick:682145393898815536>", ":warning: ", "<cross:682145379281666049>");
        cmdBuilder.useStatus(false);
        addCommands(cmdBuilder, waiter);
        CommandClient client = cmdBuilder.build();

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

        // Login Bot
        JDABuilder.createDefault(Settings.TOKEN)
                .addEventListeners(waiter, client,
                        new Presence(),
                        new EventMentionOri()
                ).build();

        // Load Music Managers
        TrackManager trackManager = new TrackManager();
        GuildMusicManager musicManager = new GuildMusicManager(trackManager.playerManager);
        musicManager.player.setVolume(100);

        // Startup Message
        PrintStream system = System.out;
        system.println("***********************");
        system.println(" ");
        system.println("Bot Loaded: Lil' Ori");
        system.println("Version: v1.0.0");
        system.println("Author: Oribuin");
        system.println(" ");
        system.println("***********************");
    }

    private void addCommands(CommandClientBuilder commandBuilder, EventWaiter waiter) {
        commandBuilder.addCommands(
                new CmdEval(),
                new CmdTest(),
                new CmdQuote(),
                new CmdQuery(),

                new CmdHelp(waiter),
                new CmdCommand(),
                new CmdPing(),
                new CmdPerms(waiter),

                new CmdCoinflip(),
                new CmdEightball(),
                new CmdGay(),
                new CmdSlap(),
                new CmdFeed(),

                new CmdVolume(),
                new CmdPause(),
                new CmdSkip(),
                new CmdGetTrack(),
                new CmdClear(),
                new CmdPlay(),
                new CmdStop(),

                new CmdBan(),
                new CmdKick(),
                new CmdMute(),
                new CmdPurge(waiter),

                new CmdPresence(waiter),
                new CmdShutdown()
        );
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

    public DatabaseConnector getConnector() {
        return connector;
    }
}