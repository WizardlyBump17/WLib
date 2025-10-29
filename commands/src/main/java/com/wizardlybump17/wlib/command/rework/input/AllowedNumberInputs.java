package com.wizardlybump17.wlib.command.rework.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

        record Unlimited() implements AllowedIntegerInputs {

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                return true;
            }
        }

        record SingleValue(int value) implements AllowedIntegerInputs {

            @Override
            public boolean isAllowed(@Nullable Integer input) {
                return input != null && input.compareTo(value) == 0;
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
        }
    }
}
