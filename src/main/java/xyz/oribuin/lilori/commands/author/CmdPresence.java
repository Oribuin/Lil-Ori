package xyz.oribuin.lilori.commands.author;

import net.dv8tion.jda.api.entities.Activity;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;
import xyz.oribuin.lilori.managers.commands.commons.waiter.EventWaiter;

public class CmdPresence extends Command {
    private final EventWaiter waiter;

    public CmdPresence(EventWaiter waiter) {
        this.name = "Presence";
        this.category = new Category("Author");
        this.aliases = new String[]{"Status"};
        this.arguments = "<OnlineStatus> <Activity> <String>";
        this.help = "Set bot activity..";
        this.ownerCommand = true;

        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length <= 2) {
            event.reply(event.getAuthor().getAsMention() + ", Correct Usage: ;" + this.name + " " + this.getArguments());
            return;
        }

        Activity.ActivityType activityType = Activity.ActivityType.valueOf(args[1].toUpperCase());
        String status = event.getMessage().getContentDisplay().substring(args[0].length() + args[1].length() + 2);

        event.getJDA().getPresence().setActivity(Activity.of(activityType, status));
        event.reply("Changed Activity: `event.getJDA().getPresence().setActivity(Activity.of(" + activityType.name().toLowerCase() + ", " + status + "));`");
        System.out.println(event.getAuthor().getAsTag() + " Changed the activity to Activity." + activityType + "(" + status + ")");
    }
}

