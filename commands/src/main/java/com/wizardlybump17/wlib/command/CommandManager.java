package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.data.CommandData;
import com.wizardlybump17.wlib.command.exception.CommandException;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.DirectCommandExtractor;
import com.wizardlybump17.wlib.command.extractor.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.holder.CommandHolder;
import com.wizardlybump17.wlib.command.registered.RegisteredCommand;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

@Getter
public class CommandManager {

    private final List<RegisteredCommand> commands = new ArrayList<>();
    protected final CommandHolder<?> holder;
    private final @NonNull Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>();
    private final @NotNull Set<CommandExtractor> commandExtractors = new HashSet<>();

    public CommandManager(@NotNull CommandHolder<?> holder) {
        this.holder = holder;
        commandExtractors.add(new MethodCommandExtractor());
        commandExtractors.add(new DirectCommandExtractor());
    }

    public void registerCommands(Object... objects) {
        for (Object object : objects)
            for (CommandExtractor extractor : commandExtractors)
                commands.addAll(extractor.extract(this, holder, object));
        commands.sort(null);
    }

    public void registerCommands(@NotNull Collection<RegisteredCommand> commands) {
        this.commands.addAll(commands);
        this.commands.sort(null);
    }

    public void unregisterCommands() {
        commands.clear();
    }

    public void execute(CommandSender<?> sender, String string) throws CommandException {
        if (commands.isEmpty())
            return;

        for (RegisteredCommand registeredCommand : commands) {
            CommandData command = registeredCommand.getCommand();
            CommandResult result = registeredCommand.execute(sender, string);

            switch (result) {
                case PERMISSION_FAIL -> {
                    String message = command.getPermissionMessage();
                    if (message != null)
                        sender.sendMessage(message);
                    return;
                }

                case INVALID_SENDER -> {
                    String message = command.getInvalidSenderMessage();
                    if (message != null)
                        sender.sendMessage(message);
                    return;
                }

                case SUCCESS, EXCEPTION -> {
                    return;
                }
            }
        }
    }

    @NonNull
    public List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String string) {
        return List.of();
    }

    public List<RegisteredCommand> getCommand(String name) {
        List<RegisteredCommand> commands = new ArrayList<>(this.commands.size());
        for (RegisteredCommand command : this.commands)
            if (command.getName().equalsIgnoreCase(name))
                commands.add(command);
        return commands;
    }

    public List<RegisteredCommand> getCommands(@NotNull Object object) {
        List<RegisteredCommand> commands = new ArrayList<>(this.commands.size());
        for (RegisteredCommand command : this.commands)
            if (command.isOwnedBy(object))
                commands.add(command);
        return commands;
    }
}