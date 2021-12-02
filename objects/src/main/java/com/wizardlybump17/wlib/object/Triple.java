package com.wizardlybump17.wlib.object;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A triple of objects.
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
}
