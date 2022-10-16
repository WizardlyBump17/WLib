package com.wizardlybump17.wlib.util.bukkit;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@SerializableAs("number-formatter")
public class NumberFormatter extends com.wizardlybump17.wlib.util.NumberFormatter implements ConfigurationSerializable {

    public NumberFormatter(List<String> suffixes) {
        super(suffixes);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of("suffixes", getSuffixes());
    }

    @SuppressWarnings("unchecked")
    public static NumberFormatter deserialize(Map<String, Object> map) {
        return new NumberFormatter((List<String>) map.get("suffixes"));
    }
}
