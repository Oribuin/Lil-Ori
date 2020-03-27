package xyz.oribuin.lilori.commands;

import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;
import xyz.oribuin.lilori.utilities.menu.Paginator;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CmdHelp extends Command {
    private final Paginator.Builder pbuilder;
    private CommandEvent commandEvent;

    public CmdHelp(EventWaiter waiter) {
        this.name = "Help";
        this.help = "Get the list of commands for the bot.";
        this.category = new Category("Info");
        this.arguments = "[Page-Number]";

        pbuilder = new Paginator.Builder().setColumns(1)
                .setItemsPerPage(10)
                .showPageNumbers(true)
                .waitOnSinglePage(true)
                .useNumberedItems(false)
                .setFinalAction(m -> {
                    try {
                        m.clearReactions().queue();
                    } catch (Exception ex) {
                        try {
                            m.delete().queue();
                        } catch (Exception ignored) {
                        }
                    }
                })
                .setEventWaiter(waiter)
                .setTimeout(2, TimeUnit.MINUTES);
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        int page = 1;

        if (args.length == 2) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                event.reply(event.getClient().getWarning() + " Please input a correct page number.");
                return;
            }
        }

        //Arrays.asList(command.getUserPermissions()).containsAll(event.getGuild().getMember(event.getAuthor()).getPermissions()))

        pbuilder.clearItems();
        event.getClient().getCommands().stream()
                .filter(command -> !command.isOwnerCommand()
                        && !command.isHidden()
                        && !command.isWhitelisted(event.getGuild().getId())
                        && event.getSelfMember().getPermissions().containsAll(Arrays.asList(command.getBotPermissions())))
                .map(command -> "(`" + command.getCategory().getName() + "`) **" + command.getName() + "** " + command.getArguments() + " - " + command.getHelp())
                .forEach(pbuilder::addItems);

        Paginator p = pbuilder.setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.black)
                .setText(event.getClient().getSuccess() + "**Command List** ")
                .setUsers(event.getAuthor())
                .setColor(Color.decode("#cca8db"))
                .build();
        p.paginate(event.getChannel(), page);
    }
}
