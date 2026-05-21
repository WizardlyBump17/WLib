package com.wizardlybump17.wlib.command.manager;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.exception.CommandExecutionException;
import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.exception.InvalidInputException;
import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.manager.listener.CommandManagerListener;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import com.wizardlybump17.wlib.util.StringUtil;
import com.wizardlybump17.wlib.util.exception.QuotedStringException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

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

    @SuppressWarnings("unchecked")
    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull List<String> input) throws CommandExecutionException {
        if (input.isEmpty())
            return CommandResult.badRequest(CommandResult.ErrorDetails.emptyInput());

        String commandName = input.getFirst();

        Command command = commandsByFullName.get(commandName);

        if (command == null)
            command = commandsByName.get(commandName);
        if (command == null)
            return CommandResult.notFound(CommandResult.ErrorDetails.nodeNotFound(0, commandName));

        List<CommandContext.CommandNodeArgument<?>> arguments = new ArrayList<>();
        List<CommandNode<?>> children = List.of(command.getRoot());

        CommandNode<?> lastNode = null;
        int lastInputIndex = 0;
        InputParsingException lastParsingError = null;
        InvalidInputException lastInputError = null;

        inputLoop: for (int i = 0; i < input.size(); i++) {
            String inputString = input.get(i);
            lastInputIndex = i;

            for (CommandNode<?> child : children) {
                lastNode = child;

                try {
                    Object result;
                    if (inputString == null) {
                        if (child.isValidInput(null))
                            result = null;
                        else
                            throw new InvalidInputException("Null inputs are not accepted by " + child.getName());
                    } else {
                        result = child.parseOrInvalid(inputString);
                    }

                    if (!(child instanceof LiteralCommandNode))
                        arguments.add(new CommandContext.CommandNodeArgument<>((CommandNode<Object>) child, inputString, result));

                    children = child.getChildren();

                    lastParsingError = null;
                    lastInputError = null;

                    continue inputLoop;
                } catch (InputParsingException e) {
                    lastParsingError = e;
                } catch (InvalidInputException e) {
                    lastInputError = e;
                }
            }

            if (lastParsingError != null)
                return CommandResult.badRequest(CommandResult.ErrorDetails.parseError(command.getName(), lastNode.getName(), lastParsingError));
            if (lastInputError != null)
                return CommandResult.unprocessableContent(CommandResult.ErrorDetails.inputError(command.getName(), lastNode.getName(), inputString, lastInputError));

            return CommandResult.notFound(CommandResult.ErrorDetails.nodeNotFound(lastInputIndex, inputString));
        }

        CommandNodeExecutor<?> executor = lastNode.getExecutor();
        if (executor == null)
            return CommandResult.notImplemented(CommandResult.ErrorDetails.noCommandExecutor(command.getName(), lastNode.getName()));

        CommandContext context = new CommandContext(
                command,
                sender,
                new CommandContext.CommandNodeArguments(arguments),
                lastInputIndex,
                lastNode
        );

        String nodePermission = lastNode.getPermission();
        if (!lastNode.canExecute(sender))
            return CommandResult.forbidden(CommandResult.ErrorDetails.noPermission(sender.getName(), nodePermission));

        try {
            CommandResult<?> result = executor.execute(context);
            if (result == null)
                throw new CommandExecutionException("The returned CommandResult can not be null", lastInputIndex, lastNode, CommandExecutionException.Reason.INVALID_COMMAND_RESULT);
            return result;
        } catch (Throwable throwable) {
            throw new CommandExecutionException(CommandExecutionException.MESSAGE.formatted(input, lastInputIndex, lastNode.getName()), throwable);
        }
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull String input) throws CommandExecutionException {
        return execute(sender, StringUtil.parseQuotedStrings(input));
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull String @NotNull [] input) throws CommandExecutionException {
        return execute(sender, String.join(" ", input));
    }

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input) throws SuggesterException {
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

        String currentInput = input.getLast();

        List<String> suggestions = new ArrayList<>();
        List<CommandNode<?>> children = List.of(command.getRoot());

        CommandNode<?> lastNode = null;
        Throwable lastError = null;

        inputLoop: for (int i = 0; i < input.size(); i++) {
            String inputString = input.get(i);
            boolean isLastInput = i == input.size() - 1;

            if (isLastInput && inputString != null && inputString.isEmpty()) {
                for (CommandNode<?> child : children) {
                    String permission = child.getPermission();
                    if (permission == null || sender.hasPermission(permission))
                        suggestions.addAll(getSuggestions0(child, sender, input, ""));
                }
                if (!children.isEmpty())
                    lastNode = children.getLast();
                break;
            }

            boolean foundNode = !children.isEmpty();
            for (CommandNode<?> child : children) {
                lastNode = child;

                if (!isLastInput) {
                    try {
                        if (inputString == null) {
                            if (!child.isValidInput(null))
                                throw new InvalidInputException("Null inputs not accepted by " + child);
                            continue;
                        }

                        child.parseOrInvalid(inputString);
                    } catch (InputParsingException | InvalidInputException e) {
                        lastError = e;
                        foundNode = false;
                        continue;
                    }
                }

                lastError = null;
                foundNode = true;

                String permission = child.getPermission();
                if (isLastInput && (permission == null || sender.hasPermission(permission)))
                    suggestions.addAll(getSuggestions0(child, sender, input, currentInput));

                if (isLastInput) {
                    continue;
                } else {
                    children = child.getChildren();
                    continue inputLoop;
                }
            }

            if (lastError != null || !foundNode)
                return List.of();
        }

        if (lastNode == null)
            return List.of();

        return suggestions;
    }

    @SuppressWarnings("unchecked")
    private static @NotNull List<String> getSuggestions0(@NotNull CommandNode<?> node, @NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String currentInput) throws SuggesterException {
        List<?> childSuggestions = node.getSuggestions(sender, input, currentInput);
        Suggester<Object> suggester = (Suggester<Object>) node.getSuggester();

        if (suggester == null) {
            return childSuggestions
                    .stream()
                    .map(Object::toString)
                    .toList();
        } else {
            Stream<String> stream = childSuggestions
                    .stream()
                    .map(suggester::getStringRepresentation);
            if (suggester.needsEscape())
                stream = stream.map(StringUtil::escapeString);
            return stream.toList();
        }
    }

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull String input) throws SuggesterException {
        try {
            if (!input.isEmpty() && !StringUtil.isProperlyQuoted(input))
                input = input + "\"";
            return getSuggestions(sender, StringUtil.parseQuotedStrings(input));
        } catch (QuotedStringException e) {
            return List.of();
        }
    }

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull String @NotNull [] input) throws SuggesterException {
        if (input.length == 0)
            return getSuggestions(sender, List.of());

        String inputString = String.join(" ", input);
        try {
            if (!inputString.isEmpty() && !StringUtil.isProperlyQuoted(inputString))
                inputString = inputString + "\"";

            List<String> inputList = StringUtil.parseQuotedStrings(inputString);
            if (input[input.length - 1].isEmpty())
                inputList.add("");

            return getSuggestions(sender, inputList);
        } catch (QuotedStringException e) {
            return List.of();
        }
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
