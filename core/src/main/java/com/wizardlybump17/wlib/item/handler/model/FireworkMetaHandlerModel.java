package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.FireworkMetaHandler;
import org.bukkit.Material;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FireworkMetaHandlerModel extends ItemMetaHandlerModel<FireworkMetaHandler> {

    public FireworkMetaHandlerModel() {
        super(Set.of(Material.FIREWORK_ROCKET));
    }

    @Override
    public FireworkMetaHandler createHandler(ItemBuilder builder) {
        return new FireworkMetaHandler(this, builder.getItemMeta());
    }

    @Override
    public @NotNull FireworkMetaHandler createHandler(@NotNull ItemMeta itemMeta) {
        return new FireworkMetaHandler(this, (FireworkMeta) itemMeta);
    }
}
