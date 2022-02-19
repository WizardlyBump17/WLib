package com.wizardlybump17.wlib.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Simple class to store a pair of 2 objects
 *
 * @param <F> the type of the first object
 * @param <S> the type of the second object
 */
@Data
@AllArgsConstructor
public class Pair<F, S> {

    private F first;
    private S second;

    /**
     * Maps this pair by using the given suppliers to set the first and second objects
     *
     * @param first  the supplier to set the first object
     * @param second the supplier to set the second object
     * @return this pair
     */
    public Pair<F, S> map(@Nullable Supplier<F> first, @Nullable Supplier<S> second) {
        if (first != null)
            this.first = first.get();
        if (second != null)
            this.second = second.get();
        return this;
    }

    /**
     * Maps this pair by using the given supplier to set the first object
     *
     * @param first the supplier to set the first object
     * @return this pair
     */
    public Pair<F, S> map(@Nullable Supplier<F> first) {
        if (first != null)
            this.first = first.get();
        return this;
    }
}
