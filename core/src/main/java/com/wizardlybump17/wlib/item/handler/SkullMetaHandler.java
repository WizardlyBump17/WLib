package com.wizardlybump17.wlib.item.handler;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.wizardlybump17.wlib.item.ItemBuilder;
import com.wizardlybump17.wlib.item.handler.model.SkullMetaHandlerModel;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkullMetaHandler extends ItemMetaHandler<SkullMetaHandlerModel> {

    private final @NonNull Map<String, PlayerProfile> profileCache = new ConcurrentHashMap<>();

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
        return getBuilder().<String, SkullMeta>getFromMeta(meta -> {
            PlayerProfile profile = meta.getPlayerProfile();
            if (profile == null)
                return null;

            URL skin = profile.getTextures().getSkin();
            return skin == null ? null : skin.toString();
        }, () -> null);
    }

    public SkullMetaHandler skull(String url) {
        getBuilder().<SkullMeta>consumeMeta(meta -> meta.setPlayerProfile(profileCache.computeIfAbsent(url, $ -> {
            try {
                PlayerProfile profile = Bukkit.createProfile(UUID.nameUUIDFromBytes(url.getBytes()));
                profile.getTextures().setSkin(URI.create(url).toURL());
                return profile;
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid URL " + url, e);
            }
        })));
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
