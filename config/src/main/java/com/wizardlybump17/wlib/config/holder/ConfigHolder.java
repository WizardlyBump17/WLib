package com.wizardlybump17.wlib.config.holder;

import com.wizardlybump17.wlib.config.Configuration;

/**
 * Basic interface for config holders
 * </code>
 */
public interface ConfigHolder<H, C extends Configuration<?>> {

    /**
     * @return the name of the holder
     */
    String getName();

    /**
     * @param name the name of the config
     * @return a config with the given name
     */
    C getConfig(String name);

    /**
     * @return the handle of this object
     */
    H getHandle();
}
