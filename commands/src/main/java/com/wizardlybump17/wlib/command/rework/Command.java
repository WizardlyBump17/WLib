package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.exception.InvalidInputException;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Command {

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
            throw new IllegalArgumentException();

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
                return CommandResult.outOfRangeInput(lastInputIndex, lastNode); //TODO: return a NotInRangeResult

            return CommandResult.extraArguments(lastInputIndex, lastNode);
        }

        if (!lastNode.getChildren().isEmpty())
            return CommandResult.insufficientArguments(lastInputIndex, lastNode);

        return CommandResult.successful(new Object(), lastInputIndex, lastNode);
    }

    public @NotNull List<String> getInputList(@NotNull String original) {
        return StringUtil.parseQuotedStrings(original);
    }

    public static @Nullable Command createCommand(@NotNull String execution) {
        LiteralCommandNode firstNode = null;

        List<CommandNode<?>> children = new ArrayList<>();
        String[] strings = execution.split(" ");

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (i == 0) {
                firstNode = new LiteralCommandNode(string, children);
                continue;
            }

            List<CommandNode<?>> newChildren = new ArrayList<>();
            LiteralCommandNode newNode = new LiteralCommandNode(string, newChildren);
            children.add(newNode);

            children = newChildren;
        }

        return firstNode == null ? null : new Command(firstNode);
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

    @Override
    public int hashCode() {
        return Objects.hashCode(root);
    }
}
