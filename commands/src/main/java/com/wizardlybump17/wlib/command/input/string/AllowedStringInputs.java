package com.wizardlybump17.wlib.command.input.string;

import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

public interface AllowedStringInputs extends AllowedInputs<String> {

    final class Listed implements AllowedStringInputs {

        private final @NotNull List<String> list;
        private final @NotNull List<String> listToCheck;
        private final boolean ignoreCase;

        public Listed(@NotNull List<String> list, boolean ignoreCase) {
            this.list = List.copyOf(list);
            if (ignoreCase) {
                listToCheck = list.stream()
                        .map(String::toLowerCase)
                        .toList();
            } else {
                listToCheck = list;
            }
            this.ignoreCase = ignoreCase;
        }

        @Override
        public boolean isAllowed(@Nullable String input) {
            return listToCheck.contains(input);
        }

        public @NotNull @Unmodifiable List<String> list() {
            return list;
        }

        public boolean ignoreCase() {
            return ignoreCase;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Listed listed = (Listed) object;
            return ignoreCase == listed.ignoreCase && Objects.equals(list, listed.list) && Objects.equals(listToCheck, listed.listToCheck);
        }

        @Override
        public int hashCode() {
            return Objects.hash(list, listToCheck, ignoreCase);
        }

        @Override
        public String toString() {
            return "Listed{" +
                    "list=" + list +
                    ", listToCheck=" + listToCheck +
                    ", ignoreCase=" + ignoreCase +
                    '}';
        }
    }

    final class Any implements AllowedStringInputs {

        @Override
        public boolean isAllowed(@Nullable String input) {
            return true;
        }

        @Override
        public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return List.of(current);
        }

        @Override
        public boolean equals(Object object) {
            return object != null && getClass() == object.getClass();
        }

        @Override
        public int hashCode() {
            return Objects.hash();
        }

        @Override
        public String toString() {
            return "Any{}";
        }
    }
}
