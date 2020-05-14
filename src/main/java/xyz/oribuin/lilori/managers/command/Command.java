package xyz.oribuin.lilori.managers.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.lilori.LilOri;

public class Command extends ListenerAdapter {

    public final LilOri bot;
    protected String name;
    protected String description;
    protected String[] arguments;
    protected String[] aliases;
    protected boolean enabled = true;
    protected boolean ownerOnly = false;
    protected Permission[] userPermissions;
    protected Permission[] botPermissions;
    public Command() {
        this.bot = LilOri.getInstance();
    }

    public void executeCommand(CommandEvent event) {
        // Unused
    }

    public void setUserPermission(Permission[] permissions) {
        this.userPermissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        //this.arguments = arguments;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOwnerOnly() {
        return ownerOnly;
    }

    public void setOwnerOnly(boolean ownerOnly) {
        this.ownerOnly = ownerOnly;
    }

    public Permission[] getUserPermissions() {
        return userPermissions;
    }

    public Permission[] getBotPermissions() {
        return botPermissions;
    }

    public void setBotPermissions(Permission[] permissions) {
        this.botPermissions = permissions;
    }
}
