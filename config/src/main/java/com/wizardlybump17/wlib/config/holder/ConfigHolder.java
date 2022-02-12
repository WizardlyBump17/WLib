package com.wizardlybump17.wlib.config.holder;

import com.wizardlybump17.wlib.config.Configuration;

/**
 * Basic interface for config holders
 * </code>
 */
public interface ConfigHolder {

    String getName();

    Configuration getConfig(String name);

    Object getHandle();
}
