package xyz.oribuin.lilori.managers.commands.command.annotation;

import net.dv8tion.jda.api.Permission;
import xyz.oribuin.lilori.managers.commands.command.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JDACommand {

    String[] name() default {"null"};

    String description() default "No description defined.";

    boolean guildOnly() default false;

    String requiredRole() default "";

    boolean ownerCommand() default false;

    String arguments() default "";

    Cooldown cooldown() default @Cooldown(0);

    Permission[] botPermissions() default {};

    Permission[] userPermissions() default {};

    boolean useTopicTags() default false;

    String[] children() default {};

    boolean isHidden() default false;

    Category category() default @Category(name = "null", location = Category.class);

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Module {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Cooldown {
        int value();

        Command.CooldownScope scope() default Command.CooldownScope.USER;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Category {
        String name();

        Class<?> location();
    }
}
