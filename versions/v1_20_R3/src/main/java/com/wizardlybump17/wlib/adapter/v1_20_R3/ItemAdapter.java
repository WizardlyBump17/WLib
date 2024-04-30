package com.wizardlybump17.wlib.adapter.v1_20_R3;

import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.NonNull;
import net.minecraft.nbt.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_20_R3.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R3.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftNBTTagConfigSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.profile.PlayerProfile;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ItemAdapter extends com.wizardlybump17.wlib.adapter.ItemAdapter {

    public static final Field CACHE = ReflectionUtil.getField("cache", CraftRegistry.class);

    private final Map<String, PlayerProfile> profileCache = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> serializeContainer(PersistentDataContainer container) {
        return (Map<String, Object>) serialize(((CraftPersistentDataContainer) container).toTagCompound());
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

    public void registerGlowEnchantment() {
        Enchantment enchantment = getGlowEnchantment();
        Map<NamespacedKey, Enchantment> map = ReflectionUtil.getFieldValue(CACHE, Registry.ENCHANTMENT);
        map.put(enchantment.getKey(), enchantment);
    }

    public static Object serialize(NBTBase base) {
        if (base instanceof NBTTagCompound compound) {
            Map<String, Object> innerMap = new HashMap<>();
            for (String key : compound.e())
                innerMap.put(key, serialize(compound.c(key)));

            return innerMap;
        } else if (base instanceof NBTTagList list) {
            List<Object> baseList = new ArrayList<>();
            for (NBTBase nbtBase : list)
                baseList.add(serialize(nbtBase));
            return baseList;
        } else if (base instanceof NBTTagString)
            return base.t_();
        else if (base instanceof NBTTagInt)
            return base + "i";

        return base.toString();
    }

    @Override
    public @NonNull Enchantment getGlowEnchantment() {
        return GlowEnchantment.INSTANCE;
    }
}