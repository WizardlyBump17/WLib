package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.handler.model.FireworkMetaHandlerModel;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class FireworkMetaHandler extends ItemMetaHandler<FireworkMetaHandlerModel> {

    public FireworkMetaHandler(FireworkMetaHandlerModel model, @NotNull FireworkMeta itemMeta) {
        super(model, itemMeta);
    }

    @Override
    public @NotNull FireworkMeta getItemMeta() {
        return (FireworkMeta) super.getItemMeta();
    }

    @Override
    public void serialize(Map<String, Object> map) {
        FireworkMeta itemMeta = getItemMeta();

        List<FireworkEffect> effects = itemMeta.getEffects();
        if (!effects.isEmpty())
            map.put("effects", effects);

        int power = itemMeta.getPower();
        map.put("power", power);
    }

    @Override
    public void deserialize(Map<String, Object> map) {
        FireworkMeta itemMeta = getItemMeta();

        itemMeta.addEffects((FireworkEffect) map.getOrDefault("effects", List.of()));
        itemMeta.setPower((int) map.getOrDefault("power", 0));
    }

    public FireworkMetaHandler effects(FireworkEffect... effects) {
        FireworkMeta itemMeta = getItemMeta();
        itemMeta.clearEffects();
        itemMeta.addEffects(effects);
        return this;
    }

    public FireworkMetaHandler power(int power) {
        getItemMeta().setPower(power);
        return this;
    }

    public FireworkMetaHandler clearEffects() {
        getItemMeta().clearEffects();
        return this;
    }

    public List<FireworkEffect> effects() {
        return getItemMeta().getEffects();
    }

    public int power() {
        return getItemMeta().getPower();
    }
}
