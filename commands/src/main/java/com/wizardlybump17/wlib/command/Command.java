package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.exception.InvalidInputException;
import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Command implements Comparable<Command> {

    public static final @NotNull Comparator<Command> COMPARATOR = Comparator
            .comparing(Command::getFullCommand);

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
        List<CommandNode<?>> children = List.of(root);

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
                            throw new InvalidInputException("Null inputs not accepted by " + child);
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
                return CommandResult.exceptionally(lastInputIndex, lastNode, new NullPointerException("The CommandResult can not be null"));
            if (result.lastNode() == null)
                return CommandResult.genericError(lastInputIndex, lastNode, "The last node can not be null");
            if (result.lastInputIndex() < 0)
                return CommandResult.genericError(lastInputIndex, lastNode, "The last input index can not be less than 0");
            return result;
        } catch (Throwable throwable) {
            return CommandResult.exceptionally(lastInputIndex, lastNode, throwable);
        }
    }

    public @NotNull List<String> getInputList(@NotNull String original) {
        return StringUtil.parseQuotedStrings(original);
    }

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input) throws SuggesterException {
        if (input.isEmpty())
            return List.of(root.getName());

        String currentInput = input.getLast();

        List<String> suggestions = new ArrayList<>();
        List<CommandNode<?>> children = List.of(root);

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

    public @NotNull Command merge(@NotNull Command other) {
        if (other.getClass() != getClass())
            return other.merge(this);
        return new Command(root.merge(other.getRoot()));
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
        return COMPARATOR.compare(this, other);
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

    public int getTotalNodes() {
        return root.getTotalNodes();
    }
}
