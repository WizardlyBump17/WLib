package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import com.wizardlybump17.wlib.command.holder.CommandHolder;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CommandManager {

    public static final Map<CommandHolder, CommandManager> MANAGERS = new HashMap<>();

    private static CommandManagerListener createListener;

    private final List<RegisteredCommand> commands = new ArrayList<>();
    private final CommandHolder holder;

    public CommandManager(CommandHolder holder) {
        MANAGERS.put(this.holder = holder, this);
    }

    public void registerCommands(Object... objects) {
        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Command.class))
                    continue;
                if (method.getParameterCount() == 0)
                    continue;
                if (!CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))
                    continue;

                method.setAccessible(true);

                final RegisteredCommand command = new RegisteredCommand(
                        method.getAnnotation(Command.class),
                        object,
                        method
                );
                commands.add(command);
                createListener.onCommandCreate(this, command);
            }
        }

        commands.sort(null);
    }

    public void unregisterCommands() {
        for (RegisteredCommand command : commands)
            createListener.onCommandDelete(this, command);
        commands.clear();
    }

    public void execute(CommandSender<?> sender, String string) throws ArgsReaderException {
        if (commands.isEmpty())
            return;

        ArgsReaderException lastException = null;
        for (RegisteredCommand command : commands) {
            try {
                command.execute(sender, string);
                return;
            } catch (ArgsReaderException e) {
                lastException = e;
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (lastException != null)
            throw lastException;
    }

    public static void setCreateListener(CommandManagerListener listener) {
        if (createListener == null)
            createListener = listener;
    }

    public static CommandManagerListener getCreateListener() {
        return createListener;
    }
}
