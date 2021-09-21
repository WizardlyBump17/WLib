package com.wizardlybump17.wlib.object;

import lombok.Data;

@Data
public class Pair<T, U> {

    private final T first;
    private final U second;
}
