package xyz.oribuin.lilori.commands.author;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

import java.util.concurrent.TimeUnit;

public class CmdTest extends Command {
    private EventWaiter waiter;

    public CmdTest(EventWaiter waiter) {
        this.name = "Test";
        this.help = "A test command.";
        this.category = new Category("Test");
        this.arguments = "[None]";
        this.hidden = true;
        this.ownerCommand = false;
        this.waiter = waiter;

        this.botPermissions = new Permission[]{Permission.VIEW_CHANNEL, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_ADD_REACTION};
        this.userPermissions = new Permission[]{Permission.VIEW_CHANNEL, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_ADD_REACTION};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(String.valueOf(event.getGuildMember(event.getAuthor()).getPermissions().toString()));
        event.react(event.getMessage(), "a:eyes_blink:646521182715379722");
    }
}
