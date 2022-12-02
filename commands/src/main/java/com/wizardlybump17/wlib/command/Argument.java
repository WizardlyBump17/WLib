package com.wizardlybump17.wlib.command;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * Class used in the arguments to represent a command argument.
 * <p>It stores useful information like the argument name and the user input</p>
 * @param <T> the argument type
 */
@Data
public class Argument<T> {

    private final String name;
    @Getter(AccessLevel.NONE)
    private final T value;
    private final String input;

    public T value() {
        return value;
    }
}
