package com.wizardlybump17.wlib.command.rework.context;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record CommandContext(@NotNull Command command, @NotNull CommandSender<?> sender, @NotNull CommandNodeArguments arguments) {

    public record CommandNodeArgument<T>(@NotNull CommandNode<T> node, @NotNull String input, @NotNull CommandResult<T> result) {

        public @Nullable T data() {
            return result.data();
        }
    }

    public record CommandNodeArguments(@NotNull Map<String, CommandNodeArgument<?>> arguments, @NotNull CommandResult<?> lastResult, @NotNull CommandNode<?> lastNode, @NotNull String lastInput) {

        public CommandNodeArguments {
            arguments = Collections.unmodifiableMap(arguments);
        }

        @SuppressWarnings("unchecked")
        public CommandNodeArguments(@NotNull List<CommandNodeArgument<?>> arguments, @NotNull CommandResult<?> lastResult, @NotNull CommandNode<?> lastNode, @NotNull String lastInput) {
            this(
                    Map.ofEntries(arguments.stream()
                            .map(argument -> new AbstractMap.SimpleEntry<>(argument.node().getName(), argument))
                            .toArray(Map.Entry[]::new)
                    ),
                    lastResult,
                    lastNode,
                    lastInput
            );
        }

        public boolean hasArgument(@NotNull String key) {
            return arguments.containsKey(key);
        }

        @SuppressWarnings("unchecked")
        public <T> @NotNull Optional<CommandNodeArgument<T>> getArgument(@NotNull String key) {
            return Optional.ofNullable((CommandNodeArgument<T>) arguments.get(key));
        }
    }
}
