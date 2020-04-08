package xyz.oribuin.lilori.commands.author;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import org.json.JSONObject;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

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
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("No Test Command Enabled");
    }
}
