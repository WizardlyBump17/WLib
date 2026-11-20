package com.wizardlybump17.wlib.command.suggestion.object;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

public interface JsonElementSuggester extends Suggester<JsonElement> {

    @NotNull Gson gson();

    @Override
    default boolean needsEscape() {
        return true;
    }

    static @NotNull Values values(@NotNull Gson gson, @NotNull List<JsonElement> elements) {
        return new Values(gson, elements);
    }

    static @NotNull Values values(@NotNull List<JsonElement> elements) {
        return new Values(Values.GSON, elements);
    }

    static @NotNull Value value(@NotNull Gson gson, @NotNull JsonElement value) {
        return new Value(gson, value);
    }

    static @NotNull Value value(@NotNull JsonElement value) {
        return new Value(Values.GSON, value);
    }

    final class Values extends AbstractValuesSuggester<JsonElement> implements JsonElementSuggester {

        private static final @NotNull Gson GSON = new Gson();

        private final @NotNull Gson gson;

        private Values(@NotNull Gson gson, @NotNull List<JsonElement> suggestions) {
            super(suggestions);
            this.gson = gson;
        }

        @Override
        public @NotNull Gson gson() {
            return gson;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            if (!super.equals(o)) return false;
            JsonElementSuggester.Values values = (JsonElementSuggester.Values) o;
            return Objects.equals(gson, values.gson);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), gson);
        }

        @Override
        public String toString() {
            return "JsonElementSuggester$Values{" +
                    "gson=" + gson +
                    "} " + super.toString();
        }
    }

    final class Value implements JsonElementSuggester {

        private final @NotNull Gson gson;
        private final @NotNull @Unmodifiable List<JsonElement> list;

        private Value(@NotNull Gson gson, @NotNull JsonElement value) {
            this.gson = gson;
            this.list = List.of(value);
        }

        @Override
        public @NotNull List<JsonElement> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) {
            return list;
        }

        @Override
        public @NotNull Gson gson() {
            return gson;
        }

        public @NotNull JsonElement value() {
            return list.getFirst();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            Value value = (Value) o;
            return Objects.equals(gson, value.gson) && Objects.equals(list, value.list);
        }

        @Override
        public int hashCode() {
            return Objects.hash(gson, list);
        }

        @Override
        public String toString() {
            return "Value{" +
                    "gson=" + gson +
                    ", value=" + value() +
                    '}';
        }
    }
}
