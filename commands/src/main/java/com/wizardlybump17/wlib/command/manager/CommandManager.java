package com.wizardlybump17.wlib.command.manager;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.manager.listener.CommandManagerListener;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.result.error.CommandNotFoundResult;
import com.wizardlybump17.wlib.command.result.full.FullCommandResult;
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
    private final @NotNull Map<String, Object> holdersByFullName = new ConcurrentHashMap<>();

    protected void addCommand(@NotNull String identifier, @NotNull String name, @NotNull Command command, @Nullable Object holder) {
        String fullName = identifier + SEPARATOR + name;

        commandsByFullName.put(fullName, command);
        commandsByName.put(name, command);

        if (holder != null) {
            commandsByHolder.computeIfAbsent(holder, $ -> ConcurrentHashMap.newKeySet()).add(command);
            holdersByFullName.put(fullName, holder);
        }

        for (CommandManagerListener listener : listeners)
            listener.onRegister(identifier, command, holder, this);
    }

    public @NotNull Command registerCommand(@NotNull String identifier, @NotNull Command command, @Nullable Object holder) {
        String commandName = command.getRoot().getName().toLowerCase();
        String fullCommandName = identifier + SEPARATOR + commandName;

        Command existingCommand = commandsByFullName.get(fullCommandName);

        if (existingCommand != null) {
            Command newCommand = mergeCommand(existingCommand, command);
            addCommand(identifier, commandName, newCommand, holder);
            return newCommand;
        }

        addCommand(identifier, commandName, command, holder);
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

    public @NotNull FullCommandResult execute(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty())
            return new FullCommandResult(sender, List.of(), 0, CommandNotFoundResult.dummyNode(), CommandResult.commandNotFound(""));

        String commandName = input.getFirst();

        Command command = commandsByFullName.get(commandName);

        if (command == null)
            command = commandsByName.get(commandName);
        if (command == null)
            return new FullCommandResult(sender, input, 0, CommandNotFoundResult.dummyNode(), CommandResult.commandNotFound(commandName));

        return command.execute(sender, input);
    }

    public @NotNull FullCommandResult execute(@NotNull CommandSender<?> sender, @NotNull String input) {
        return execute(sender, StringUtil.parseQuotedStrings(input));
    }

    public @NotNull FullCommandResult execute(@NotNull CommandSender<?> sender, @NotNull String @NotNull [] input) {
        return execute(sender, String.join(" ", input));
    }

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty() || input.size() == 1) {
            return commandsByName.values().stream()
                    .map(Command::getRoot)
                    .filter(node -> node.canExecute(sender))
                    .map(CommandNode::getName)
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

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull String input) {
        try {
            if (!input.isEmpty() && !StringUtil.isProperlyQuoted(input))
                input = input + "\"";
            return getSuggestions(sender, StringUtil.parseQuotedStrings(input));
        } catch (QuotedStringException e) {
            return List.of();
        }
    }

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull String @NotNull [] input) {
        return getSuggestions(sender, String.join(" ", input));
    }

    public @NotNull Optional<Command> getCommand(@NotNull String name) {
        return Optional
                .ofNullable(commandsByFullName.get(name))
                .or(() -> Optional.ofNullable(commandsByName.get(name)));
    }

    public void clear() {
        for (CommandManagerListener listener : listeners)
            listener.onPreClear(this);

        commandsByName.clear();
        commandsByFullName.clear();

        commandsByHolder.forEach((holder, commands) -> commands.clear());
        commandsByHolder.clear();

        holdersByFullName.clear();

        for (CommandManagerListener listener : listeners)
            listener.onPostClear(this);
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

    public @NotNull @UnmodifiableView Map<String, Object> getHoldersByFullName() {
        return Collections.unmodifiableMap(holdersByFullName);
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

    public boolean isEmpty() {
        return commandsByFullName.isEmpty();
    }

    public void unregister(@NotNull String identifier, @NotNull Command command) {
        unregister(identifier, command.getName());
    }

    public void unregister(@NotNull String identifier, @NotNull String name) {
        String fullName = identifier + SEPARATOR + name;

        Command command = commandsByFullName.remove(fullName);
        if (command == null)
            return;

        commandsByName.remove(name);

        Object holder = holdersByFullName.remove(fullName);
        if (holder != null) {
            Set<Command> commands = commandsByHolder.get(holder);
            if (commands != null)
                commands.remove(command);
        }

        for (CommandManagerListener listener : listeners)
            listener.onUnregister(identifier, command, holder, this);
    }

    public void unregisterByHolder(@NotNull String identifier, @NotNull Object holder) {
        Set<Command> commands = commandsByHolder.get(holder);
        if (commands == null)
            return;

        for (Command command : commands)
            unregister(identifier, command);
    }
}
