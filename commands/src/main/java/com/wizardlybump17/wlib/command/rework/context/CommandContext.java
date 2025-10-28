package com.wizardlybump17.wlib.command.rework.context;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public record CommandContext(@NotNull Command command, @NotNull CommandSender<?> sender, @NotNull CommandNodeArguments arguments, int lastInputIndex, @NotNull CommandNode<?> lastNode) {

    public record CommandNodeArgument<T>(@NotNull CommandNode<T> node, @NotNull String input, @Nullable T data) {
    }

    public static final class CommandNodeArguments {

        private final @NotNull LinkedHashMap<String, CommandNodeArgument<?>> arguments;

        private CommandNodeArguments(@NotNull LinkedHashMap<String, CommandNodeArgument<?>> arguments) {
            this.arguments = arguments; //CommandResult and ParseResult
        }

        public CommandNodeArguments(@NotNull List<CommandNodeArgument<?>> arguments) {
            this((LinkedHashMap<String, CommandNodeArgument<?>>) MapUtils.collectionToMap(LinkedHashMap::new, arguments, argument -> argument.node().getName()));
        }

        public boolean hasArgument(@NotNull String key) {
            return arguments.containsKey(key);
        }

        @SuppressWarnings("unchecked")
        public <T> @NotNull Optional<CommandNodeArgument<T>> getArgument(@NotNull String key) {
            return Optional.ofNullable((CommandNodeArgument<T>) arguments.get(key));
        }

        public @NotNull @Unmodifiable Map<String, CommandNodeArgument<?>> getArguments() {
            return Collections.unmodifiableMap(arguments);
        }

        @Override
        public String toString() {
            return "CommandNodeArguments{" +
                    "arguments=" + arguments +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            CommandNodeArguments arguments1 = (CommandNodeArguments) o;
            return Objects.equals(arguments, arguments1.arguments);
        }

        @Override
        public int hashCode() {
            return Objects.hash(arguments);
        }
    }
}
