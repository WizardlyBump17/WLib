package com.wizardlybump17.wlib.command.input.number;

import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.RangedAllowedInputs;
import com.wizardlybump17.wlib.command.input.SingleValueInput;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AllowedNumberInputs<N extends Number> extends AllowedInputs<N> {

    static <N extends Number> @NotNull Value<N> value(@NotNull N value) {
        return new Value<>(value);
    }

    static <N extends Number> @NotNull Values<N> values(@NotNull List<N> values) {
        return new Values<>(values);
    }

    abstract class Ranged<N extends Number & Comparable<N>> implements AllowedNumberInputs<N>, RangedAllowedInputs<N> {

        private final @NotNull N from;
        private final @NotNull N to;

        public Ranged(@NotNull N from, @NotNull N to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public @NotNull N from() {
            return from;
        }

        @Override
        public @NotNull N to() {
            return to;
        }

        @Override
        public boolean isInRange(@NotNull N number) {
            return number.compareTo(from) >= 0 && number.compareTo(to) <= 0;
        }

        @Override
        public boolean isAllowed(@Nullable N input) {
            if (input == null)
                return false;
            return isInRange(input);
        }
    }

    interface Unlimited<N extends Number> extends AllowedNumberInputs<N> {

        @Override
        default boolean isAllowed(@Nullable N input) {
            return true;
        }
    }

    class Value<N extends Number> implements AllowedNumberInputs<N>, SingleValueInput<N> {

        private final @NotNull N value;

        Value(@NotNull N value) {
            this.value = value;
        }

        @Override
        public @NotNull N value() {
            return value;
        }
    }

    class Values<N extends Number> implements AllowedNumberInputs<N> {

        private final @NotNull List<N> values;

        Values(@NotNull List<N> values) {
            this.values = List.copyOf(values);
        }

        @Override
        public boolean isAllowed(@Nullable N input) {
            if (input == null)
                return false;
            return values.contains(input);
        }

        @Override
        public @NotNull List<N> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current) {
            return values;
        }
    }

    interface Positive<N extends Number> extends AllowedNumberInputs<N>, RangedAllowedInputs<N> {

        @Override
        default boolean isAllowed(@Nullable N input) {
            if (input == null)
                return false;
            return isInRange(input);
        }
    }

    interface Negative<N extends Number> extends AllowedNumberInputs<N>, RangedAllowedInputs<N> {

        @Override
        default boolean isAllowed(@Nullable N input) {
            if (input == null)
                return false;
            return isInRange(input);
        }
    }
}
