package com.wizardlybump17.wlib.config.holder;

/**
 * This class is used to create config holders
 */
public abstract class ConfigHolderFactory {

    /**
     * Creates a new config holder based on the given class
     * @param clazz the class of the holder
     * @return the config holder
     */
    public abstract ConfigHolder create(Class<?> clazz);
}
