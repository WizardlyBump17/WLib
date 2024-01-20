package com.wizardlybump17.wlib.util.bukkit.particle;

import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import com.wizardlybump17.wlib.util.bukkit.config.ConfigWrapper;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
@SerializableAs("WLib:Particle:BlockData")
public class BlockDataWrapper implements ConfigurationSerializable, ConfigWrapper<BlockData> {

    private final @NonNull BlockData blockData;

    @Override
    public BlockData unwrap() {
        return blockData;
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        return Map.of("block-data", blockData.getAsString(true));
    }

    public static @NonNull BlockDataWrapper deserialize(@NonNull Map<@NotNull String, @NotNull Object> map) {
        return new BlockDataWrapper(Bukkit.createBlockData(ConfigUtil.<String>get("block-data", map)));
    }
}
