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

@SerializableAs("WLib:Particle:DustTransition")
public class DustTransitionWrapper extends Particle.DustTransition implements ConfigurationSerializable {

    public DustTransitionWrapper(@NonNull Color from, @NonNull Color to, float size) {
        super(from, to, size);
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("from", getColor());
        map.put("to", getToColor());
        map.put("size", getSize());
        return map;
    }

    public static @NonNull DustTransitionWrapper deserialize(@NonNull Map<@NonNull String, @Nullable Object> map) {
        return new DustTransitionWrapper(
                ConfigUtil.get("from", map),
                ConfigUtil.get("to", map),
                ConfigUtil.<Number>get("size", map).floatValue()
        );
    }
}
