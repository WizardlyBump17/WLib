package com.wizardlybump17.wlib.command.suggestion;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ValuesSuggester<T> extends Suggester<T> {
    
    @NotNull List<T> values();
}
