package com.wizardlybump17.wlib.config.holder;

public abstract class ConfigHolderFactory {

    public abstract String getType();

    public abstract ConfigHolder create(String name);
}
