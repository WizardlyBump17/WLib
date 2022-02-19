package com.wizardlybump17.wlib.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A triple of objects.
 *
 * @param <F> the type of the first object
 * @param <S> the type of the second object
 * @param <T> the type of the third object
 */
@Data
@AllArgsConstructor
public class Triple<F, S, T> {

    private F first;
    private S second;
    private T third;

    /**
     * Maps this pair by using the given suppliers to set the first, second and third objects
     *
     * @param first  the supplier to set the first object
     * @param second the supplier to set the second object
     * @param third  the supplier to set the third object
     * @return this pair
     */
    public Triple<F, S, T> map(@Nullable Supplier<F> first, @Nullable Supplier<S> second, @Nullable Supplier<T> third) {
        if (first != null)
            this.first = first.get();
        if (second != null)
            this.second = second.get();
        if (third != null)
            this.third = third.get();
        return this;
    }

    /**
     * Maps this pair by using the given suppliers to set the first and second objects
     *
     * @param first  the supplier to set the first object
     * @param second the supplier to set the second object
     * @return this pair
     */
    public Triple<F, S, T> map(@Nullable Supplier<F> first, @Nullable Supplier<S> second) {
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
    public Triple<F, S, T> map(@Nullable Supplier<F> first) {
        if (first != null)
            this.first = first.get();
        return this;
    }
}
