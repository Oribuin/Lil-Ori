package xyz.oribuin.lilori.utilities.command;

import net.dv8tion.jda.api.entities.Guild;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

public interface CommandClient {
    String getPrefix();

    String getAltPrefix();

    String getTexturalPrefix();

    void addCommand(Command command);

    void addCommand(Command command, int index);

    void removeCommand(String name);

    void addAnnotatedModule(Object module);

    void addAnnotatedModule(Object module, Function<Command, Integer> mapFunction);

    void setListener(CommandListener listener);

    CommandListener getListener();

    List<Command> getCommands();

    OffsetDateTime getStartTime();

    OffsetDateTime getCooldown(String name);

    int getRemainingCooldown(String name);

    void applyCooldown(String name, int seconds);

    void clearCooldowns();

    int getCommandUses(Command command);

    int getCommandUses(String name);

    String getOwnerId();

    long getOwnerIdLong();

    String[] getCoOwnerIds();

    long[] getCoOwnerIdsLong();

    String getSuccess();

    String getWarning();

    String getError();

    ScheduledExecutorService getScheduleExecutor();

    String getServerInvite();

    int getTotalGuilds();

    String getHelpWorld();

    boolean usesLinkedDeletion();

    <S> S getSettingsFor(Guild guild);

    <M extends GuildSettingsManager> M getSettingsManager();

    void shutdown();
}
