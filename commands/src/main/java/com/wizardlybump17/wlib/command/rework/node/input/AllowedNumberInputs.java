package com.wizardlybump17.wlib.command.rework.node.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        }
    }
}
