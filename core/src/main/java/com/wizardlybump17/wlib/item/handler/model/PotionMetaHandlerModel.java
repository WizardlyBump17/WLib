package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.PotionMetaHandler;
import org.bukkit.Material;

import java.util.Set;

public class PotionMetaHandlerModel extends ItemMetaHandlerModel<PotionMetaHandler> {

    public PotionMetaHandlerModel() {
        super(Set.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));
    }

    @Override
    public PotionMetaHandler createHandler(ItemBuilder builder) {
        return new PotionMetaHandler(this, builder);
    }
}
