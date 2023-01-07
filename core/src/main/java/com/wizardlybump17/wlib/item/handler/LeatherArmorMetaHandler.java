package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.model.LeatherArmorMetaHandlerModel;
import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Map;

public class LeatherArmorMetaHandler extends ItemMetaHandler<LeatherArmorMetaHandlerModel> {

    public LeatherArmorMetaHandler(LeatherArmorMetaHandlerModel model, ItemBuilder builder) {
        super(model, builder);
    }

    @Override
    public void serialize(Map<String, Object> map) {
        map.put("color", getBuilder().getFromMeta(LeatherArmorMeta::getColor, null));
    }

    @Override
    public void deserialize(Map<String, Object> map) {
        getBuilder().<LeatherArmorMeta>consumeMeta(meta -> meta.setColor((Color) map.get("color")));
    }

    public LeatherArmorMetaHandler color(Color color) {
        getBuilder().<LeatherArmorMeta>consumeMeta(meta -> meta.setColor(color));
        return this;
    }

    public Color color() {
        return getBuilder().getFromMeta(LeatherArmorMeta::getColor, null);
    }
}
