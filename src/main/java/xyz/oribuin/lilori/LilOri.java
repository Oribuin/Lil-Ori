package xyz.oribuin.lilori;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.commands.*;
import xyz.oribuin.lilori.commands.administrative.CmdPerms;
import xyz.oribuin.lilori.commands.author.CmdShutdown;
import xyz.oribuin.lilori.commands.author.CmdTest;
import xyz.oribuin.lilori.commands.moderation.CmdBan;
import xyz.oribuin.lilori.commands.moderation.CmdKick;
import xyz.oribuin.lilori.commands.moderation.CmdMute;
import xyz.oribuin.lilori.commands.moderation.CmdPurge;
import xyz.oribuin.lilori.commands.music.*;
import xyz.oribuin.lilori.listeners.EventMentionOri;
import xyz.oribuin.lilori.listeners.Presence;
import xyz.oribuin.lilori.utilities.command.CommandClient;
import xyz.oribuin.lilori.utilities.command.CommandClientBuilder;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

import javax.security.auth.login.LoginException;
import java.io.PrintStream;

public class LilOri extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {

        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder CmdBuilder = new CommandClientBuilder();

        CmdBuilder.setOwnerId("345406020450779149");
        CmdBuilder.setPrefix(";");
        CmdBuilder.useHelpBuilder(false);
        CmdBuilder.setEmojis("<:tick:682145393898815536>", ":warning: ", "<cross:682145379281666049>");


        CmdBuilder.addCommands(

                new CmdTest(waiter),
                /*
                 * Info Commands
                 */

                new CmdHelp(waiter),
                new CmdPerms(waiter),
                new CmdPing(),

                new CmdCoinflip(),
                new CmdEightball(),
                new CmdGay(waiter),
                new CmdSlap(),
                /*
                 * Music Commands
                 */

                new CmdVolume(),
                new CmdPause(),
                new CmdSkip(),
                new CmdGetTrack(),
                new CmdClear(),
                new CmdPlay(),
                new CmdStop(),
                /*
                 * Moderation Commands
                 */
                new CmdBan(),
                new CmdKick(),
                new CmdMute(),
                new CmdPurge(waiter),

                /*
                 * Author
                 */
                new CmdShutdown()
        );


        CommandClient Client = CmdBuilder.build();
        JDA jda = JDABuilder.createDefault(Settings.TOKEN)
                .addEventListeners(waiter, Client).addEventListeners(
                        new Presence(),
                        new EventMentionOri()

                ).build();

        PrintStream system = System.out;
        system.println("***********************");
        system.println("Bot Loaded: Lil' Ori");
        system.println("Version: v1.0.0");
        system.println("Author: Oribuin");
        system.println("***********************");

    }
}