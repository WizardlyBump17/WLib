package com.wizardlybump17.wlib.adapter;

import lombok.NonNull;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public abstract class PotionEffectTypeAdapter {

    private static PotionEffectTypeAdapter instance;

    public static void setInstance(@NonNull PotionEffectTypeAdapter instance) {
        if (PotionEffectTypeAdapter.instance != null)
            throw new IllegalStateException("Instance is already set");
        PotionEffectTypeAdapter.instance = instance;
    }

    public abstract @Nullable PotionEffectType getEffectType(@NonNull String string);
}
