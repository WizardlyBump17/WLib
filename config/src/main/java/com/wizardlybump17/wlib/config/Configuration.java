package com.wizardlybump17.wlib.config;

public interface Configuration {

    Object get(String path, Object def);

    Object get(String path);

    <T> T getByType(String path, T def);

    <T> T getByType(String path);

    byte getByte(String path, byte def);

    byte getByte(String path);

    short getShort(String path, short def);

    short getShort(String path);

    int getInt(String path, int def);

    int getInt(String path);

    long getLong(String path, long def);

    long getLong(String path);

    float getFloat(String path, float def);

    float getFloat(String path);

    double getDouble(String path, double def);

    double getDouble(String path);

    boolean getBoolean(String path, boolean def);

    boolean getBoolean(String path);

    String getString(String path, String def);

    String getString(String path);

    Object get(String path, Object def, Class<?> type);

    Object get(String path, Class<?> type);

    void set(String path, Object value);

    void saveConfig();

    void saveDefaultConfig();

    void reloadConfig();
}
