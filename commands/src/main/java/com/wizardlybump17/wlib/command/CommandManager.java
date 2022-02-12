package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.holder.CommandHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Set<RegisteredCommand> registerCommands(Object... objects) {
        Set<RegisteredCommand> createdCommands = new HashSet<>();

        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Command.class) || method.getParameterCount() == 0 || !CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))
                    continue;

                method.setAccessible(true);

                final RegisteredCommand command = new RegisteredCommand(
                        method.getAnnotation(Command.class),
                        object,
                        method
                );
                commands.add(command);

                createdCommands.add(command);

                holder.getCommand(command.getName()).setDefaultExecutor(this);
            }
        }

        commands.sort(null);
        return createdCommands;
    }

    public void unregisterCommands() {
        commands.clear();
    }

    public void execute(CommandSender<?> sender, String string) {
        if (commands.isEmpty())
            return;

        for (RegisteredCommand command : commands) {
            if (listener != null && listener.shouldExecute(sender, command))
                switch (command.execute(sender, string)) {
                    case SUCCESS:
                    case PERMISSION_FAIL:
                        return;
                    default:
                        continue;
                }
        }
    }
}