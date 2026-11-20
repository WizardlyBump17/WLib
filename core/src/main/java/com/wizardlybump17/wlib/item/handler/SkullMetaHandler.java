package com.wizardlybump17.wlib.item.handler;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.wizardlybump17.wlib.item.handler.model.SkullMetaHandlerModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class SkullMetaHandler extends ItemMetaHandler<SkullMetaHandlerModel> {

    public SkullMetaHandler(@NotNull SkullMetaHandlerModel model, @NotNull SkullMeta itemMeta) {
        super(model, itemMeta);
    }

    @Override
    public @NotNull SkullMeta getItemMeta() {
        return (SkullMeta) super.getItemMeta();
    }

    @Override
    public void serialize(Map<String, Object> map) {
        String skullUrl = skullUrl();
        if (skullUrl != null)
            map.put("skull", skullUrl);

        OfflinePlayer skullOwner = skullOwner();
        if (skullOwner != null)
            map.put("owner", skullOwner.getUniqueId().toString());
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

    public @Nullable String skullUrl() {
        PlayerProfile profile = getItemMeta().getPlayerProfile();
        if (profile == null)
            return null;

        URL skin = profile.getTextures().getSkin();
        return skin == null ? null : skin.toString();
    }

    public @NotNull SkullMetaHandler skull(@Nullable String url) {
        SkullMeta itemMeta = getItemMeta();

        if (url == null) {
            PlayerProfile profile = itemMeta.getPlayerProfile();
            if (profile == null)
                return this;

            PlayerTextures textures = profile.getTextures();

            textures.setSkin(null);
            profile.setTextures(textures);

            itemMeta.setPlayerProfile(profile);
            return this;
        }

        try {
            PlayerProfile profile = Bukkit.createProfile(UUID.nameUUIDFromBytes(url.getBytes()));

            PlayerTextures textures = profile.getTextures();
            textures.setSkin(URI.create(url).toURL());

            profile.setTextures(textures);

            itemMeta.setPlayerProfile(profile);
            return this;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL " + url, e);
        }
    }

    public @Nullable OfflinePlayer skullOwner() {
        return getItemMeta().getOwningPlayer();
    }

    public @NotNull SkullMetaHandler skull(@Nullable OfflinePlayer owner) {
       getItemMeta().setOwningPlayer(owner);
       return this;
    }
}
