package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.data.CommandData;
import com.wizardlybump17.wlib.command.exception.CommandException;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.DirectCommandExtractor;
import com.wizardlybump17.wlib.command.extractor.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.holder.CommandHolder;
import com.wizardlybump17.wlib.command.registered.RegisteredCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandManager {

    private final @NotNull Map<String, Set<RegisteredCommand>> commands = new HashMap<>();
    protected final @NotNull CommandHolder<?> holder;
    private final @NotNull Set<CommandExtractor> commandExtractors = new HashSet<>();

    public CommandManager(@NotNull CommandHolder<?> holder) {
        this.holder = holder;
        commandExtractors.add(new MethodCommandExtractor());
        commandExtractors.add(new DirectCommandExtractor());
    }

    public void registerCommands(@NotNull Object @NotNull ... objects) {
        for (Object object : objects)
            for (CommandExtractor extractor : commandExtractors)
                registerCommands(extractor.extract(this, holder, object));
    }

    public void registerCommands(@NotNull Collection<RegisteredCommand> commands) {
        commands.forEach(this::registerCommand);
    }

    public void registerCommand(@NotNull RegisteredCommand command) {
        command.onRegister(this);
        commands.computeIfAbsent(command.getName().toLowerCase(), $ -> new TreeSet<>()).add(command);
    }

    public void unregisterCommands() {
        commands.forEach((name, commands) -> {
            commands.forEach(command -> command.onUnregister(this));
            commands.clear();
        });
        commands.clear();
    }

    public void execute(@NotNull CommandSender<?> sender, @NotNull String string) throws CommandException {
        if (commands.isEmpty())
            return;

        int spaceIndex = string.indexOf(' ');
        String name;
        if (spaceIndex == -1)
            name = string;
        else
            name = string.substring(0, spaceIndex);

        for (RegisteredCommand registeredCommand : commands.getOrDefault(name, Set.of())) {
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

    public @NotNull List<@NotNull String> autoComplete(@NotNull CommandSender<?> sender, @NotNull String string) {
        return List.of();
    }

    public @NotNull List<RegisteredCommand> getCommands(@NotNull String name) {
        Set<RegisteredCommand> commands = this.commands.get(name);
        return commands == null ? List.of() : List.copyOf(commands);
    }

    public @NotNull List<RegisteredCommand> getCommands(@NotNull Object object) {
        List<RegisteredCommand> result = new ArrayList<>(commands.size());
        for (Set<RegisteredCommand> commands : this.commands.values())
            for (RegisteredCommand command : commands)
                if (command.isOwnedBy(object))
                    result.add(command);
        return result;
    }

    public @NotNull CommandHolder<?> getHolder() {
        return holder;
    }

    public @NotNull Map<String, Set<RegisteredCommand>> getCommands() {
        return Map.copyOf(commands);
    }

    public @NotNull Set<CommandExtractor> getCommandExtractors() {
        return Set.copyOf(commandExtractors);
    }
}