package com.wizardlybump17.wlib.item;

import com.google.gson.Gson;
import com.wizardlybump17.wlib.config.WConfig;
import com.wizardlybump17.wlib.list.ListUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

@Getter
public class ItemBuilder {

    private final Material material;
    private final Set<ItemFlag> itemFlags = new HashSet<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private int amount;
    private short durability;
    private String displayName;
    private List<String> lore;
    private boolean glow;
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
        try {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemBuilder
                    .displayName(itemMeta.getDisplayName())
                    .lore(itemMeta.getLore())
                    .addItemFlags(itemMeta.getItemFlags())
                    .enchantments(itemStack.getEnchantments())
                    .unbreakable(itemMeta.spigot().isUnbreakable());

            Object nmsCopy = Class.forName(
                    "org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack")
                    .getDeclaredMethod("asNMSCopy", ItemStack.class)
                    .invoke(null, itemStack);

            Object tag =
                    (boolean) nmsCopy.getClass().getDeclaredMethod("hasTag").invoke(nmsCopy)
                            ? nmsCopy.getClass().getDeclaredMethod("getTag").invoke(nmsCopy)
                            : Class.forName("net.minecraft.server.v1_8_R3.NBTTagCompound")
                            .newInstance();
            itemBuilder.glow((boolean) tag.getClass().getDeclaredMethod("hasKey", String.class).invoke(tag, "ench"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @SuppressWarnings("unchecked")
    public static ItemBuilder fromConfig(WConfig config, String path) {
        ItemBuilder itemBuilder = new ItemBuilder(
                Material.valueOf(config.getString(path + ".material").toUpperCase()),
                config.getInt(path + ".amount", 1),
                (short) config.getInt(path + ".durability", 0));

        Map<Enchantment, Integer> enchantments = new HashMap<>();
        if (!config.isNull(path + ".enchantments"))
            new Gson().fromJson(config.getString(path + ".enchantments"), Map.class)
                    .forEach((key, value) -> enchantments.put(Enchantment.getByName(key.toString().toUpperCase()), (int) Double.parseDouble(value.toString())));

        Set<ItemFlag> flags = new HashSet<>();
        if (!config.isNull(path + ".item-flags"))
            for (String flagName : config.getStringList(path + ".item-flags"))
                flags.add(ItemFlag.valueOf(flagName.toUpperCase()));

        itemBuilder
                .displayName(
                        config.isNull(path + ".display-name")
                                ? null
                                : config.getString(path + ".display-name")
                                .replace('&', '§'))
                .lore(ListUtil.replace(
                        config.getStringList(path + ".lore"), "&", "§"))
                .enchantments(enchantments)
                .glow(config.getBoolean(path + ".glow"))
                .unbreakable(config.getBoolean(path + ".unbreakable"))
                .addItemFlags(flags);
        return itemBuilder;
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
        this.enchantments = enchantments == null ? new HashMap<>() : enchantments;
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        itemFlags.addAll(Arrays.asList(flags));
        return this;
    }

    public ItemBuilder addItemFlags(Set<ItemFlag> flags) {
        itemFlags.addAll(flags);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag flag) {
        itemFlags.remove(flag);
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, durability);
        itemStack.addUnsafeEnchantments(enchantments);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[]{}));
        itemMeta.spigot().setUnbreakable(isUnbreakable());
        itemStack.setItemMeta(itemMeta);
        if (isGlow() && enchantments.isEmpty()) {
            try {
                Object nmsCopy = Class.forName(
                        "org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack")
                        .getDeclaredMethod("asNMSCopy", ItemStack.class)
                        .invoke(null, itemStack);

                Object tag =
                        (boolean) nmsCopy.getClass().getDeclaredMethod("hasTag").invoke(nmsCopy)
                                ? nmsCopy.getClass().getDeclaredMethod("getTag").invoke(nmsCopy)
                                : Class.forName("net.minecraft.server.v1_8_R3.NBTTagCompound")
                                .newInstance();


                tag.getClass().getDeclaredMethod(
                        "set",
                        String.class,
                        Class.forName("net.minecraft.server.v1_8_R3.NBTBase")).invoke(
                        tag, "ench", Class.forName("net.minecraft.server.v1_8_R3.NBTTagList").newInstance());

                if ((displayName == null
                        || displayName.isEmpty())
                        && (lore == null || lore.isEmpty()))
                    tag.getClass().getDeclaredMethod(
                            "set",
                            String.class,
                            Class.forName("net.minecraft.server.v1_8_R3.NBTBase")).invoke(
                            tag, "HideFlags", Class.forName("net.minecraft.server.v1_8_R3.NBTTagInt").getConstructor(int.class).newInstance(1));

                nmsCopy.getClass().getDeclaredMethod(
                        "setTag",
                        Class.forName("net.minecraft.server.v1_8_R3.NBTTagCompound"))
                        .invoke(nmsCopy, tag);

                itemStack = (ItemStack)
                        Class.forName("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack")
                                .getDeclaredMethod(
                                        "asCraftMirror",
                                        Class.forName(
                                                "net.minecraft.server.v1_8_R3.ItemStack"))
                                .invoke(null, nmsCopy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemStack;
    }
}
