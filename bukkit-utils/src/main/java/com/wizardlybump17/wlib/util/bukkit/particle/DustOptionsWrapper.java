package com.wizardlybump17.wlib.util.bukkit.particle;

import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import lombok.NonNull;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("WLib:Particle:DustOptions")
public class DustOptionsWrapper extends Particle.DustOptions implements ConfigurationSerializable {

    public DustOptionsWrapper(@NonNull Color color, float size) {
        super(color, size);
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("color", getColor());
        map.put("size", getSize());
        return map;
    }

    public static @NonNull DustOptionsWrapper deserialize(@NonNull Map<@NonNull String, @Nullable Object> map) {
        return new DustOptionsWrapper(
                ConfigUtil.get("color", map),
                ConfigUtil.<Number>get("size", map).floatValue()
        );
    }
}
