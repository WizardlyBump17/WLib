package com.wizardlybump17.wlib.command.suggestion;

import com.wizardlybump17.wlib.command.exception.SuggesterException;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;

public class CachingSuggester<T> implements Suggester<T> {

    public static final long DEFAULT_EXPIRATION_MILLIS = 30 * 1000;

    private final @NotNull Suggester<T> delegate;
    private final long expirationMillis;
    private @NotNull @UnmodifiableView List<T> cache = List.of();
    private long lastUpdateMillis;

    private CachingSuggester(@NotNull Suggester<T> delegate, long expirationMillis) {
        this.delegate = delegate;
        this.expirationMillis = expirationMillis;
    }

    @Override
    public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input, @NotNull String current, @NotNull CommandNode<?> currentNode) throws SuggesterException {
        if (lastUpdateMillis + expirationMillis <= System.currentTimeMillis()) {
            List<T> suggestions = delegate.getSuggestions(sender, input, current, currentNode);
            cache = Collections.unmodifiableList(suggestions);
            lastUpdateMillis = System.currentTimeMillis();
            return suggestions;
        }

        return cache;
    }

    @Override
    public @NotNull String getStringRepresentation(@NotNull T value) {
        return delegate.getStringRepresentation(value);
    }

    public long expirationMillis() {
        return expirationMillis;
    }

    public @NotNull @UnmodifiableView List<T> cache() {
        return cache;
    }

    private long lastUpdateMillis() {
        return lastUpdateMillis;
    }

    public static <T> @NotNull CachingSuggester<T> of(@NotNull Suggester<T> suggester, long expirationMillis) {
        return new CachingSuggester<>(suggester, expirationMillis);
    }

    public static <T> @NotNull CachingSuggester<T> of(@NotNull Suggester<T> suggester) {
        return new CachingSuggester<>(suggester, DEFAULT_EXPIRATION_MILLIS);
    }
}
