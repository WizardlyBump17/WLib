package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.holder.CommandHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CommandManager {

    private final List<RegisteredCommand> commands = new ArrayList<>();
    private final CommandHolder<?> holder;

    public void registerCommands(Object... objects) {
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
                holder.onCommandCreate(this, command);
            }
        }

        commands.sort(null);
    }

    public void unregisterCommands() {
        for (RegisteredCommand command : commands)
            holder.onCommandDelete(this, command);
        commands.clear();
    }

    public void execute(CommandSender<?> sender, String string) throws Exception {
        if (commands.isEmpty())
            return;

        Exception lastException = null;
        for (RegisteredCommand command : commands) {
            try {
                command.execute(sender, string);
                return;
            } catch (Exception e) {
                lastException = e;
            }
        }

        if (lastException != null)
            throw lastException;
    }
}