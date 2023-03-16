package com.wizardlybump17.wlib.adapter.v1_19_R3;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNBTTagConfigSerializer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    private final Map<String, PlayerProfile> profileCache = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> serializeContainer(PersistentDataContainer container) {
        return (Map<String, Object>) CraftNBTTagConfigSerializer.serialize(((CraftPersistentDataContainer) container).toTagCompound());
    }

    @Override
    public PersistentDataContainer deserializeContainer(Map<String, Object> map) {
        CraftPersistentDataContainer container = (CraftPersistentDataContainer) PERSISTENT_DATA_ADAPTER_CONTEXT.newPersistentDataContainer();
        container.putAll((NBTTagCompound) CraftNBTTagConfigSerializer.deserialize(map));
        return container;
    }

    @Override
    public void transferPersistentData(PersistentDataContainer from, PersistentDataContainer to) {
        CraftPersistentDataContainer craftTo = (CraftPersistentDataContainer) to;
        craftTo.getRaw().clear();
        craftTo.putAll(((CraftPersistentDataContainer) from).toTagCompound());
    }

    @Override
    public void copyPersistentData(PersistentDataContainer from, PersistentDataContainer to) {
        ((CraftPersistentDataContainer) to).putAll(((CraftPersistentDataContainer) from).toTagCompound());
    }

    @Override
    public void setSkull(SkullMeta meta, String url) {
        PlayerProfile profile = profileCache.computeIfAbsent(url, s -> {
            PlayerProfile playerProfile = Bukkit.getServer().createPlayerProfile(UUID.nameUUIDFromBytes(url.getBytes()));
            try {
                playerProfile.getTextures().setSkin(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return playerProfile;
        });

        meta.setOwnerProfile(profile);
    }

    @Override
    public String getSkullUrl(SkullMeta meta) {
        URL url;
        if (meta.getOwningPlayer() != null)
            url = meta.getOwningPlayer().getPlayerProfile().getTextures().getSkin();
        else if (meta.getOwnerProfile() != null)
            url = meta.getOwnerProfile().getTextures().getSkin();
        else
            return null;

        return url == null ? null : url.toString();
    }
}