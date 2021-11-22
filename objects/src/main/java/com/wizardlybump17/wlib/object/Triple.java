package com.wizardlybump17.wlib.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Triple<F, S, T> {

    private F first;
    private S second;
    private T third;
}
