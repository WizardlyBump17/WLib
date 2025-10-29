package com.wizardlybump17.wlib.command.rework.manager;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandManager {

    private static final char SEPARATOR = ':';

    private final @NotNull Map<String, Command> commands = new HashMap<>();

    public @NotNull Command registerCommand(@NotNull String identifier, @NotNull Command command) {
        String commandName = command.getRoot().getName().toLowerCase();
        String fullCommandName = identifier + SEPARATOR + commandName;
        Command existingCommand = commands.get(fullCommandName);

        if (existingCommand != null) {
            Command newCommand = mergeCommand(existingCommand, command);
            commands.put(fullCommandName, newCommand);
            commands.put(commandName, newCommand);
            return newCommand;
        }

        commands.put(fullCommandName, command);
        commands.put(commandName, command);
        return command;
    }

    protected @NotNull Command mergeCommand(@NotNull Command left, @NotNull Command right) {
        return left.merge(right);
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

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull String input) {
        return execute(sender, StringUtil.parseQuotedStrings(input));
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull String @NotNull [] input) {
        return execute(sender, String.join(" ", input));
    }

    public @NotNull Optional<Command> getCommand(@NotNull String name) {
        return Optional.ofNullable(commands.get(name));
    }
}
