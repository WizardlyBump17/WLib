package com.wizardlybump17.wlib.object;

import lombok.Data;

/**
 * Simple class to store a pai of 2 objects
 * @param <T> the type of the first object
 * @param <U> the type of the second object
 */
@Data
public class Pair<T, U> {

    private final T first;
    private final U second;
}
