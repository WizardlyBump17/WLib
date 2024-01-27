package com.wizardlybump17.wlib.util.bukkit.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface ConfigWrapper<T> extends ConfigurationSerializable {

    T unwrap();
}
