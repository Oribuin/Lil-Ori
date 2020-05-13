package xyz.oribuin.lilori.managers.commands.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class Command {

    protected String name = "none";

    protected String description = "No description defined.";

    protected Category category;

    protected String arguments = null;

    protected boolean enabled = true;

    protected boolean guildOnly = false;

    protected String requiredRole = null;

    protected boolean ownerCommand = false;

    protected int cooldown = 0;

    protected Permission[] userPermissions = new Permission[0];

    protected Permission[] botPermissions = new Permission[0];

    protected String[] aliases = new String[0];

    protected Command[] children = new Command[0];

    protected BiConsumer<CommandEvent, Command> helpBiConsumer = null;

    protected boolean usesTopicTags = true;

    protected boolean hidden = false;

    protected CooldownScope cooldownScope = CooldownScope.USER;

    protected String guildId = null;

    EmbedBuilder botEmbed = new EmbedBuilder()
            .setAuthor("Missing Permissions!")
            .setColor(Color.decode("#33539e"))
            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
            .setDescription("I am missing the permissions to execute this command.");

    EmbedBuilder userEmbed = new EmbedBuilder()
            .setAuthor("Missing Permissions!")
            .setColor(Color.decode("#33539e"))
            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
            .setDescription("You are missing the permissions to execute this command.");

    EmbedBuilder nullEmbed = new EmbedBuilder()
            .setAuthor("Null Issue :(")
            .setColor(Color.decode("#33539e"))
            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
            .setDescription("Oh no! There was an issue where something as returned null, Please contact the developer!");

    protected abstract void execute(CommandEvent event);

    public final void run(CommandEvent event) {
        if (!event.getArgs().isEmpty()) {
            String[] parts = Arrays.copyOf(event.getArgs().split("\\s+", 2), 2);
            if (helpBiConsumer != null && parts[0].equals(event.getClient().getHelpWorld())) {
                helpBiConsumer.accept(event, this);
                return;
            }

            for (Command command : children) {
                if (command.isCommandFor(parts[0])) {
                    event.setArgs(parts[1] == null ? "" : parts[1]);
                    command.run(event);
                }
            }
        }

        if (guildId != null && !guildId.equalsIgnoreCase(event.getGuild().getId())) {
            terminate(event, null);
            return;
        }

        if (!enabled) {
            terminate(event, "This command is currently disabled.");
            return;
        }

        if (ownerCommand && !(event.isOwner())) {
            terminate(event, "Sorry! You don't have access to this command.");
            return;
        }

        if (category == null) {
            category = new Category("Undefined");
            terminate(event, null);
            return;
        }

        if (event.isFromType(ChannelType.TEXT) && isAllowed(event.getTextChannel())) {
            terminate(event, "Sorry! You cannot execute this command here.");
            return;
        }

        if (requiredRole != null)
            if (!event.isFromType(ChannelType.TEXT) || event.getMember().getRoles().stream().noneMatch(r -> r.getName().equalsIgnoreCase(requiredRole))) {
                terminate(event, "Sorry! You need **" + requiredRole + "** to use this command!");
                return;
            }

        if (event.getChannelType() == ChannelType.TEXT) {
            Arrays.stream(botPermissions).forEach(permission -> {
                if (permission.isChannel()) {
                    if (permission.getName().startsWith("VOICE")) {
                        VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();

                        if (event.getMember().getVoiceState() == null) return;
                        if (voiceChannel == null) {
                            terminate(event, "You need to be in a voice channel to use this command.");
                        }
                    }
                }
            });

            if (!event.getSelfMember().getPermissions().containsAll(Arrays.asList(botPermissions))) {
                embedTerminate(event, botEmbed);
                return;
            }

            if (!event.getMember().hasPermission(event.getTextChannel())
                    || !event.getMember().getPermissions().containsAll(Arrays.asList(userPermissions))) {
                embedTerminate(event, userEmbed);
                return;
            }

        } else if (guildOnly) {
            terminate(event, "Sorry! You cannot use this command in Private Messages.");
            return;
        }

        if (cooldown > 0) {
            String key = getCooldownKey(event);
            int remaining = event.getClient().getRemainingCooldown(key);
            if (remaining > 0) {
                String error = getCooldownError(event, remaining);
                if (error != null) {
                    terminate(event, error);
                    return;
                }
            } else event.getClient().applyCooldown(key, cooldown);
        }

        try {
            execute(event);
        } catch (
                Throwable t) {
            if (event.getClient().getListener() != null) {
                event.getClient().getListener().onCommandException(event, this, t);
                return;
            }
            throw t;
        }

        if (event.getClient().getListener() != null)
            event.getClient().getListener().onCompletedCommand(event, this);
    }

    public boolean isCommandFor(String input) {
        if (name.equalsIgnoreCase(input))
            return true;
        for (String alias : aliases)
            if (alias.equalsIgnoreCase(input))
                return true;
        return false;
    }

    public boolean isAllowed(TextChannel channel) {
        if (!usesTopicTags)
            return false;
        if (channel == null)
            return false;
        String topic = channel.getTopic();
        if (topic == null || topic.isEmpty())
            return false;
        topic = topic.toLowerCase();
        String lowerName = name.toLowerCase();
        if (topic.contains("{" + lowerName + "}"))
            return false;
        if (topic.contains("{-" + lowerName + "}"))
            return true;

        String lowerCategory = category == null ? null : category.getName().toLowerCase();
        if (lowerCategory != null) {
            if (topic.contains("{" + lowerCategory + "}"))
                return false;
            if (topic.contains("{-" + lowerCategory + "}"))
                return true;
        }
        return topic.contains("{-all}");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Category getCategory() {
        return category;
    }

    public String getArguments() {
        return arguments;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Permission[] getUserPermissions() {
        return userPermissions;
    }

    public Permission[] getBotPermissions() {
        return botPermissions;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Command[] getChildren() {
        return children;
    }

    public boolean isOwnerCommand() {
        return ownerCommand;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isWhitelisted(String id) {
        return guildId != null && !this.guildId.equals(id);
    }

    public String getGuildId() {
        return guildId;
    }

    private void terminate(CommandEvent event, String message) {

        if (ownerCommand)
            event.reply(message);

        if (event.isFromType(ChannelType.TEXT) && isAllowed(event.getTextChannel()))
            event.reply(message);

        if (requiredRole != null)
            if (!event.isFromType(ChannelType.TEXT) || event.getMember().getRoles().stream().noneMatch(r -> r.getName().equalsIgnoreCase(requiredRole))) {
                event.reply(message);
                return;
            }

        if (event.getClient().getListener() != null)
            event.getClient().getListener().onTerminatedCommand(event, this);
    }

    private void embedTerminate(CommandEvent event, EmbedBuilder embed) {
        event.reply(embed.build());

        if (event.getClient().getListener() != null)
            event.getClient().getListener().onTerminatedCommand(event, this);
    }

    public String getCooldownKey(CommandEvent event) {
        switch (cooldownScope) {
            case USER:
                return cooldownScope.genKey(name, event.getAuthor().getIdLong());
            case USER_GUILD:
                return event.getGuild() != null ? cooldownScope.genKey(name, event.getAuthor().getIdLong(), event.getGuild().getIdLong())
                        : CooldownScope.USER_CHANNEL.genKey(name, event.getAuthor().getIdLong(), event.getChannel().getIdLong());
            case USER_CHANNEL:
                return cooldownScope.genKey(name, event.getAuthor().getIdLong(), event.getChannel().getIdLong());
            case GUILD:
                return event.getGuild() != null ? cooldownScope.genKey(name, event.getGuild().getIdLong()) :
                        CooldownScope.CHANNEL.genKey(name, event.getChannel().getIdLong());
            case CHANNEL:
                return cooldownScope.genKey(name, event.getChannel().getIdLong());
            case SHARD:
                event.getJDA().getShardInfo();
                return cooldownScope.genKey(name, event.getJDA().getShardInfo().getShardId());
            case USER_SHARD:
                event.getJDA().getShardInfo();
                return cooldownScope.genKey(name, event.getAuthor().getIdLong(), event.getJDA().getShardInfo().getShardId());
            case GLOBAL:
                return cooldownScope.genKey(name, 0);
            default:
                return "";

        }
    }

    public String getCooldownError(CommandEvent event, int remaining) {
        if (remaining <= 0)
            return null;
        if (cooldownScope.equals(CooldownScope.USER))
            return "Please wait " + remaining + "s before trying again.";
        else if (cooldownScope.equals(CooldownScope.USER_GUILD) && event.getGuild() == null)
            return "Please wait " + remaining + "s before trying in this channel.";
        else if (cooldownScope.equals(CooldownScope.GUILD) && event.getGuild() == null)
            return "Please wait " + remaining + "s before trying in this channel.";
        else
            return "Please wait " + remaining + "s before trying again.";
    }

    public static class Category {
        private final String name;
        private final String failResponse;
        private final Predicate<CommandEvent> predicate;

        public Category(String name) {
            this.name = name;
            this.failResponse = null;
            this.predicate = null;
        }

        public Category(String name, Predicate<CommandEvent> predicate) {
            this.name = name;
            this.failResponse = null;
            this.predicate = predicate;
        }

        public Category(String name, Predicate<CommandEvent> predicate, String failResponse) {
            this.name = name;
            this.failResponse = failResponse;
            this.predicate = predicate;
        }

        public String getName() {
            return name;
        }

        public String getFailResponse() {
            return failResponse;
        }

        public boolean test(CommandEvent event) {
            return predicate == null || predicate.test(event);
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Category))
                return false;
            Category other = (Category) object;
            return Objects.equals(name, other.name) && Objects.equals(predicate, other.predicate) && Objects.equals(failResponse, other.failResponse);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.name);
            hash = 17 * hash + Objects.hashCode(this.failResponse);
            hash = 17 * hash + Objects.hashCode(this.predicate);
            return hash;
        }
    }

    public enum CooldownScope {
        USER("U:%d"),
        CHANNEL("C:%d"),
        USER_CHANNEL("U:%d|C:%d"),
        GUILD("G:%d"),
        USER_GUILD("U:%d|G:%d"),
        SHARD("S:%d"),
        USER_SHARD("U:%d|S:%d"),
        GLOBAL("Global");

        private final String format;

        CooldownScope(String format) {
            this.format = format;
        }

        String genKey(String name, long id) {
            return genKey(name, id, -1);
        }

        String genKey(String name, long idOne, long idTwo) {
            if (this.equals(GLOBAL))
                return name + "|" + format;
            else if (idTwo == -1)
                return name + "|" + String.format(format, idOne);
            else return name + "|" + String.format(format, idOne, idTwo);
        }
    }

}
