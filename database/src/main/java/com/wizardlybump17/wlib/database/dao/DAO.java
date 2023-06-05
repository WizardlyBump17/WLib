package com.wizardlybump17.wlib.database.dao;

import com.wizardlybump17.wlib.database.Database;
import lombok.Data;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Data
public abstract class DAO<K, V> {

    private final Database<?> database;
    private final ExecutorService executor;

    public abstract CompletableFuture<V> find(K key);

    public abstract CompletableFuture<Void> update(V value);

    /**
     * <p>Updates all values in the database.</p>
     * <p>The default implementation only updates the values if the {@code values} is a {@link Collection}.<br>
     * It uses the {@link CompletableFuture#allOf(CompletableFuture[])} to generate the {@link CompletableFuture} returned.
     * </p>
     * @param values the values to update
     * @return a {@link CompletableFuture} that will be completed when the update is done
     */
    public CompletableFuture<Void> updateAll(Iterable<V> values) {
        if (values instanceof Collection<V> collection)
            return CompletableFuture.allOf(collection.stream().map(this::update).toArray(CompletableFuture[]::new));
        throw new IllegalArgumentException("the given Iterable is not recognize by this method");
    }

    /**
     * <p>Updates all values in the database.</p>
     * <p>The default implementation uses the {@link CompletableFuture#allOf(CompletableFuture[])} to generate the {@link CompletableFuture} returned.</p>
     * @param values the values to update
     * @return a {@link CompletableFuture} that will be completed when the update is done
     */
    public CompletableFuture<Void> updateAll(V... values) {
        return CompletableFuture.allOf(Arrays.stream(values).map(this::update).toArray(CompletableFuture[]::new));
    }

    public abstract CompletableFuture<List<V>> fetchAll();

    public abstract void setupDatabase();
}
