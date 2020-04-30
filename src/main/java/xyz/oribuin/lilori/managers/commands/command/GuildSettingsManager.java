package xyz.oribuin.lilori.managers.commands.command;

import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nullable;

public interface GuildSettingsManager<T> {
    @Nullable
    T getSettings(Guild guild);

    default void init() {
    }

    default void shutdown() {
    }
}
