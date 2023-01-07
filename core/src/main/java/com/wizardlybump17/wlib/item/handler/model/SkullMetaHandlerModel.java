package com.wizardlybump17.wlib.item.handler.model;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.SkullMetaHandler;
import org.bukkit.Material;

import java.util.Set;

public class SkullMetaHandlerModel extends ItemMetaHandlerModel<SkullMetaHandler> {

    public SkullMetaHandlerModel() {
        super(Set.of(Material.PLAYER_HEAD));
    }

    @Override
    public SkullMetaHandler createHandler(ItemBuilder builder) {
        return new SkullMetaHandler(this, builder);
    }
}
