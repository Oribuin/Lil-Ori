package xyz.oribuin.lilori.commands.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CmdMute extends Command {
    public CmdMute() {
        this.name = "Mute";
        this.help = "Mute a member from talking in the guild";
        this.cooldown = 2;
        this.category = new Command.Category("Moderation");
        this.guildOnly = true;
        this.arguments = "[Setup/@User]";
        this.disabled = true;

        this.botPermissions = new Permission[]{Permission.MANAGE_ROLES, Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS};
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES, Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length < 2) {
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.timedReply(event.getAuthor().getAsMention() + ", Correct Format: ;mute [Setup/@User]", 10, TimeUnit.SECONDS);
            return;
        }

        if (!event.getMessage().getMentionedMembers().isEmpty() && !args[1].equalsIgnoreCase("Setup")) {
            // mute system

            if (event.getGuild().getRolesByName("Muted", true).size() == 0) {
                event.deleteCmd(10, TimeUnit.SECONDS);
                event.timedReply(event.getAuthor().getAsMention() + ", The mute command has not been setup.", 10, TimeUnit.SECONDS);
                return;
            }

            Role role = event.getGuild().getRolesByName("Muted", true).get(0);
            Member mentioned = event.getMessage().getMentionedMembers().get(0);

            if (mentioned.isOwner() || event.isHigher(mentioned, event.getGuild().getMember(event.getAuthor())) || event.isBot(mentioned)) {
                event.deleteCmd(10, TimeUnit.SECONDS);
                event.timedReply(event.getAuthor().getAsMention() + ", You cannot kick this user due to rank hierarchy.", 10, TimeUnit.SECONDS);
                return;
            }

            if (mentioned.getRoles().contains(role)) {
                event.deleteCmd(10, TimeUnit.SECONDS);
                event.timedReply(event.getAuthor().getAsMention() + ", This user is already muted.", 10, TimeUnit.SECONDS);
                return;
            }


            event.muteMember(event.getGuild().getMember(event.getAuthor()), role);
            event.deleteCmd(10, TimeUnit.SECONDS);
            event.reply(event.getAuthor().getAsMention() + ", You have muted " + mentioned.getUser().getAsTag() + ".");
        }

        if (args[1].equalsIgnoreCase("Setup")) {
            if (event.getGuild().getRolesByName("Muted", true).size() == 0) {
                // create role

                event.getGuild().createRole()
                        .setName("Muted")
                        .setHoisted(true)
                        .setMentionable(false)
                        .setPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY, Permission.VOICE_CONNECT, Permission.VOICE_CONNECT).queue();

                EmbedBuilder notification = new EmbedBuilder()
                        .setAuthor("Created Role")

                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("Successfully Created @Muted Role");

                event.deleteCmd(10, TimeUnit.SECONDS);
                event.reply(notification.build());
                return;
            }

            Role role = event.getGuild().getRolesByName("Muted", true).get(0);

            if (event.getGuild().getRolesByName("Muted", true).size() >= 1) {


                event.getGuild().getChannels().forEach(guildChannel -> {
                    if (guildChannel.getPermissionOverrides().toString().contains(role.getId())) {
                        guildChannel.upsertPermissionOverride(role)
                                .setDeny(Permission.ALL_PERMISSIONS)
                                .setAllow(role.getPermissions()).queue();
                    } else {
                        guildChannel.createPermissionOverride(role)
                                .setDeny(Permission.EMPTY_PERMISSIONS)
                                .setAllow(role.getPermissions()).queue();
                    }
                });

                int channelAmt = (int) event.getGuild().getChannels().stream().filter(guildChannel -> !role.hasPermission(guildChannel)).count();

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor("Setup Mute Command")
                        .setColor(Color.decode("#CCA8DB"))
                        .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                        .setDescription("Successfully Setup the Mute Command for " + channelAmt + "/" + event.getGuild().getChannels().size() + " Channels");

                event.deleteCmd(10, TimeUnit.SECONDS);
                event.reply(embedBuilder.build());
            }

        }
    }
}