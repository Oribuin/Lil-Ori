package xyz.oribuin.lilori.utilities.menu;

import net.dv8tion.jda.api.entities.*;
import xyz.oribuin.lilori.utilities.commons.waiter.EventWaiter;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class Menu {
    protected final EventWaiter waiter;
    protected Set<User> users;
    protected Set<Role> roles;
    protected final long timeout;
    protected final TimeUnit unit;

    protected Menu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit) {
        this.waiter = waiter;
        this.users = users;
        this.roles = roles;
        this.timeout = timeout;
        this.unit = unit;
    }

    public abstract void display(Message message);

    protected boolean isValidUser(User user) {
        return isValidUser(user, null);
    }

    protected boolean isValidUser(User user, @Nullable Guild guild) {
        if (user.isBot())
            return false;
        if (users.isEmpty() && roles.isEmpty())
            return true;
        if (users.contains(user))
            return true;
        if (guild == null || !guild.isMember(user))
            return false;

        return guild.getMember(user).getRoles().stream().anyMatch(roles::contains);
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<T extends Builder<T, V>, V extends Menu> {
        protected EventWaiter waiter;
        protected Set<User> users = new HashSet<>();
        protected Set<Role> roles = new HashSet<>();
        protected long timeout = 1;
        protected TimeUnit unit = TimeUnit.MINUTES;

        public abstract V build();

        public final T setEventWaiter(EventWaiter waiter) {
            this.waiter = waiter;
            return (T) this;
        }

        public final T addUsers(User... users) {
            this.users.addAll(Arrays.asList(users));
            return (T) this;
        }

        public final T setUsers(User... users) {
            this.users.clear();
            this.users.addAll(Arrays.asList(users));
            return (T) this;
        }

        public final T addRoles(Role... roles) {
            this.roles.addAll(Arrays.asList(roles));
            return (T) this;
        }

        public final T setRoles(Role... roles) {
            this.roles.clear();
            this.roles.addAll(Arrays.asList(roles));
            return (T) this;
        }

        public final T setTimeout(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.unit = unit;
            return (T) this;
        }
    }
}
