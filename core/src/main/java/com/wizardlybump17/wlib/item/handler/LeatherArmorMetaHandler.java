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
        map.put("color", getBuilder().getFromMeta(LeatherArmorMeta::getColor, (Color) null));
    }

    @Override
    public void deserialize(Map<String, Object> map) {
        getBuilder().<LeatherArmorMeta>consumeMeta(meta -> meta.setColor(getColor(map.get("color"))));
    }

    public LeatherArmorMetaHandler color(Color color) {
        getBuilder().<LeatherArmorMeta>consumeMeta(meta -> meta.setColor(color));
        return this;
    }

    public Color color() {
        return getBuilder().getFromMeta(LeatherArmorMeta::getColor, (Color) null);
    }

    private static Color getColor(Object object) {
        if (object instanceof Color color)
            return color;

        if (object instanceof Number number)
            return Color.fromRGB(number.intValue());

        if (object instanceof String string) {
            if (string.startsWith("#"))
                return Color.fromRGB(Integer.parseInt(string.substring(1), 16));
            else
                return Color.fromRGB(Integer.parseInt(string, 16));
        }

        return null;
    }
}
