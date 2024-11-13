package com.wizardlybump17.wlib.command.extractor;

import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.holder.CommandHolder;
import com.wizardlybump17.wlib.command.registered.RegisteredCommand;
import com.wizardlybump17.wlib.command.registered.RegisteredMethodCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MethodCommandExtractor implements CommandExtractor {

    @Override
    public @NotNull List<RegisteredCommand> extract(@NotNull CommandManager manager, @NotNull CommandHolder<?> holder, @NotNull Object object) {
        List<RegisteredCommand> commands = new ArrayList<>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class) || method.getParameterCount() == 0 || !CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))
                continue;

            RegisteredMethodCommand command;
            try {
                command = new RegisteredMethodCommand(
                        method.getAnnotation(Command.class),
                        object,
                        method
                );
            } catch (Exception e) {
                holder.getLogger().log(Level.SEVERE, "Error while creating a command", e);
                continue;
            }

            commands.add(command);
            com.wizardlybump17.wlib.command.holder.Command holderCommand = holder.getCommand(command.getName());
            if (holderCommand != null)
                holderCommand.setExecutor(holderCommand.getDefaultExecutor(manager, command.getName()));
        }
        return commands;
    }
}
