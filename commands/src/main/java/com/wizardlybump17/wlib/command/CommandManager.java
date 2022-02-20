package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.holder.CommandHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CommandManager {

    private final List<RegisteredCommand> commands = new ArrayList<>();
    protected final CommandHolder<?> holder;
    @Nullable
    private final CommandExecutorListener listener;

    public CommandManager(CommandHolder<?> holder) {
        this.holder = holder;
        this.listener = null;
    }

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
                    holderCommand.setDefaultExecutor(this);
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
            if (listener == null || listener.shouldExecute(sender, command))
                switch (command.execute(sender, string)) {
                    case SUCCESS:
                    case PERMISSION_FAIL:
                        return;
                    default:
                }
        }
    }

    public List<RegisteredCommand> getCommand(String name) {
        List<RegisteredCommand> commands = new ArrayList<>(this.commands.size());
        for (RegisteredCommand command : this.commands)
            if (command.getName().equalsIgnoreCase(name))
                commands.add(command);
        return commands;
    }
}