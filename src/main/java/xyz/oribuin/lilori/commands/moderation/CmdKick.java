package xyz.oribuin.lilori.commands.moderation;

import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.persist.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CmdKick extends Command {
    public CmdKick() {
        this.name = "Kick";
        this.help = "Kick a member from the guild.";
        this.cooldown = 2;
        this.category = new Command.Category("Moderation");
        this.guildOnly = true;
        this.arguments = "[@User] [Reason]";

        this.botPermissions = new Permission[]{Permission.KICK_MEMBERS, Permission.MESSAGE_MANAGE};
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS, Permission.MESSAGE_MANAGE};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length <= 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Correct Format: ;kick [@User] [Reason]", 10, TimeUnit.SECONDS);
            return;
        }

        if (event.getMessage().getMentionedMembers().get(0) == null) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Please include a user to kick.", 10, TimeUnit.SECONDS);
            return;
        }

        Member mentioned = event.getMessage().getMentionedMembers().get(0);

        if (mentioned.isOwner() || event.isHigher(mentioned, event.getGuild().getMember(event.getAuthor())) || event.isBot(mentioned)) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", You cannot kick this user due to rank hierarchy.", 10, TimeUnit.SECONDS);
            return;
        }

        String reason = event.getMessage().getContentRaw().substring(args[0].length() + 1 + args[1].length() + 1);

        event.kickMember(mentioned.getUser(), reason);

        event.deleteCmd();
        event.reply(event.getAuthor().getAsMention() + ", You have kicked `" + mentioned.getUser().getAsTag() + "`");
    }
}