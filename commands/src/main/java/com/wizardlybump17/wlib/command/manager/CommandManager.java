package com.wizardlybump17.wlib.command.manager;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.manager.listener.CommandManagerListener;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import com.wizardlybump17.wlib.util.exception.QuotedStringException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public static final char SEPARATOR = ':';

    private final @NotNull Map<String, Command> commandsByFullName = new ConcurrentHashMap<>();
    private final @NotNull Map<String, Command> commandsByName = new ConcurrentHashMap<>();
    private final @NotNull Set<CommandManagerListener> listeners = ConcurrentHashMap.newKeySet();
    private final @NotNull Map<Object, Set<Command>> commandsByHolder = new ConcurrentHashMap<>();

    protected void addCommand(@NotNull String identifier, @NotNull String fullName, @NotNull String name, @NotNull Command command, @Nullable Object holder) {
        commandsByFullName.put(fullName, command);
        commandsByName.put(name, command);
        if (holder != null)
            commandsByHolder.computeIfAbsent(holder, $ -> ConcurrentHashMap.newKeySet()).add(command);

        for (CommandManagerListener listener : listeners)
            listener.onRegister(identifier, command, holder, this);
    }

    public @NotNull Command registerCommand(@NotNull String identifier, @NotNull Command command, @Nullable Object holder) {
        String commandName = command.getRoot().getName().toLowerCase();
        String fullCommandName = identifier + SEPARATOR + commandName;

        Command existingCommand = commandsByFullName.get(fullCommandName);

        if (existingCommand != null) {
            Command newCommand = mergeCommand(existingCommand, command);
            addCommand(identifier, fullCommandName, commandName, newCommand, holder);
            return newCommand;
        }

        addCommand(identifier, fullCommandName, commandName, command, holder);
        return command;
    }

    public @NotNull Command registerCommand(@NotNull String identifier, @NotNull Command command) {
        return registerCommand(identifier, command, null);
    }

    public @NotNull List<Command> registerCommands(@NotNull String identifier, @NotNull List<Command> commands, @Nullable Object holder) {
        List<Command> newCommands = new ArrayList<>(commands.size());
        for (Command command : commands)
            newCommands.add(registerCommand(identifier, command, holder));
        return newCommands;
    }

    public @NotNull List<Command> registerCommands(@NotNull String identifier, @NotNull List<Command> commands) {
        return registerCommands(identifier, commands, null);
    }

    protected @NotNull Command mergeCommand(@NotNull Command left, @NotNull Command right) {
        return left.merge(right);
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty())
            return CommandResult.commandNotFound("");

        String commandName = input.getFirst();

        Command command = commandsByFullName.get(commandName);

        if (command == null)
            command = commandsByName.get(commandName);
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

    public @NotNull List<Object> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty() || input.size() == 1) {
            return commandsByName.values().stream()
                    .map(Command::getRoot)
                    .filter(node -> node.canExecute(sender))
                    .map(CommandNode::getName)
                    .map(string -> (Object) string)
                    .toList();
        }

        String commandName = input.getFirst();

        Command command = commandsByFullName.get(commandName);

        if (command == null)
            command = commandsByName.get(commandName);
        if (command == null)
            return List.of();

        return command.getSuggestions(sender, input);
    }

    public @NotNull List<Object> getSuggestions(@NotNull CommandSender<?> sender, @NotNull String input) {
        try {
            if (!input.isEmpty() && !StringUtil.isProperlyQuoted(input))
                input = input + "\"";
            return getSuggestions(sender, StringUtil.parseQuotedStrings(input));
        } catch (QuotedStringException e) {
            return List.of();
        }
    }

    public @NotNull List<Object> getSuggestions(@NotNull CommandSender<?> sender, @NotNull String @NotNull [] input) {
        return getSuggestions(sender, String.join(" ", input));
    }

    public @NotNull Optional<Command> getCommand(@NotNull String name) {
        return Optional
                .ofNullable(commandsByFullName.get(name))
                .or(() -> Optional.ofNullable(commandsByName.get(name)));
    }

    public void clear() {
        commandsByName.clear();
        commandsByFullName.clear();

        commandsByHolder.forEach((holder, commands) -> commands.clear());
        commandsByHolder.clear();

        for (CommandManagerListener listener : listeners)
            listener.onClear(this);
    }

    public @NotNull @UnmodifiableView Map<String, Command> getCommandsByFullName() {
        return Collections.unmodifiableMap(commandsByFullName);
    }

    public @NotNull @UnmodifiableView Map<String, Command> getCommandsByName() {
        return Collections.unmodifiableMap(commandsByName);
    }

    public @NotNull @UnmodifiableView Map<Object, Set<Command>> getCommandsByHolder() {
        return Collections.unmodifiableMap(commandsByHolder);
    }

    public @NotNull @UnmodifiableView Set<Command> getCommandsByHolder(@NotNull Object holder) {
        return Collections.unmodifiableSet(commandsByHolder.getOrDefault(holder, Set.of()));
    }

    public void addListener(@NotNull CommandManagerListener listener) {
        listeners.add(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public @NotNull @UnmodifiableView Set<CommandManagerListener> getListeners() {
        return Collections.unmodifiableSet(listeners);
    }
}
