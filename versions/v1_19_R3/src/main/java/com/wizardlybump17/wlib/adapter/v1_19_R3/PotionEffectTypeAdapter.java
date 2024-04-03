package com.wizardlybump17.wlib.adapter.v1_19_R3;

import com.wizardlybump17.wlib.util.bukkit.NamespacedKeyUtil;
import lombok.NonNull;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class PotionEffectTypeAdapter extends com.wizardlybump17.wlib.adapter.PotionEffectTypeAdapter {

    @Override
    public @Nullable PotionEffectType getEffectType(@NonNull String string) {
        NamespacedKey key = NamespacedKeyUtil.fromString(string);
        return key == null ? PotionEffectType.getByName(string) : PotionEffectType.getByKey(key);
    }
}
