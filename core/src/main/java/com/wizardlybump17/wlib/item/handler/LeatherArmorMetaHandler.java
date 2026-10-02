package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.handler.model.LeatherArmorMetaHandlerModel;
import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LeatherArmorMetaHandler extends ItemMetaHandler<LeatherArmorMetaHandlerModel> {

    public LeatherArmorMetaHandler(LeatherArmorMetaHandlerModel model, LeatherArmorMeta itemMeta) {
        super(model, itemMeta);
    }

    @Override
    public @NotNull LeatherArmorMeta getItemMeta() {
        return (LeatherArmorMeta) super.getItemMeta();
    }

    @Override
    public void serialize(@NotNull Map<String, Object> map) {
        LeatherArmorMeta itemMeta = getItemMeta();

        map.put("color", itemMeta.getColor());
    }

    @Override
    public void deserialize(@NotNull Map<String, Object> map) {
        LeatherArmorMeta itemMeta = getItemMeta();

        itemMeta.setColor(getColor(map.get("color")));
    }

    public @NotNull LeatherArmorMetaHandler color(@NotNull Color color) {
        getItemMeta().setColor(color);
        return this;
    }

    public @NotNull Color color() {
        return getItemMeta().getColor();
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
