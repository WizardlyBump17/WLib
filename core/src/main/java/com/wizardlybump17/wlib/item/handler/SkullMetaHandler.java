package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.model.SkullMetaHandlerModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;
import java.util.UUID;

public class SkullMetaHandler extends ItemMetaHandler<SkullMetaHandlerModel> {

    public SkullMetaHandler(SkullMetaHandlerModel model, ItemBuilder builder) {
        super(model, builder);
    }

    @Override
    public void serialize(Map<String, Object> map) {
        if (skullUrl() != null)
            map.put("skull", skullUrl());
        if (skullOwner() != null)
            map.put("owner", skullOwner().getUniqueId().toString());
    }

    @Override
    public void deserialize(Map<String, Object> map) {
        String skull = (String) map.get("skull");
        if (skull == null)
            return;

        try {
            skull(Bukkit.getOfflinePlayer(UUID.fromString(skull)));
        } catch (IllegalArgumentException ignored) {
            skull(skull);
        }
    }

    public String skullUrl() {
        return ItemAdapter.getInstance().getSkullUrl(getBuilder().getItemMeta());
    }

    public SkullMetaHandler skull(String url) {
        getBuilder().<SkullMeta>consumeMeta(meta -> ItemAdapter.getInstance().setSkull(meta, url));
        return this;
    }

    public OfflinePlayer skullOwner() {
        return getBuilder().getFromMeta(SkullMeta::getOwningPlayer, null);
    }

    public SkullMetaHandler skull(OfflinePlayer owner) {
       getBuilder().<SkullMeta>consumeMeta(meta -> meta.setOwningPlayer(owner));
       return this;
    }
}
