package xyz.oribuin.lilori.commands.administrative;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;
import xyz.oribuin.lilori.managers.commands.commons.waiter.EventWaiter;
import xyz.oribuin.lilori.managers.commands.menu.Paginator;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CmdPerms extends Command {
    private final Paginator.Builder pbuilder;

    public CmdPerms(EventWaiter waiter) {
        this.name = "Permissions";
        this.aliases = new String[]{"Perms"};
        this.help = "List of permissions the bot has.";
        this.guildOnly = true;
        this.category = new Category("Info");
        this.arguments = "<Page>";


        pbuilder = new Paginator.Builder().setColumns(1)
                .setItemsPerPage(16)
                .showPageNumbers(true)
                .waitOnSinglePage(true)
                .useNumberedItems(false)
                .setFinalAction(m -> {
                    try {
                        m.clearReactions().queue();
                    } catch (Exception ex) {
                        try {
                            m.delete().queue();
                        } catch (ErrorResponseException ignored) {
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
                event.reply(event.getClient().getWarning() + "Please input a correct page number.");
                return;
            }
        }

        pbuilder.clearItems();
        event.getGuild().getSelfMember().getPermissions().stream()
                .map(perm -> "**\\*** " + perm.getName().substring(0, 1).toUpperCase() + perm.getName().substring(1))
                .forEach(pbuilder::addItems);

        Paginator p = pbuilder.setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.black)
                .setText(event.getClient().getSuccess() + " **Lil' Ori Permissions | Total:** " + event.getGuild().getSelfMember().getPermissions().size())
                .setUsers(event.getAuthor())
                .setColor(Color.decode("#33539e"))
                .build();
        p.paginate(event.getChannel(), page);
    }
}
