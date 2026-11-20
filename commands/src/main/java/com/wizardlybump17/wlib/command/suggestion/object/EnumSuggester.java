package com.wizardlybump17.wlib.command.suggestion.object;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public interface EnumSuggester<E extends Enum<E>> extends Suggester<E> {

    @Override
    default @NotNull String getStringRepresentation(@NotNull E value) {
        return value.name();
    }

    @NotNull Class<E> enumType();

    static <E extends Enum<E>> @NotNull Any<E> any(@NotNull Class<E> enumType) {
        return new Any<>(enumType);
    }

    final class Any<E extends Enum<E>> implements EnumSuggester<E> {

        private final @NotNull List<E> cache;
        private final @NotNull Class<E> enumType;

        private Any(@NotNull Class<E> enumType) {
            this.enumType = enumType;
            this.cache = List.of(enumType.getEnumConstants());
        }

        @Override
        public @NotNull List<E> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) throws SuggesterException {
            return cache;
        }

        @Override
        public @NotNull Class<E> enumType() {
            return enumType;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            Any<?> any = (Any<?>) o;
            return Objects.equals(cache, any.cache) && Objects.equals(enumType, any.enumType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cache, enumType);
        }

        @Override
        public String toString() {
            return "EnumSuggester$Any{" +
                    "cache=" + cache +
                    ", enumType=" + enumType +
                    '}';
        }
    }
}
