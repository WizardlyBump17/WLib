package com.wizardlybump17.wlib.util.bukkit.config;

import com.wizardlybump17.wlib.util.builder.MapBuilder;
import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Data
@SerializableAs("WLib:Sound")
public class ConfigSound implements ConfigurationSerializable, Cloneable {

    private @NonNull String sound;
    private @NonNull SoundCategory category;
    private float volume;
    private float pitch;

    public ConfigSound(@NonNull String sound, @NonNull SoundCategory category, float volume, float pitch) {
        this.sound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(@NonNull Player player, @NonNull Location location) {
        player.playSound(location, sound, category, volume, pitch);
    }

    public void play(@NonNull Player player) {
        play(player, player.getLocation());
    }

    public void play(@NonNull Location location) {
        Objects.requireNonNull(location.getWorld(), "The location's world cannot be null").playSound(location, sound, category, volume, pitch);
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        return new MapBuilder<String, Object>(new LinkedHashMap<>())
                .put("sound", sound)
                .putIf("category", category.name(), category != SoundCategory.MASTER)
                .putIf("volume", volume, volume != 1)
                .putIf("pitch", pitch, pitch != 1)
                .build();
    }

    @Override
    public ConfigSound clone() {
        try {
            return (ConfigSound) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    public static @NonNull ConfigSound deserialize(@NonNull Map<@NonNull String, @Nullable Object> map) {
        return new ConfigSound(
                ConfigUtil.get("sound", map),
                ConfigUtil.get("category", map, SoundCategory.MASTER),
                ConfigUtil.<Number>get("volume", map, 1f).floatValue(),
                ConfigUtil.<Number>get("pitch", map, 1f).floatValue()
        );
    }
}
