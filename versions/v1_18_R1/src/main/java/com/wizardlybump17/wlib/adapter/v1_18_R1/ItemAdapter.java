package com.wizardlybump17.wlib.adapter.v1_18_R1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_18_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftNBTTagConfigSerializer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    private final Map<String, GameProfile> skullCache = new HashMap<>();

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
    public void setSkull(SkullMeta meta, String url) {
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(
                    meta,
                    skullCache.computeIfAbsent(url, s -> {
                        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(url.getBytes()), url);
                        profile.getProperties().put(
                                "textures",
                                new Property("textures", new String(Base64.getEncoder().encode(("{textures:{SKIN:{url:\"" + url + "\"}}}").getBytes())))
                        );
                        return profile;
                    })
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSkullUrl(SkullMeta meta) {
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            GameProfile profile = (GameProfile) field.get(meta);
            return profile.getProperties().get("textures").iterator().next().getValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}