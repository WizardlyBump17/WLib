package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.handler.model.PotionMetaHandlerModel;
import com.wizardlybump17.wlib.util.bukkit.config.wrapper.potion.PotionDataWrapper;
import lombok.NonNull;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PotionMetaHandler extends ItemMetaHandler<PotionMetaHandlerModel> {

    public PotionMetaHandler(@NotNull PotionMetaHandlerModel model, @NotNull PotionMeta itemMeta) {
        super(model, itemMeta);
    }

    @Override
    public @NotNull PotionMeta getItemMeta() {
        return (PotionMeta) super.getItemMeta();
    }

    @Override
    public void serialize(Map<String, Object> map) {
        map.put("base-potion-data", PotionDataWrapper.fromBukkit(basePotionData()));
        map.put("custom-effects", customEffects());
        map.put("color", color());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deserialize(Map<String, Object> map) {
        PotionDataWrapper basePotionData = (PotionDataWrapper) map.get("base-potion-data");
        if (basePotionData != null)
            basePotionData(basePotionData.unwrap());

        ((List<PotionEffect>) map.getOrDefault("custom-effects", Collections.emptyList())).forEach(effect -> customEffect(effect, true));
        color((Color) map.get("color"));
    }

    public PotionMetaHandler basePotionData(@NonNull PotionData data) {
        getItemMeta().setBasePotionData(data);
        return this;
    }

    public PotionData basePotionData() {
        return getItemMeta().getBasePotionData();
    }

    public boolean hasCustomEffects() {
        return getItemMeta().hasCustomEffects();
    }

    public @NonNull List<PotionEffect> customEffects() {
        return getItemMeta().getCustomEffects();
    }

    public boolean customEffect(@NonNull PotionEffect effect, boolean overwrite) {
        return getItemMeta().addCustomEffect(effect, overwrite);
    }

    public boolean removeCustomEffect(@NonNull PotionEffectType type) {
        return getItemMeta().removeCustomEffect(type);
    }

    public boolean hasCustomEffect(@NonNull PotionEffectType type) {
        return getItemMeta().hasCustomEffect(type);
    }

    public boolean clearCustomEffects() {
        return getItemMeta().clearCustomEffects();
    }

    public boolean hasColor() {
        return getItemMeta().hasColor();
    }

    public @Nullable Color color() {
        return getItemMeta().getColor();
    }

    public PotionMetaHandler color(@Nullable Color color) {
        getItemMeta().setColor(color);
        return this;
    }
}
