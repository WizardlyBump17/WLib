package com.wizardlybump17.wlib.item;

import com.google.gson.Gson;
import com.wizardlybump17.wlib.config.WConfig;
import com.wizardlybump17.wlib.reflection.Reflection;
import com.wizardlybump17.wlib.util.ListUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
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
import java.util.*;

@Getter
public class ItemBuilder {

    private static final List<String> DEFAULT_ITEM_NBT_TAGS = Arrays.asList(
            "display", "HideFlags", "ench", "Unbreakable", "SkullOwner", "AttributeModifiers",
            "CanDestroy", "PickupDelay", "Age", "generation", "Enchantments"
    );

    private ItemStack itemStack;
    private Material material;
    private Set<ItemFlag> itemFlags = new HashSet<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private int amount;
    private short durability;
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private boolean glow;
    private boolean unbreakable;
    private Map<String, Object> nbtTags = new HashMap<>();

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

    public static ItemBuilder getHead(UUID playerId, int amount) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        itemMeta.setOwner(Bukkit.getOfflinePlayer(playerId).getName());
        itemStack.setItemMeta(itemMeta);
        return fromItemStack(itemStack);
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
        itemBuilder.enchantments = new HashMap<>(itemStack.getEnchantments());
        itemBuilder.unbreakable = itemMeta.spigot().isUnbreakable();
        itemBuilder.itemFlags = itemBuilder.getItemFlags();

        try {
            Object nbtTag = getNBTTagCompound(itemStack);
            Field mapField = nbtTag.getClass().getDeclaredField("map");
            mapField.setAccessible(true);
            Map<String, Object> map = (Map<String, Object>) mapField.get(nbtTag);
            Map<String, Object> fixedMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet())
                if (!DEFAULT_ITEM_NBT_TAGS.contains(entry.getKey()))
                    fixedMap.put(entry.getKey(), entry.getValue());
            itemBuilder.nbtTags = fixedMap;
            itemBuilder.glow = map.containsKey("ench");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemBuilder;
    }

    private static Object getNBTTagCompound(ItemStack item) {
        try {
            Object nmsCopy = Reflection.getCraftBukkitClass("inventory.CraftItemStack")
                    .getDeclaredMethod("asNMSCopy", ItemStack.class)
                    .invoke(null, item);

            return (boolean) nmsCopy.getClass().getDeclaredMethod("hasTag").invoke(nmsCopy)
                            ? nmsCopy.getClass().getDeclaredMethod("getTag").invoke(nmsCopy)
                            : Reflection.getNMSClass("NBTTagCompound").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

        Map<String, Object> nbtTags = new HashMap<>();
        if (!config.isNull(path + ".nbt-tags")) {
            Map<String, Object> configTags = config.getMap(path + ".nbt-tags");
            Map<String, Object> fixedTags = new HashMap<>();
            for (Map.Entry<String, Object> entry : configTags.entrySet())
                if (!DEFAULT_ITEM_NBT_TAGS.contains(entry.getKey()))
                    fixedTags.put(entry.getKey(), entry.getValue());
            nbtTags = fixedTags;
        }

        itemBuilder
                .displayName(
                        config.isNull(path + ".display-name")
                                ? null
                                : config.getString(path + ".display-name")
                                .replace('&', '§'))
                .lore(new ListUtil(config.getStringList(path + ".lore")).replace('&', '§').getList())
                .enchantments(enchantments)
                .glow(config.getBoolean(path + ".glow"))
                .unbreakable(config.getBoolean(path + ".unbreakable"))
                .nbtTags(nbtTags)
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

    public ItemBuilder material(Material material) {
        this.material = material;
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

    public ItemBuilder nbtTag(String tag, Object value) {
        nbtTags.put(tag, value);
        return this;
    }

    public boolean hasNBTTag(String key) {
        return nbtTags.containsKey(key);
    }

    public Object getNBTTag(String key) {
        return Reflection.convertToNBTTag(nbtTags.get(key));
    }

    public ItemBuilder nbtTags(Map<String, Object> tags) {
        nbtTags = tags == null ? new HashMap<>() : tags;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = this.itemStack == null
                ? new ItemStack(material, amount, durability) : this.itemStack;
        itemStack.setType(material);
        itemStack.setAmount(amount);
        itemStack.setDurability(durability);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemStack.addUnsafeEnchantments(enchantments);
        itemMeta.setDisplayName(displayName == null
                ? null
                : displayName.replace('&', '§'));
        itemMeta.setLore(new ListUtil(lore).replace('&', '§').getList());
        itemMeta.spigot().setUnbreakable(isUnbreakable());
        itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[]{}));

        itemStack.setItemMeta(itemMeta);

        if (isGlow() && enchantments.isEmpty()) {
            nbtTag("ench", new ArrayList<>());
            if ((displayName == null || displayName.isEmpty()) && (lore == null || lore.isEmpty()))
                nbtTag("HideFlags", 1);
        }

        try {
            Object nmsStack = applyNBTTags(itemStack, nbtTags);
            itemStack = (ItemStack) Reflection.getCraftBukkitClass("inventory.CraftItemStack")
                    .getDeclaredMethod("asCraftMirror", Reflection.getNMSClass("ItemStack"))
                    .invoke(null, nmsStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemStack;
    }

    private static Object applyNBTTags(ItemStack itemStack, Map<String, Object> tags) {
        try {
            Object nmsCopy = Reflection.getCraftBukkitClass("inventory.CraftItemStack")
                    .getDeclaredMethod("asNMSCopy", ItemStack.class)
                    .invoke(null, itemStack);

            Object tag = getNBTTagCompound(itemStack);
            for (Map.Entry<String, Object> entry : tags.entrySet())
                tag.getClass().getDeclaredMethod(
                        "set",
                        String.class,
                        Reflection.getNMSClass("NBTBase"))
                        .invoke(tag, entry.getKey(), Reflection.convertToNBTTag(entry.getValue()));
            nmsCopy.getClass().getDeclaredMethod(
                    "setTag",
                    Reflection.getNMSClass("NBTTagCompound")).invoke(nmsCopy, tag);
            return nmsCopy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean hasNBTTag(ItemStack itemStack, String key) {
        try {
            Object tag = getNBTTagCompound(itemStack);
            return (boolean) tag.getClass().getDeclaredMethod("hasKey", String.class).invoke(tag, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
