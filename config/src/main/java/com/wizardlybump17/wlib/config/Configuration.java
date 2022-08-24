package com.wizardlybump17.wlib.config;

/**
 * Common interface for the configuration classes
 * @param <H> the type of the handle
 */
public interface Configuration<H> {

    Object get(String path, Object def);

    Object get(String path);

    <T> T getByType(String path, T def);

    <T> T getByType(String path);

    Object get(String path, Object def, Class<?> type);

    Object get(String path, Class<?> type);

    Object get(String path, Object def, Class<?> type, Path requester);

    Object get(String path, Class<?> type, Path requester);

    void set(String path, Object value);

    void saveConfig();

    void saveDefaultConfig();

    void reloadConfig();

    H getHandle();

    boolean isSet(String path);
}
