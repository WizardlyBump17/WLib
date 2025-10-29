package com.wizardlybump17.wlib.command.rework.manager;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    private static final char SEPARATOR = ':';

    private final @NotNull Map<String, Command> commands = new HashMap<>();

    public @NotNull Command registerCommand(@NotNull String identifier, @NotNull Command command) {
        String commandName = command.getRoot().getName().toLowerCase();
        String fullCommandName = identifier + SEPARATOR + commandName;
        Command existingCommand = commands.get(fullCommandName);

        if (existingCommand != null) {
            Command newCommand = mergeCommand(command, existingCommand);
            commands.put(fullCommandName, newCommand);
            commands.put(commandName, newCommand);
            return newCommand;
        }

        commands.put(fullCommandName, command);
        commands.put(commandName, command);
        return command;
    }

    protected @NotNull Command mergeCommand(@NotNull Command left, @NotNull Command right) {
        return right;
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty())
            return CommandResult.commandNotFound("");

        String commandName = input.getFirst();

        Command command = commands.get(commandName);
        if (command == null)
            return CommandResult.commandNotFound(commandName);
        return command.execute(sender, input);
    }
}
