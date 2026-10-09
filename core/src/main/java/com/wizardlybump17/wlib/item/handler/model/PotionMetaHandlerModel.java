package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.PotionMetaHandler;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PotionMetaHandlerModel extends ItemMetaHandlerModel<PotionMetaHandler> {

    public PotionMetaHandlerModel() {
        super(Set.of(Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION));
    }

    @Override
    public PotionMetaHandler createHandler(ItemBuilder builder) {
        return new PotionMetaHandler(this, builder.getItemMeta());
    }

    @Override
    public @NotNull PotionMetaHandler createHandler(@NotNull ItemMeta itemMeta) {
        return new PotionMetaHandler(this, (PotionMeta) itemMeta);
    }
}
