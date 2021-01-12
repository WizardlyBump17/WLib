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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Getter
public class ItemBuilder {

    private ItemStack itemStack;
    private final Material material;
    private Set<ItemFlag> itemFlags = new HashSet<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private int amount;
    private short durability;
    private String displayName;
    private List<String> lore = new ArrayList<>();
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
        itemStack = null;
    }

    public static ItemBuilder getHead(String base64, int amount) {
        try {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

            Object gameProfile = Class.forName("com.mojang.authlib.GameProfile")
                    .getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), null);
            Object properties = gameProfile.getClass().getDeclaredMethod("getProperties")
                    .invoke(gameProfile);

            properties.getClass().getSuperclass().getDeclaredMethod(
                    "put",
                    Object.class,
                    Object.class).invoke(
                            properties,
                    "textures",
                            Class.forName("com.mojang.authlib.properties.Property")
                                    .getConstructor(String.class, String.class).newInstance(
                                            "textures",
                                            base64));

            Field profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, gameProfile);

            itemStack.setItemMeta(itemMeta);
            return fromItemStack(itemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemBuilder fromItemStack(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder(
                itemStack.getType(),
                itemStack.getAmount(),
                itemStack.getDurability());
        itemBuilder.itemStack = itemStack;

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemBuilder.displayName = itemMeta.getDisplayName();
        itemBuilder.lore = itemMeta.getLore();
        itemBuilder.glow = hasNBTTag(itemStack, "ench");
        itemBuilder.enchantments = new HashMap<>(itemStack.getEnchantments());
        itemBuilder.unbreakable = itemMeta.spigot().isUnbreakable();
        itemBuilder.itemFlags = itemBuilder.getItemFlags();
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
        Material material = !config.isNull(path + ".skull-base64")
                ? Material.SKULL_ITEM
                : Material.valueOf(config.getString(path + ".material").toUpperCase());
        int amount = config.getInt(path + ".amount", 1);
        short durability = !config.isNull(path + ".skull-base64")
                ? 3
                : (short) config.getInt(path + ".durability", 0);
        String skullBase64 = config.getString(path + ".skull-base64", null);

        ItemBuilder itemBuilder = skullBase64 == null
                ? new ItemBuilder(material, amount, durability)
                : getHead(skullBase64, amount);

        Map<Enchantment, Integer> enchantments = new HashMap<>();
        if (!config.isNull(path + ".enchantments"))
            new Gson().fromJson(config.getString(path + ".enchantments"), Map.class)
                    .forEach((key, value) ->
                            enchantments.put(
                                    Enchantment.getByName(key.toString().toUpperCase()),
                                    (int) Double.parseDouble(value.toString())));

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
                .itemFlags(flags);
        return itemBuilder;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        if (itemStack != null) itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder durability(short durability) {
        this.durability = durability;
        if (itemStack != null) itemStack.setDurability(durability);
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

    public ItemBuilder itemFlags(ItemFlag... flags) {
        return itemFlags(new HashSet<>(Arrays.asList(flags)));
    }

    public ItemBuilder itemFlags(Set<ItemFlag> flags) {
        itemFlags = flags;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = this.itemStack == null
                ? new ItemStack(material, amount, durability) : this.itemStack;
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemStack.addUnsafeEnchantments(enchantments);
        itemMeta.setDisplayName(displayName == null
                ? null
                : displayName.replace('&', '§'));
        itemMeta.setLore(ListUtil.replace(lore, "&", "§"));
        itemMeta.spigot().setUnbreakable(isUnbreakable());
        itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[]{}));

        itemStack.setItemMeta(itemMeta);

        if (isGlow() && enchantments.isEmpty()) {
            try {
                itemStack = setNBTTag(
                        itemStack,
                        "ench",
                        Class.forName("net.minecraft.server.v1_8_R3.NBTTagList").newInstance());
                if ((displayName == null
                        || displayName.isEmpty())
                        && (lore == null || lore.isEmpty()))
                    itemStack = setNBTTag(
                            itemStack,
                            "HideFlags",
                            Class.forName("net.minecraft.server.v1_8_R3.NBTTagInt")
                                    .getConstructor(int.class).newInstance(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemStack;
    }

    public static <T> ItemStack setNBTTag(ItemStack itemStack, String key, T value) {
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
                    Class.forName("net.minecraft.server.v1_8_R3.NBTBase")).invoke(tag, key, value);

            nmsCopy.getClass().getDeclaredMethod(
                            "setTag",
                            Class.forName("net.minecraft.server.v1_8_R3.NBTTagCompound"))
                    .invoke(nmsCopy, tag);

            return (ItemStack) Class.forName("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack")
                            .getDeclaredMethod(
                                    "asCraftMirror",
                                    Class.forName(
                                            "net.minecraft.server.v1_8_R3.ItemStack")).invoke(null, nmsCopy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getNBTTag(ItemStack itemStack, String key) {
        try {
            Object nmsCopy = Class.forName("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack")
                    .getDeclaredMethod("asNMSCopy", ItemStack.class)
                    .invoke(null, itemStack);

            Object tag =
                    (boolean) nmsCopy.getClass().getDeclaredMethod("hasTag").invoke(nmsCopy)
                            ? nmsCopy.getClass().getDeclaredMethod("getTag").invoke(nmsCopy)
                            : Class.forName("net.minecraft.server.v1_8_R3.NBTTagCompound").newInstance();

            return tag.getClass().getDeclaredMethod("get", String.class).invoke(tag, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasNBTTag(ItemStack itemStack, String key) {
        return getNBTTag(itemStack, key) != null;
    }
}
