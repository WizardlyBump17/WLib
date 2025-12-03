package com.wizardlybump17.wlib.item.handler;

import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.model.SkullMetaHandlerModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
        if (skull != null) {
            skull(skull);
            return;
        }

        String owner = (String) map.get("owner");
        if (owner != null)
            skull(Bukkit.getOfflinePlayer(UUID.fromString(owner)));
    }

    public String skullUrl() {
        return getBuilder().<String, SkullMeta>getFromMeta(meta -> {
            PlayerProfile profile = meta.getOwnerProfile();
            if (profile == null)
                return null;

            URL skin = profile.getTextures().getSkin();
            return skin == null ? null : skin.toString();
        }, () -> null);
    }

    public SkullMetaHandler skull(String url) {
        getBuilder().<SkullMeta>consumeMeta(meta -> {
            try {
                PlayerProfile profile = Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(url.getBytes()));

                PlayerTextures textures = profile.getTextures();
                textures.setSkin(URI.create(url).toURL());

                profile.setTextures(textures);

                meta.setOwnerProfile(profile);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid URL " + url, e);
            }
        });
        return this;
    }

    public OfflinePlayer skullOwner() {
        return getBuilder().getFromMeta(SkullMeta::getOwningPlayer, (OfflinePlayer) null);
    }

    public SkullMetaHandler skull(OfflinePlayer owner) {
       getBuilder().<SkullMeta>consumeMeta(meta -> meta.setOwningPlayer(owner));
       return this;
    }
}
