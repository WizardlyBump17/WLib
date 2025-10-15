package com.wizardlybump17.wlib.command.rework.node.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AllowedNumberInputs<N extends Number> extends AllowedInputs<N> {

    @NotNull N from();

    @NotNull N to();

    boolean isInRange(@NotNull N number);

    record AllowedIntegerInputs(@NotNull Integer from, @NotNull Integer to) implements AllowedNumberInputs<Integer> {

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
    }
}
