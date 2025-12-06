package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.exception.InvalidInputException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Command implements Comparable<Command> {

    private final @NotNull LiteralCommandNode root;

    public Command(@NotNull LiteralCommandNode root) {
        this.root = root;
    }

    public @NotNull LiteralCommandNode getRoot() {
        return root;
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull String input) {
        List<String> inputList = getInputList(input);
        return execute(sender, inputList);
    }

    @SuppressWarnings("unchecked")
    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty())
            return CommandResult.insufficientArguments(this);

        List<CommandContext.CommandNodeArgument<?>> arguments = new ArrayList<>();
        Collection<CommandNode<?>> children = List.of(root);

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
                    Object result = child.parseOrInvalid(inputString);

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
                return CommandResult.parseInputException(lastInputIndex, lastNode, lastParsingError);
            if (lastInputError != null)
                return CommandResult.outOfRangeInput(lastInputIndex, lastNode);

            return CommandResult.extraArguments(lastInputIndex, lastNode);
        }

        CommandNodeExecutor<?> executor = lastNode.getExecutor();
        if (executor == null)
            return CommandResult.noCommandNodeExecutor(lastInputIndex, lastNode);

        String nodePermission = lastNode.getPermission();
        if (nodePermission != null && !sender.hasPermission(nodePermission))
            return CommandResult.noPermission(lastInputIndex, lastNode);

        CommandContext context = new CommandContext(
                this,
                sender,
                new CommandContext.CommandNodeArguments(arguments),
                lastInputIndex,
                lastNode
        );

        try {
            CommandResult<?> result = executor.execute(context);
            if (result == null)
                return CommandResult.successful(context, null);
            if (result.lastNode() == null || result.lastInputIndex() < 0)
                return CommandResult.genericError(context);
            return result;
        } catch (Throwable throwable) {
            return CommandResult.exceptionally(lastInputIndex, lastNode, throwable);
        }
    }

    public @NotNull List<String> getInputList(@NotNull String original) {
        return StringUtil.parseQuotedStrings(original);
    }

    public @NotNull List<Object> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty())
            return List.of(root.getName());

        String currentInput = input.getLast();

        List<Object> suggestions = new ArrayList<>();
        Collection<CommandNode<?>> children = List.of(root);

        CommandNode<?> lastNode = null;
        InputParsingException lastParsingError = null;

        inputLoop: for (int i = 0; i < input.size(); i++) {
            String inputString = input.get(i);
            boolean isLastInput = i == input.size() - 1;

            if (inputString.isEmpty()) {
                for (CommandNode<?> child : children) {
                    String permission = child.getPermission();
                    if (permission == null || sender.hasPermission(permission))
                        suggestions.addAll(child.getSuggestions(sender, input, ""));
                }
                break;
            }

            boolean foundNode = false;
            for (CommandNode<?> child : children) {
                lastNode = child;

                try {
                    foundNode = true;

                    child.parse(inputString);

                    String permission = child.getPermission();
                    if (isLastInput && (permission == null || sender.hasPermission(permission)))
                        suggestions.addAll(child.getSuggestions(sender, input, currentInput));

                    lastParsingError = null;

                    if (isLastInput) {
                        continue;
                    } else {
                        children = child.getChildren();
                        continue inputLoop;
                    }
                } catch (InputParsingException e) {
                    lastParsingError = e;
                }
            }

            if (lastParsingError != null)
                return List.of();

            if (!foundNode)
                return List.of();
        }

        if (lastNode == null)
            return List.of();

        return suggestions;
    }

    public @NotNull Command merge(@NotNull Command other) {
        return new Command((LiteralCommandNode) root.merge(other.getRoot()));
    }

    @Override
    public String toString() {
        return "Command{" +
                "root=" + root +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Command command = (Command) o;
        return Objects.equals(root, command.root);
    }

    public boolean equalsIgnoreExecutor(@Nullable Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        Command command = (Command) other;
        return root.equalsIgnoreExecutor(command.root);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(root);
    }

    @Override
    public int compareTo(@NotNull Command other) {
        String fullCommand = getFullCommand();
        String otherFullCommand = other.getFullCommand();
        return fullCommand.compareTo(otherFullCommand);
    }

    public @NotNull String getFullCommand() {
        return root.getFullCommand();
    }

    public @Nullable CommandNode<?> findNode(@NotNull String name) {
        return root.findChild(name);
    }

    public @NotNull String getName() {
        return root.getName();
    }
}
