package com.wizardlybump17.wlib.database.controller;

import com.wizardlybump17.wlib.database.dao.DAO;
import com.wizardlybump17.wlib.object.Cache;
import lombok.Data;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 *     A controller, in this context, is the middleman between the cache and the database.
 *     It's used to get data from the cache and if it's not present there, it will get it from the database and cache it.
 * </p>
 * <p>
 *     It follows this order:
 *     <ol>
 *         <li>Get the data from the cache</li>
 *         <li>If it's not present in the cache, get it from the database</li>
 *         <li>If the data was found, then cache it using the {@link #cache(Object)} method</li>
 *     </ol>
 * </p>
 * @param <K> the key type
 * @param <V> the value type
 * @param <C> the {@link Cache} type
 * @param <D> the {@link DAO} type
 */
@Data
public abstract class Controller<K, V, C extends Cache<K, V, ?>, D extends DAO<K, V>> {

    @NonNull
    private final C cache;
    @NonNull
    private final D dao;
    private final @NonNull Logger logger;

    public Controller(@NonNull C cache, @NonNull D dao) {
        this.cache = cache;
        this.dao = dao;
        this.logger = Logger.getLogger(getClass().getSimpleName());
    }

    public Controller(@NonNull C cache, @NonNull D dao, @NonNull Logger logger) {
        this.cache = cache;
        this.dao = dao;
        this.logger = logger;
    }

    /**
     * <p>Gets the value from the cache and if it's not present there, gets it from the database.</p>
     * @param key the key
     * @return a {@link CompletableFuture} that will be completed with the value
     */
    public CompletableFuture<V> get(@NonNull K key) {
        return cache.get(key)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> dao.find(key).whenComplete((value, throwable) -> {
                    if (throwable != null) {
                        onGetError(key, throwable);
                        return;
                    }

                    if (value == null)
                        return;

                    cache(value);
                }));
    }

    protected void onGetError(@NonNull K key, @NonNull Throwable throwable) {
        getLogger().log(Level.SEVERE, "Error while getting the value with the key " + key, throwable);
    }

    /**
     * <p>Caches the value in the {@link #getCache()}.</p>
     * @param value the value
     */
    public abstract void cache(@NonNull V value);

    /**
     * <p>Caches all the given values in the {@link #getCache()}</p>
     * @param values the values
     */
    public void cacheAll(@NonNull Iterable<V> values) {
        for (V value : values)
            cache(value);
    }

    /**
     * <p>
     *     Called when {@code this} {@link Controller} shutdowns.
     *     The default implementation calls the {@link #save()} method and waits for the {@link CompletableFuture} to complete.
     * </p>
     * @apiNote this method is not called natively by WLib, you need to call it manually
     */
    public void shutdown() {
        save().join();
    }

    /**
     * <p>
     *     Called when {@code this} {@link Controller} is requested to save all the data.
     *     The Spigot implementation of WLib will call this method every 5 minutes.
     * </p>
     */
    public CompletableFuture<Void> save() {
        return CompletableFuture.allOf(cache.getAll().stream().map(dao::update).toArray(CompletableFuture[]::new));
    }

    @Override
    public int hashCode() {
        return Controller.class.hashCode();
    }
}
