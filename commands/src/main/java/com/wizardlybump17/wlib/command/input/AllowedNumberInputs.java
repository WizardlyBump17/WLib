package com.wizardlybump17.wlib.command.input;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

public interface AllowedNumberInputs<N extends Number> extends AllowedInputs<N> {

    interface AllowedIntegerInputs extends AllowedInputs<Integer> {

        record Range(@NotNull Integer from, @NotNull Integer to) implements AllowedIntegerInputs, RangedAllowedInputs<Integer> {

            @Override
            public boolean isInRange(@NotNull Integer number) {
                return number.compareTo(from) >= 0 && number.compareTo(to) <= 0;
            }

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                if (input == null)
                    return false;
                return isInRange(input);
            }

            @Override
            public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
                if (to - from < 5)
                    return IntStream.rangeClosed(from, to).boxed().toList();

                int fourth = (to - from) / 4;
                return List.of(from, from + fourth, from + fourth * 2, from + fourth * 3, to);
            }
        }

        record Unlimited() implements AllowedIntegerInputs {

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                return true;
            }

            @Override
            public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
                return List.of(
                        -100000,
                        -50000,
                        -3000,
                        -200,
                        -50,
                        0,
                        50,
                        200,
                        3000,
                        50000,
                        100000
                );
            }
        }

        record SingleValue(int value) implements AllowedIntegerInputs {

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                return input != null && input.compareTo(value) == 0;
            }

            @Override
            public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
                return List.of(value);
            }
        }

        record ValuesList(@NotNull List<Integer> values) implements AllowedIntegerInputs {

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                if (input == null)
                    return false;

                for (Integer value : values)
                    if (input.compareTo(value) == 0)
                        return true;

                return false;
            }

            @Override
            public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
                if (values.size() < 5)
                    return values;

                int fourth = values.size() / 4;
                return List.of(
                        values.get(0),
                        values.get(fourth),
                        values.get(fourth * 2),
                        values.get(fourth * 3),
                        values.get(values.size() - 1)
                );
            }
        }

        record Positive() implements AllowedIntegerInputs, RangedAllowedInputs<Integer> {

            @Override
            public @NotNull Integer from() {
                return 0;
            }

            @Override
            public @NotNull Integer to() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isInRange(@NotNull Integer input) {
                return input > 0;
            }

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                if (input == null)
                    return false;
                return isInRange(input);
            }

            @Override
            public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
                return List.of(
                        0,
                        50,
                        200,
                        3000,
                        50000,
                        100000
                );
            }
        }

        record Negative() implements AllowedIntegerInputs, RangedAllowedInputs<Integer> {

            @Override
            public @NotNull Integer from() {
                return Integer.MIN_VALUE;
            }

            @Override
            public @NotNull Integer to() {
                return -1;
            }

            @Override
            public boolean isInRange(@NotNull Integer input) {
                return input < 0;
            }

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                if (input == null)
                    return false;
                return isInRange(input);
            }

            @Override
            public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
                return List.of(
                        -100000,
                        -50000,
                        -3000,
                        -200,
                        -50,
                        -1
                );
            }
        }
    }
}
