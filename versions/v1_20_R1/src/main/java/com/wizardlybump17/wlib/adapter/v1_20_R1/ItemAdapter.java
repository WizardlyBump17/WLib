package com.wizardlybump17.wlib.adapter.v1_20_R1;

import lombok.NonNull;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNBTTagConfigSerializer;
import org.bukkit.inventory.ItemStack;
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


    @Override
    public @NonNull ItemStack setRawNBTTag(@NonNull ItemStack item, @NonNull String key, @NonNull Object value) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.w();
        tag.a(key, NMSConverter.toNBT(value));
        return CraftItemStack.asBukkitCopy(stack);
    }

    @Override
    public @NonNull ItemStack setRawNBTTags(@NonNull ItemStack item, @NonNull Map<String, Object> tags) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        stack.c((NBTTagCompound) NMSConverter.toNBT(tags));
        return CraftItemStack.asBukkitCopy(stack);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Map<String, Object> getRawNBTTags(@NonNull ItemStack item) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = stack.v();
        if (tag == null)
            return new HashMap<>();

        return (Map<String, Object>) NMSConverter.fromNBT(tag);
    }
}