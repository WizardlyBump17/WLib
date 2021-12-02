package com.wizardlybump17.wlib.object;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple class to store a pai of 2 objects
 * @param <F> the type of the first object
 * @param <S> the type of the second object
 */
@Data
@AllArgsConstructor
public class Pair<F, S> {

    private F first;
    private S second;
}
