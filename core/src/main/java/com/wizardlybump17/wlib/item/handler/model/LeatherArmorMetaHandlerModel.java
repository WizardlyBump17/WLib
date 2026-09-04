package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.LeatherArmorMetaHandler;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class LeatherArmorMetaHandlerModel extends ItemMetaHandlerModel<LeatherArmorMetaHandler> {

    public LeatherArmorMetaHandlerModel() {
        super(Set.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS));
    }

    @Override
    public LeatherArmorMetaHandler createHandler(ItemBuilder builder) {
        return new LeatherArmorMetaHandler(this, builder.getItemMeta());
    }

    @Override
    public @NotNull LeatherArmorMetaHandler createHandler(@NotNull ItemMeta itemMeta) {
        return new LeatherArmorMetaHandler(this, (LeatherArmorMeta) itemMeta);
    }
}
