package xyz.oribuin.lilori.utilities.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.oribuin.lilori.utilities.command.AnnotatedModuleCompiler;
import xyz.oribuin.lilori.utilities.command.Command;
import xyz.oribuin.lilori.utilities.command.CommandBuilder;
import xyz.oribuin.lilori.utilities.command.CommandEvent;
import xyz.oribuin.lilori.utilities.command.annotation.JDACommand;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AnnotatedModuleCompilerImpl implements AnnotatedModuleCompiler {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedModuleCompiler.class);

    @Override
    public List<Command> compile(Object o) {
        JDACommand.Module module = o.getClass().getAnnotation(JDACommand.Module.class);
        if (module == null)
            throw new IllegalArgumentException("Object provided is not annoated with JDACommand.Module!");
        if (module.value().length < 1)
            throw new IllegalArgumentException("Object provided is annotated with an empty command module!");

        List<Method> commands = collect((Method method) -> {
            for (String name : module.value()) {
                if (name.equalsIgnoreCase(method.getName()))
                    return true;
            }
            return false;
        }, o.getClass().getMethods());

        List<Command> list = new ArrayList<>();
        commands.forEach(method -> {
            try {
                list.add(compileMethod(o, method));
            } catch (MalformedParametersException e) {
                LOG.error(e.getMessage());
            }
        });
        return list;
    }

    private Command compileMethod(Object o, Method method) throws MalformedParametersException {
        JDACommand properties = method.getAnnotation(JDACommand.class);

        if (properties == null)
            throw new IllegalArgumentException("Method named" + method.getName() + " is not annotated with JDACommand!");

        CommandBuilder builder = new CommandBuilder();

        String[] names = properties.name();
        builder.setName(names.length < 1 ? "null" : names[0]);

        if (names.length > 1)
            for (int i = 1; i < names.length; i++)
                builder.addAliases(names[i]);

        builder.setHelp(properties.help());

        builder.setArguments(properties.arguments().trim().isEmpty() ? null : properties.arguments().trim());

        if (!properties.category().location().equals(JDACommand.Category.class)) {
            JDACommand.Category category = properties.category();

            for (Field field : category.location().getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(Command.Category.class)) {
                    if (category.name().equalsIgnoreCase(field.getName())) {
                        try {
                            builder.setCategory((Command.Category) field.get(null));
                        } catch (IllegalAccessException e) {
                            LOG.error("Encountered Exception ", e);
                        }
                    }
                }
            }
        }

        builder.setGuildOnly(properties.guildOnly());

        builder.setRequiredRole(properties.requiredRole().trim().isEmpty() ? null : properties.requiredRole().trim());

        builder.setOwnerCommand(properties.ownerCommand());

        builder.setCooldown(properties.cooldown().value());

        builder.setCooldownScope(properties.cooldown().scope());

        builder.setBotPermissions(properties.botPermissions());

        builder.setUserPermissions(properties.userPermissions());

        builder.setUsesTopicTags(properties.useTopicTags());

        builder.setHidden(properties.isHidden());

        if (properties.children().length > 0) {
            collect((Method m) -> {
                for (String cName : properties.children()) {
                    if (cName.equalsIgnoreCase(m.getName()))
                        return true;
                }
                return false;
            }, o.getClass().getMethods()).forEach(cm -> {
                try {
                    builder.addChild(compileMethod(o, cm));
                } catch (MalformedParametersException e) {
                    LOG.error("Encountered Exception ", e);
                }
            });
        }

        Class<?>[] parameters = method.getParameterTypes();

        if (parameters[0] == Command.class && parameters[1] == CommandEvent.class) {
            return builder.build(((command, commandEvent) -> {
                try {
                    method.invoke(o, command, commandEvent);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOG.error("Encountered Exception ", e);
                }
            }));

        } else if (parameters[0] == CommandEvent.class) {
            if (parameters.length == 1) {
                return builder.build(commandEvent -> {
                    try {
                        method.invoke(o, commandEvent);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOG.error("Encountered Exception", e);
                    }
                });
            } else if (parameters[1] == Command.class) {
                return builder.build((command, commandEvent) -> {
                    try {
                        method.invoke(o, commandEvent, command);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOG.error("Encountered Exception", e);
                    }
                });
            }
        }

        throw new MalformedParametersException("Method named " + method.getName() +"  was not compiled due to improper parameter types");
    }

    @SafeVarargs
    private static <T> List<T> collect(Predicate<T> filter, T... entities) {
        List<T> list = new ArrayList<>();
        for (T entity : entities) {
            if (filter.test(entity))
                list.add(entity);
        }

        return list;
    }
}
