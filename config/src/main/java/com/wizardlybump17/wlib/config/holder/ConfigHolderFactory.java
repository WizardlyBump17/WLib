package com.wizardlybump17.wlib.config.holder;

public abstract class ConfigHolderFactory {

    public abstract ConfigHolder create(Class<?> clazz);
}
