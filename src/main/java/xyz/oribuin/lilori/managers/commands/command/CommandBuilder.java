package xyz.oribuin.lilori.managers.commands.command;

import net.dv8tion.jda.api.Permission;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommandBuilder {
    private String name = "None";
    private String description = "No description defined.";
    private boolean enabled = true;
    private Command.Category category = null;
    private String arguments = null;
    private boolean guildOnly = true;
    private String requiredRole = null;
    private boolean ownerCommand = false;
    private int cooldown = 0;
    private Permission[] userPermissions = new Permission[0];
    private Permission[] botPermissions = new Permission[0];
    private final LinkedList<String> aliases = new LinkedList<>();
    private final LinkedList<Command> children = new LinkedList<>();
    private BiConsumer<CommandEvent, Command> helpBiConsumer = null;
    private boolean usesTopicTags = true;
    private Command.CooldownScope cooldownScope = Command.CooldownScope.USER;
    private boolean hidden = false;
    private String guildId = null;

    public CommandBuilder setName(String name) {
        if (name == null)
            this.name = "null";
        else
            this.name = name;
        return null;
    }

    public CommandBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public CommandBuilder setDescription(String description) {
        if (description == null)
            this.description = "No description defined.";
        else
            this.description = description;
        return this;
    }

    public CommandBuilder setCategory(Command.Category category) {
        this.category = category;
        return this;
    }

    public CommandBuilder setArguments(String arguments) {
        this.arguments = arguments;
        return this;
    }

    public CommandBuilder setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
        return this;
    }

    public CommandBuilder setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
        return this;
    }

    public CommandBuilder setOwnerCommand(boolean ownerCommand) {
        this.ownerCommand = ownerCommand;
        return this;
    }

    public CommandBuilder setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public CommandBuilder setUserPermissions(Permission... userPermissions) {
        if (userPermissions == null)
            this.userPermissions = new Permission[0];
        else
            this.userPermissions = userPermissions;
        return this;
    }

    public CommandBuilder setBotPermissions(Permission... botPermissions) {
        if (botPermissions == null)
            this.botPermissions = new Permission[0];
        else
            this.botPermissions = userPermissions;
        return this;
    }


    public CommandBuilder addAliases(String... aliases) {
        for (String alias : aliases)
            addAliases(alias);
        return this;
    }

    public CommandBuilder addChild(Command child) {
        children.add(child);
        return this;
    }

    public CommandBuilder addChildren(Command... children) {
        for (Command child : children)
            addChild(child);
        return this;
    }

    public CommandBuilder setChildren(Collection<Command> children) {
        this.children.clear();
        if (children != null)
            this.children.addAll(children);
        return this;
    }

    public CommandBuilder setHelpBiConsumer(BiConsumer<CommandEvent, Command> helpBiConsumer) {
        this.helpBiConsumer = helpBiConsumer;
        return this;
    }

    public CommandBuilder setUsesTopicTags(boolean usesTopicTags) {
        this.usesTopicTags = usesTopicTags;
        return this;
    }

    public CommandBuilder setCooldownScope(Command.CooldownScope cooldownScope) {
        if (cooldownScope == null)
            this.cooldownScope = Command.CooldownScope.USER;
        else
            this.cooldownScope = cooldownScope;
        return this;
    }

    public CommandBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public CommandBuilder setGuildId(String id) {
        this.guildId = id;
        return null;
    }

    public Command build(Consumer<CommandEvent> execution) {
        return build((c, e) -> execution.accept(e));
    }

    public Command build(BiConsumer<Command, CommandEvent> execution) {
        return new BlankCommand(name, description, enabled, category, arguments,
                guildOnly, requiredRole, ownerCommand, cooldown,
                userPermissions, botPermissions, aliases.toArray(new String[0]),
                children.toArray(new Command[0]), helpBiConsumer, usesTopicTags,
                cooldownScope, hidden, guildId) {

            @Override
            protected void execute(CommandEvent event) {
                execution.accept(this, event);
            }
        };
    }

    private abstract static class BlankCommand extends Command {
        BlankCommand(String name, String description, boolean enabled, Category category, String arguments, boolean guildOnly, String requiredRole,
                     boolean ownerCommand, int cooldown, Permission[] userPermissions,
                     Permission[] botPermissions, String[] aliases, Command[] children,
                     BiConsumer<CommandEvent, Command> helpBiConsumer,
                     boolean usesTopicTags, CooldownScope cooldownScope, boolean hidden, String guildId) {

            this.name = name;
            this.description = description;
            this.enabled = enabled;
            this.category = category;
            this.arguments = arguments;
            this.guildOnly = guildOnly;
            this.requiredRole = requiredRole;
            this.ownerCommand = ownerCommand;
            this.cooldown = cooldown;
            this.userPermissions = userPermissions;
            this.botPermissions = botPermissions;
            this.aliases = aliases;
            this.children = children;
            this.helpBiConsumer = helpBiConsumer;
            this.usesTopicTags = usesTopicTags;
            this.cooldownScope = cooldownScope;
            this.hidden = hidden;
            this.guildId = guildId;
        }
    }
}
