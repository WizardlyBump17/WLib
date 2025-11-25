package com.wizardlybump17.wlib.command.input.number;

import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.input.RangedAllowedInputs;
import com.wizardlybump17.wlib.command.input.SingleValueInput;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Ranged<?> ranged = (Ranged<?>) object;
            return Objects.equals(from, ranged.from) && Objects.equals(to, ranged.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return "Ranged{" +
                    "from=" + from +
                    ", to=" + to +
                    '}';
        }
    }

    abstract class Unlimited<N extends Number> implements AllowedNumberInputs<N> {

        @Override
        public boolean isAllowed(@Nullable N input) {
            return true;
        }

        @Override
        public String toString() {
            return "Unlimited{}";
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

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Value<?> value1 = (Value<?>) object;
            return Objects.equals(value, value1.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Value{" +
                    "value=" + value +
                    '}';
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

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            Values<?> values1 = (Values<?>) object;
            return Objects.equals(values, values1.values);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(values);
        }

        @Override
        public String toString() {
            return "Values{" +
                    "values=" + values +
                    '}';
        }
    }

    abstract class Positive<N extends Number> implements AllowedNumberInputs<N>, RangedAllowedInputs<N> {

        @Override
        public boolean isAllowed(@Nullable N input) {
            if (input == null)
                return false;
            return isInRange(input);
        }

        @Override
        public String toString() {
            return "Positive{}";
        }
    }

    abstract class Negative<N extends Number> implements AllowedNumberInputs<N>, RangedAllowedInputs<N> {

        @Override
        public boolean isAllowed(@Nullable N input) {
            if (input == null)
                return false;
            return isInRange(input);
        }

        @Override
        public String toString() {
            return "Negative{}";
        }
    }
}
