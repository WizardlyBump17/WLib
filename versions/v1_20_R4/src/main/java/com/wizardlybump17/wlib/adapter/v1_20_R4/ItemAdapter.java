package com.wizardlybump17.wlib.adapter.v1_20_R4;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.persistence.CraftPersistentDataContainer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

    @Override
    public Map<String, Object> serializeContainer(PersistentDataContainer container) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PersistentDataContainer deserializeContainer(Map<String, Object> map) {
        throw new UnsupportedOperationException();
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
    public void applyGlow(@NonNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        meta.setEnchantmentGlintOverride(true);
        item.setItemMeta(meta);
    }

    @Override
    public void removeGlow(@NonNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return;

        meta.setEnchantmentGlintOverride(false);
        item.setItemMeta(meta);
    }

    @Override
    public boolean isGlowing(@NonNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;

        return meta.hasEnchantmentGlintOverride() && meta.getEnchantmentGlintOverride();
    }

    @Override
    public @NonNull Enchantment getGlowEnchantment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasGlowEnchantment() {
        return false;
    }
}