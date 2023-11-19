package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.model.PotionMetaHandlerModel;
import lombok.NonNull;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PotionMetaHandler extends ItemMetaHandler<PotionMetaHandlerModel> {

    public PotionMetaHandler(PotionMetaHandlerModel model, ItemBuilder builder) {
        super(model, builder);
    }

    @Override
    public void serialize(Map<String, Object> map) {
        map.put("base-potion-data", basePotionData());
        map.put("custom-effects", customEffects());
        map.put("color", color());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deserialize(Map<String, Object> map) {
        PotionData basePotionData = (PotionData) map.get("base-potion-data");
        if (basePotionData != null)
            basePotionData(basePotionData);

        ((List<PotionEffect>) map.getOrDefault("custom-effects", Collections.emptyList())).forEach(effect -> customEffect(effect, true));
        color((Color) map.get("color"));
    }

    public PotionMetaHandler basePotionData(@NonNull PotionData data) {
        getBuilder().<PotionMeta>consumeMeta(meta -> meta.setBasePotionData(data));
        return this;
    }

    public PotionData basePotionData() {
        return getBuilder().getFromMeta(PotionMeta::getBasePotionData, (PotionData) null);
    }

    public boolean hasCustomEffects() {
        return getBuilder().getFromMeta(PotionMeta::hasCustomEffects, false);
    }

    public @NonNull List<PotionEffect> customEffects() {
        return getBuilder().getFromMeta(PotionMeta::getCustomEffects, Collections.emptyList());
    }

    public boolean customEffect(@NonNull PotionEffect effect, boolean overwrite) {
        return getBuilder().<Boolean, PotionMeta>getFromMeta(meta -> meta.addCustomEffect(effect, overwrite), false);
    }

    public boolean removeCustomEffect(@NonNull PotionEffectType type) {
        return getBuilder().<Boolean, PotionMeta>getFromMeta(meta -> meta.removeCustomEffect(type), false);
    }

    public boolean hasCustomEffect(@NonNull PotionEffectType type) {
        return getBuilder().<Boolean, PotionMeta>getFromMeta(meta -> meta.hasCustomEffect(type), false);
    }

    public boolean clearCustomEffects() {
        return getBuilder().getFromMeta(PotionMeta::clearCustomEffects, false);
    }

    public boolean hasColor() {
        return getBuilder().getFromMeta(PotionMeta::hasColor, false);
    }

    public @Nullable Color color() {
        return getBuilder().getFromMeta(PotionMeta::getColor, (Color) null);
    }

    public PotionMetaHandler color(@Nullable Color color) {
        getBuilder().<PotionMeta>consumeMeta(meta -> meta.setColor(color));
        return this;
    }
}
