package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.SuccessResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Command {

    private final @NotNull LiteralCommandNode root;

    public Command(@NotNull LiteralCommandNode root) {
        this.root = root;
    }

    public @NotNull LiteralCommandNode getRoot() {
        return root;
    }

    public @NotNull CommandResult execute(@NotNull CommandSender<?> sender, @NotNull String execution) {
        List<String> strings = StringUtil.parseQuotedStrings(execution);
        return SuccessResult.INSTANCE;
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
}
