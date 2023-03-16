package com.wizardlybump17.wlib.database.dao;

import com.wizardlybump17.wlib.database.Database;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Data
public abstract class DAO<K, V> {

    private final Database<?> database;
    private final ExecutorService executor;

    public abstract CompletableFuture<V> find(K key);

    public abstract CompletableFuture<Void> update(V value);

    public abstract CompletableFuture<List<V>> fetchAll();

    public abstract void setupDatabase();
}
