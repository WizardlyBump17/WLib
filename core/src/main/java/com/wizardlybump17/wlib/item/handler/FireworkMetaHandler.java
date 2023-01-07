package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.model.FireworkMetaHandlerModel;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FireworkMetaHandler extends ItemMetaHandler<FireworkMetaHandlerModel> {

    public FireworkMetaHandler(FireworkMetaHandlerModel model, ItemBuilder builder) {
        super(model, builder);
    }

    @Override
    public void serialize(Map<String, Object> map) {
        List<FireworkEffect> effects = getBuilder().getFromMeta(FireworkMeta::getEffects, Collections.emptyList());
        map.put("effects", effects.isEmpty() ? null : effects);
        int power = getBuilder().<Integer, FireworkMeta>getFromMeta(FireworkMeta::getPower, 0);
        map.put("power", power == 0 ? null : power);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deserialize(Map<String, Object> map) {
        getBuilder().<FireworkMeta>consumeMeta(meta -> {
            meta.addEffects((List<FireworkEffect>) map.getOrDefault("effects", Collections.emptyList()));
            meta.setPower((Integer) map.getOrDefault("power", 0));
        });
    }

    public FireworkMetaHandler effects(FireworkEffect... effects) {
        getBuilder().<FireworkMeta>consumeMeta(meta -> meta.addEffects(effects));
        return this;
    }

    public FireworkMetaHandler power(int power) {
        getBuilder().<FireworkMeta>consumeMeta(meta -> meta.setPower(power));
        return this;
    }

    public FireworkMetaHandler clearEffects() {
        getBuilder().consumeMeta(FireworkMeta::clearEffects);
        return this;
    }

    public List<FireworkEffect> effects() {
        return getBuilder().getFromMeta(FireworkMeta::getEffects, Collections.emptyList());
    }

    public int power() {
        return getBuilder().<Integer, FireworkMeta>getFromMeta(FireworkMeta::getPower, 0);
    }
}
