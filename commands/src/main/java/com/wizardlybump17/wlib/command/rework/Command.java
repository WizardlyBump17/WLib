package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.executor.CommandExecutor;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.SuccessResult;
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

    public @NotNull CommandResult execute(@NotNull CommandSender<?> sender, @NotNull String execution) {
        List<String> strings = getInputList(execution);
        Map<String, NodeResult<?>> results = getNodes(strings);

        if (results.isEmpty())
            return CommandResult.error();

        return SuccessResult.INSTANCE;
    }

    public @NotNull List<String> getInputList(@NotNull String original) {
        return StringUtil.parseQuotedStrings(original);
    }

    public @NotNull Map<String, NodeResult<?>> getNodes(@NotNull List<String> input) {
        if (input.isEmpty())
            return Map.of();

        Map<String, NodeResult<?>> nodes = new LinkedHashMap<>();

        List<CommandNode<?>> children = List.of(root);

        CommandNode<?> last = null;
        inputLoop: for (String inputString : input) {
            for (CommandNode<?> child : children) {
                NodeResult<?> nodeResult = getNodeResult(child, inputString);
                if (nodeResult == null)
                    continue;

                last = child;
                children = child.getChildren();

                nodes.put(inputString, nodeResult);
                continue inputLoop;
            }

            return Map.of();
        }

        if (!last.getChildren().isEmpty())
            return Map.of();

        return nodes;
    }

    @SuppressWarnings("unchecked")
    public static @Nullable NodeResult<Object> getNodeResult(@NotNull CommandNode<?> node, @NotNull String input) {
        CommandNode.ParseResult<?> parseResult = node.parse(input);
        if (!parseResult.success())
            return null;

        Object value = parseResult.value();
        if (!((CommandNode<Object>) node).isValidInput(value))
            return null;

        return new NodeResult<>((CommandNode<Object>) node, value);
    }

    public record NodeResult<T>(@NotNull CommandNode<T> node, @Nullable T value) {
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

            CommandExecutor executor;
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
