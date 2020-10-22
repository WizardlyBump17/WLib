package com.wizardlybump17.wlib.item;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ItemBuilder {

    private final Material material;
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private int amount;
    private short durability;
    private String displayName;
    private List<String> lore;
    private boolean glow;
    private boolean hideAttributes;
    private boolean unbreakable;

    public ItemBuilder(Material material) {
        this(material, 1, (short) 0);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, (short) 0);
    }

    public ItemBuilder(Material material, int amount, short durability) {
        this.material = material;
        this.amount = amount;
        this.durability = durability;
    }

    public static ItemBuilder fromItemStack(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack.getType(), itemStack.getAmount(), itemStack.getDurability());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemBuilder
                .displayName(itemMeta.getDisplayName())
                .lore(itemMeta.getLore())
                .hideAttributes(itemMeta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES))
                .enchantments(itemStack.getEnchantments())
                .unbreakable(itemMeta.spigot().isUnbreakable());
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsStack.hasTag()) return itemBuilder;
        NBTTagCompound tag = nmsStack.getTag();
        NBTTagList ench = tag.getList("ench", 0);
        itemBuilder.glow(ench != null);
        return itemBuilder;
    }

    public static String toBase64(ItemStack itemStack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemBuilder fromBase64(String base64) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack itemStack = (ItemStack) dataInput.readObject();
            dataInput.close();
            return fromItemStack(itemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder durability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        enchantments.forEach(this::enchantment);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemBuilder hideAttributes(boolean hideAttributes) {
        this.hideAttributes = hideAttributes;
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);
        itemStack.addUnsafeEnchantments(enchantments);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if (isHideAttributes()) itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.spigot().setUnbreakable(isUnbreakable());
        itemStack.setItemMeta(itemMeta);
        if (isGlow()) {
            net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound tag = null;
            if (!nmsStack.hasTag()) {
                tag = new NBTTagCompound();
                nmsStack.setTag(tag);
            }
            if (tag == null) tag = nmsStack.getTag();
            NBTTagList ench = new NBTTagList();
            tag.set("ench", ench);
            nmsStack.setTag(tag);
            itemStack = CraftItemStack.asCraftMirror(nmsStack);
        }
        return itemStack;
    }
}
