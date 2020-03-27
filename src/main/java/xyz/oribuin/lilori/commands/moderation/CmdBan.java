package xyz.oribuin.lilori.commands.moderation;

import net.dv8tion.jda.api.entities.Member;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.persist.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CmdBan extends Command {
    public CmdBan() {
        this.name = "Ban";
        this.aliases = new String[]{"Expel"};
        this.help = "Ban a member from entering your guild";
        this.cooldown = 2;
        this.category = new Command.Category("Moderation");
        this.guildOnly = true;
        this.arguments = "[@User] [Messages] [Reason]";

        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.BAN_MEMBERS};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length <= 3) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Correct Format: ;ban [@User] [Messages] [Reason]", 10, TimeUnit.SECONDS);
            return;
        }

        if (event.getMessage().getMentionedMembers().get(0) == null) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please include a user to ban.", 10, TimeUnit.SECONDS);
            return;
        }

        Member mentioned = event.getMessage().getMentionedMembers().get(0);

        if (mentioned.isOwner() || event.isHigher(mentioned, event.getGuild().getMember(event.getAuthor())) || event.isBot(mentioned)) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", You cannot ban this user due to rank hierarchy.", 10, TimeUnit.SECONDS);
            return;
        }

        int messages;
        String reason = event.getMessage().getContentRaw().substring(args[0].length() + 1 + args[1].length() + 1 + args[2].length() + 1);

        try {
            messages = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please include a correct number of messages.", 10, TimeUnit.SECONDS);
            return;
        }

        event.banMember(mentioned.getUser(), messages, reason);

        event.deleteCmd();
        event.reply(event.getAuthor().getAsMention() + ", You have banned `" + mentioned.getUser().getAsTag() + "`");
    }
}