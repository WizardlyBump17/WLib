package com.wizardlybump17.wlib.util.bukkit.config.wrapper.potion;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import com.wizardlybump17.wlib.util.builder.MapBuilder;
import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import com.wizardlybump17.wlib.util.bukkit.config.ConfigWrapper;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("WLib:Wrapper:PotionEffect")
public class PotionEffectWrapper extends PotionEffect implements ConfigWrapper<PotionEffect> {

    public static final Method GET_KEY = ReflectionUtil.getMethod("getKey", PotionEffectType.class);
    public static final Method GET_BY_KEY = ReflectionUtil.getMethod("getByKey", PotionEffectType.class, NamespacedKey.class);

    public PotionEffectWrapper(@NonNull PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        super(type, duration, amplifier, ambient, particles, icon);
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        return new MapBuilder<String, Object>(new LinkedHashMap<>())
                .putIf("type", () -> ReflectionUtil.<NamespacedKey>invokeMethod(GET_KEY, getType()).toString(), GET_KEY != null)
                .putIf("type", getType().getId(), GET_KEY == null)
                .put("duration", getDuration())
                .putIf("amplifier", getAmplifier(), getAmplifier() != 0)
                .putIf("ambient", isAmbient(), !isAmbient())
                .putIf("particles", hasParticles(), !hasParticles())
                .putIf("icon", hasIcon(), !hasIcon())
                .build();
    }

    @Override
    public PotionEffect unwrap() {
        return this;
    }

    public static @NonNull PotionEffectWrapper deserialize(@NonNull Map<String, Object> map) {
        return new PotionEffectWrapper(
                ConfigUtil.map("type", map, object -> {
                    if (object instanceof String string && GET_BY_KEY != null)
                        return ReflectionUtil.invokeMethod(GET_BY_KEY, null, NamespacedKey.fromString(string));

                    if (object instanceof Integer integer)
                        return PotionEffectType.getById(integer);

                    return null;
                }),
                ConfigUtil.get("duration", map),
                ConfigUtil.get("amplifier", map, 0),
                ConfigUtil.get("ambient", map, true),
                ConfigUtil.get("particles", map, true),
                ConfigUtil.get("icon", map, true)
        );
    }

    public static @NonNull PotionEffectWrapper fromBukkit(@NonNull PotionEffect bukkit) {
        return new PotionEffectWrapper(
                bukkit.getType(),
                bukkit.getDuration(),
                bukkit.getAmplifier(),
                bukkit.isAmbient(),
                bukkit.hasParticles(),
                bukkit.hasIcon()
        );
    }
}
