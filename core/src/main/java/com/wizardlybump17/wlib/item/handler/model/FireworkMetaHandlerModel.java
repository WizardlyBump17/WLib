package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.FireworkMetaHandler;
import org.bukkit.Material;

import java.util.Set;

public class FireworkMetaHandlerModel extends ItemMetaHandlerModel<FireworkMetaHandler> {

    public FireworkMetaHandlerModel() {
        super(Set.of(Material.FIREWORK_ROCKET));
    }

    @Override
    public FireworkMetaHandler createHandler(ItemBuilder builder) {
        return new FireworkMetaHandler(this, builder);
    }
}
