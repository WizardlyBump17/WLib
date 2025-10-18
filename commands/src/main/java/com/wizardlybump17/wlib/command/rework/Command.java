package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.SuccessResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
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

    @SuppressWarnings("unchecked")
    public @NotNull Map<String, NodeResult<?>> getNodes(@NotNull List<String> input) {
        if (input.isEmpty())
            return Map.of();

        Map<String, NodeResult<?>> nodes = new LinkedHashMap<>();

        CommandNode<Object> root = (CommandNode<Object>) (Object) this.root;
        String firstInput = input.getFirst();

        NodeResult<Object> firstNodeResult = getNodeResult(root, firstInput);
        if (firstNodeResult == null)
            return Map.of();

        nodes.put(firstInput, firstNodeResult);

        if (root.getChildren().isEmpty())
            return input.size() > 1 ? Map.of() : nodes;

        Iterator<String> inputIterator = input.subList(1, input.size()).iterator();
        Iterator<CommandNode<Object>> childrenIterator = (Iterator<CommandNode<Object>>) (Object) root.getChildren().iterator();

        if (!inputIterator.hasNext() && childrenIterator.hasNext())
            return Map.of();

        CommandNode<?> last = null;
        inputLoop: for (String inputString : input.subList(1, input.size())) {
            for (CommandNode<?> child : root.getChildren()) {
                NodeResult<Object> nodeResult = getNodeResult(child, inputString);
                if (nodeResult == null)
                    continue;

                last = child;
                root = (CommandNode<Object>) child;

                nodes.put(inputString, nodeResult);
                continue inputLoop;
            }

            return Map.of();
        }

        if (last == null)
            return Map.of();

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
