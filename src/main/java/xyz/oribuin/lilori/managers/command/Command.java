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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isOwnerOnly() {
        return ownerOnly;
    }

    public Permission[] getUserPermissions() {
        return userPermissions;
    }

    public Permission[] getBotPermissions() {
        return botPermissions;
    }

}
