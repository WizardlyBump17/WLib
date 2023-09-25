package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.holder.CommandHolder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CommandManager {

    private final List<RegisteredCommand> commands = new ArrayList<>();
    protected final CommandHolder<?> holder;

    public void registerCommands(Object... objects) {
        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Command.class) || method.getParameterCount() == 0 || !CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))
                    continue;

                RegisteredCommand command = new RegisteredCommand(
                        method.getAnnotation(Command.class),
                        object,
                        method
                );

                commands.add(command);
                com.wizardlybump17.wlib.command.holder.Command holderCommand = holder.getCommand(command.getName());
                if (holderCommand != null)
                    holderCommand.setExecutor(holderCommand.getDefaultExecutor(this, command.getName()));
            }
        }

        commands.sort(null);
    }

    public void unregisterCommands() {
        commands.clear();
    }

    public void execute(CommandSender<?> sender, String string) {
        if (commands.isEmpty())
            return;

        for (RegisteredCommand command : commands) {
            CommandResult result = command.execute(sender, string);
            switch (result) {
                case SUCCESS, PERMISSION_FAIL -> {
                    return;
                }
            }
        }
    }

    @NonNull
    public List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String string) {
        List<String> result = new ArrayList<>();
        for (RegisteredCommand command : commands)
            result.addAll(command.autoComplete(sender, string));
        return result;
    }

    public List<RegisteredCommand> getCommand(String name) {
        List<RegisteredCommand> commands = new ArrayList<>(this.commands.size());
        for (RegisteredCommand command : this.commands)
            if (command.getName().equalsIgnoreCase(name))
                commands.add(command);
        return commands;
    }

    public List<RegisteredCommand> getCommands(Object object) {
        List<RegisteredCommand> commands = new ArrayList<>(this.commands.size());
        for (RegisteredCommand command : this.commands)
            if (command.getObject() == object)
                commands.add(command);
        return commands;
    }
}