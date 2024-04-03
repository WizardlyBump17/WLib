package com.wizardlybump17.wlib.adapter.v1_17_R1;

import lombok.NonNull;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class PotionEffectTypeAdapter extends com.wizardlybump17.wlib.adapter.PotionEffectTypeAdapter {

    @Override
    public @Nullable PotionEffectType getEffectType(@NonNull String string) {
        return PotionEffectType.getByName(string);
    }
}
