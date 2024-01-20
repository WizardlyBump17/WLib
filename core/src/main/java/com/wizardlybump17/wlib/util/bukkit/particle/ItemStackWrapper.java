package com.wizardlybump17.wlib.util.bukkit.particle;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import com.wizardlybump17.wlib.util.bukkit.config.ConfigWrapper;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Data
@SerializableAs("WLib:Particle:ItemStack")
public class ItemStackWrapper implements ConfigurationSerializable, ConfigWrapper<ItemStack> {

    private final @NonNull ItemBuilder itemBuilder;

    @Override
    public ItemStack unwrap() {
        return itemBuilder.build();
    }

    @Override
    public @NonNull Map<String, Object> serialize() {
        return Map.of("item", itemBuilder);
    }

    public static @NonNull ItemStackWrapper deserialize(@NonNull Map<@NonNull String, @Nullable Object> map) {
        return new ItemStackWrapper(ConfigUtil.get("item", map));
    }
}
