package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.executor.CommandExecutor;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Command {

    private final @NotNull LiteralCommandNode root;

    public Command(@NotNull LiteralCommandNode root) {
        this.root = root;
    }

    public @NotNull LiteralCommandNode getRoot() {
        return root;
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull String execution) {
        List<String> strings = getInputList(execution);
        CommandContext.CommandNodeArguments arguments = getArguments(strings);

        if (arguments == null)
            return CommandResult.error();

        return CommandResult.successful(new Object());
    }

    public @NotNull List<String> getInputList(@NotNull String original) {
        return StringUtil.parseQuotedStrings(original);
    }

    @SuppressWarnings("unchecked")
    public @Nullable CommandContext.CommandNodeArguments getArguments(@NotNull List<String> input) {
        if (input.isEmpty())
            return null;

        Map<String, CommandContext.CommandNodeArgument<?>> arguments = new LinkedHashMap<>();

        List<CommandNode<?>> children = List.of(root);

        CommandNode<?> lastSuccessfulNode = null;
        CommandResult<?> lastResult = null;
        String lastInputString = null;
        inputLoop: for (String inputString : input) {
            for (CommandNode<?> child : children) {
                CommandResult<?> result = child.parseOrInvalid(inputString);

                lastResult = result;
                lastInputString = inputString;

                if (!result.success())
                    continue;

                lastSuccessfulNode = child;
                children = child.getChildren();

                arguments.put(inputString, new CommandContext.CommandNodeArgument<>((CommandNode<Object>) child, inputString, (CommandResult<Object>) result));
                continue inputLoop;
            }

            return new CommandContext.CommandNodeArguments(arguments, lastResult, lastSuccessfulNode, lastInputString);
        }

        if (!lastSuccessfulNode.getChildren().isEmpty())
            return new CommandContext.CommandNodeArguments(arguments, lastResult, lastSuccessfulNode, lastInputString);

        return new CommandContext.CommandNodeArguments(arguments, lastResult, lastSuccessfulNode, lastInputString);
    }

    public static @NotNull CommandResult<?> getNodeResult(@NotNull CommandNode<?> node, @NotNull String input) {
        return node.parseOrInvalid(input);
//
//        CommandResult<Object> parseResult = (CommandResult<Object>) node.parse(input);
//        Object value = parseResult.data();
//
//        CommandContext.CommandNodeArgument<Object> argument = new CommandContext.CommandNodeArgument<>((CommandNode<Object>) node, input, parseResult);
//        if (!parseResult.success())
//            return argument;
//
//        if (!((CommandNode<Object>) node).isValidInput(value))
//            return argument;
//
//        return argument;
    }

    public static @Nullable Command createCommand(@NotNull String execution) {
        LiteralCommandNode firstNode = null;

        List<CommandNode<?>> children = new ArrayList<>();
        String[] strings = execution.split(" ");

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (i == 0) {
                firstNode = new LiteralCommandNode(string, children, null);
                continue;
            }

            CommandExecutor<String> executor;
            if (i + 1 >= strings.length)
                executor = CommandExecutor.TEST_EXECUTOR;
            else
                executor = null;

            List<CommandNode<?>> newChildren = new ArrayList<>();
            LiteralCommandNode newNode = new LiteralCommandNode(string, newChildren, executor);
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
