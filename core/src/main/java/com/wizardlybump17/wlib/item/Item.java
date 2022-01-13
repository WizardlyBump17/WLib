package com.wizardlybump17.wlib.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.adapter.WMaterial;
import com.wizardlybump17.wlib.util.CollectionUtil;
import com.wizardlybump17.wlib.util.MapUtils;
import com.wizardlybump17.wlib.util.StringUtil;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
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
import java.util.stream.Collectors;

//TODO refactor. Move the builder to a proper class
@Data
@Builder
public class Item {

    private static final Map<UUID, ItemStack> HEADS = new HashMap<>();
    private static final NMSAdapter ADAPTER = NMSAdapterRegister.getInstance().current();

    private Material type;
    private Integer amount;
    private short durability;
    private String displayName;
    private List<String> lore;
    private boolean glow;
    private boolean unbreakable;
    private Map<Enchantment, Integer> enchantments;
    private Set<ItemFlag> flags;
    private Map<String, Object> nbtTags;
    private Integer customModelData;
    private Integer potionColor;

    public static ItemBuilder getHead(String base64) {
        final UUID uuid = UUID.nameUUIDFromBytes(base64.getBytes());
        if (HEADS.containsKey(uuid))
            return fromItemStack(HEADS.get(uuid));

        try {
            ItemStack itemStack = builder().type(WMaterial.PLAYER_HEAD).build();
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

            GameProfile gameProfile = new GameProfile(uuid, null);
            PropertyMap properties = gameProfile.getProperties();

            properties.put("textures", new Property("textures", base64));

            Field profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, gameProfile);

            itemStack.setItemMeta(itemMeta);

            HEADS.put(uuid, itemStack);

            return fromItemStack(itemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemBuilder getHead(UUID player) {
        return getHead(Bukkit.getOfflinePlayer(player));
    }

    public static ItemBuilder getHead(OfflinePlayer player) {
        if (HEADS.containsKey(player.getUniqueId()))
            return fromItemStack(HEADS.get(player.getUniqueId()));

        ItemStack item = builder().type(WMaterial.PLAYER_HEAD).build();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        item.setItemMeta(meta);

        HEADS.put(player.getUniqueId(), item);

        return fromItemStack(item);
    }

    /**
     * Attempts to create an ItemBuilder of the item.
     * If the item is null it will return an empty ItemBuilder
     * @param item the item
     * @return the ItemBuilder based on the item
     */
    public static ItemBuilder fromItemStack(ItemStack item) {
        final ItemBuilder builder = builder();
        if (item == null)
            return builder;

        ItemAdapter itemAdapter = ADAPTER.getItemAdapter(item);
        ItemMeta itemMeta = item.getItemMeta();

        builder
                .type(item.getType())
                .amount(item.getAmount() == 1 ? null : item.getAmount())
                .durability(item.getDurability())
                .enchantments(item.getEnchantments().isEmpty() ? null : item.getEnchantments())
                .nbtTags(itemAdapter.getNbtTags().isEmpty() ? null : itemAdapter.getNbtTags())
                .glow(itemAdapter.hasGlow());

        if (item.hasItemMeta())
            builder
                    .displayName(itemMeta.getDisplayName())
                    .lore(itemMeta.getLore())
                    .flags(itemMeta.getItemFlags().isEmpty() ? null : itemMeta.getItemFlags())
                    .customModelData(itemAdapter.getCustomModelData())
                    .unbreakable(itemAdapter.isUnbreakable());

        if (item.getType() == Material.POTION)
            builder.potionColor((Integer) itemAdapter.getMinecraftNbtTag("CustomPotionColor"));

        return builder;
    }

    public static ItemBuilder deserialize(Map<String, Object> args) {
        return builder().setData(args);
    }

    public static String toBase64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack fromBase64(String base64) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param item the item
     * @return the display item name in the client (in english)
     */
    public static String getRealName(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null)
            return item.getItemMeta().getDisplayName();

        switch (item.getType().name()) {
            case "BANNER":
                return getColorName((byte) (15 - item.getDurability())) + " Banner";
            case "INK_SACK":
                if (item.getDurability() == 0)
                    return "Ink Sac";
                return getColorName((byte) (15 - item.getDurability())) + " Dye";
            case "STAINED_GLASS":
                return getColorName((byte) item.getDurability()) + " Stained Glass";
            case "STAINED_GLASS_PANE":
                return getColorName((byte) item.getDurability()) + " Stained Glass Pane";
            case "CARPET":
                return getColorName((byte) item.getDurability()) + " Carpet";
            case "BED":
                return getColorName((byte) item.getDurability()) + " Bed";
            case "WOOL":
                return getColorName((byte) item.getDurability()) + " Wool";
            case "CONCRETE":
                return getColorName((byte) item.getDurability()) + " Concrete";
            case "CONCRETE_POWDER":
                return getColorName((byte) item.getDurability()) + " Concrete Powder";
            case "SKULL_ITEM": {
                switch (item.getDurability()) {
                    case 0:
                        return "Skeleton Skull";
                    case 1:
                        return "White Skeleton Skull";
                    case 2:
                        return "Zombie Head";
                    case 3:
                        return "Head";
                    case 4:
                        return "Creeper Head";
                    case 5:
                        return "Dragon Head";
                    default:
                        return "Unknown Skull";
                }
            }
            case "WOOD": {
                switch (item.getDurability()) {
                    case 0:
                        return "Oak Wood Planks";
                    case 1:
                        return "Spruce Wood Planks";
                    case 2:
                        return "Birch Wood Planks";
                    case 3:
                        return "Jungle Wood Planks";
                    case 4:
                        return "Acacia Wood Planks";
                    case 5:
                        return "Dark Oak Wood Planks";
                    default:
                        return "Unknown Wood Planks";
                }
            }
            case "LOG": {
                switch (item.getDurability()) {
                    case 0:
                        return "Oak Wood";
                    case 1:
                        return "Spruce Wood";
                    case 2:
                        return "Birch Wood";
                    case 3:
                        return "Jungle Wood";
                    default:
                        return "Unknown Wood";
                }
            }
            case "LOG_2": {
                switch (item.getDurability()) {
                    case 0:
                        return "Acacia Wood";
                    case 1:
                        return "Dark Wood";
                    default:
                        return "Unknown Wood";
                }
            }
            case "WOOD_STEP": {
                switch (item.getDurability()) {
                    case 0:
                        return "Oak Wood Slab";
                    case 1:
                        return "Spruce Wood Slab";
                    case 2:
                        return "Birch Wood Slab";
                    case 3:
                        return "Jungle Wood Slab";
                    case 4:
                        return "Acacia Wood Slab";
                    case 5:
                        return "Dark Wood Slab";
                    default:
                        return "Unknown Wood Slab";
                }
            }
            case "STEP": {
                switch (item.getDurability()) {
                    case 0:
                        return "Stone Slab";
                    case 1:
                        return "Sandstone Slab";
                    case 3:
                        return "Cobblestone Slab";
                    case 4:
                        return "Bricks Slab";
                    case 5:
                        return "Stone Bricks Slab";
                    case 6:
                        return "Nether Brick Slab";
                    case 7:
                        return "Quartz Slab";
                    default:
                        return "Unknown Slab";
                }
            }
            default:
                return StringUtil.getName(item.getType());
        }
    }

    private static String getColorName(byte color) {
        switch (color) {
            case 0:
                return "White";
            case 1:
                return "Orange";
            case 2:
                return "Magenta";
            case 3:
                return "Light Blue";
            case 4:
                return "Yellow";
            case 5:
                return "Lime";
            case 6:
                return "Pink";
            case 7:
                return "Gray";
            case 8:
                return "Light Gray";
            case 9:
                return "Cyan";
            case 10:
                return "Purple";
            case 11:
                return "Blue";
            case 12:
                return "Brown";
            case 13:
                return "Green";
            case 14:
                return "Red";
            case 15:
                return "Black";
            default:
                return "Unknown";
        }
    }

    @SerializableAs("item-builder")
    public static class ItemBuilder implements ConfigurationSerializable, Cloneable {

        private WMaterial wmaterial;

        public String getDisplayName() {
            return displayName;
        }

        public Material getType() {
            fixMaterial();
            return type;
        }

        public int getAmount() {
            return amount == null ? 1 : amount;
        }

        public short getDurability() {
            return durability;
        }

        public Map<Enchantment, Integer> getEnchantments() {
            return enchantments == null ? new HashMap<>() : enchantments;
        }

        public Set<ItemFlag> getFlags() {
            return flags;
        }

        public ItemBuilder type(Material type) {
            this.type = type;
            wmaterial = null;
            return this;
        }

        public ItemBuilder type(WMaterial type) {
            this.type = null;
            wmaterial = type;
            return this;
        }

        public ItemBuilder lore(String... lore) {
            return lore(Arrays.asList(lore));
        }

        public ItemBuilder lore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public ItemBuilder enchantment(Enchantment enchantment, int level) {
            if (enchantments == null)
                enchantments = new HashMap<>();
            enchantments.put(enchantment, level);
            return this;
        }

        public ItemBuilder flags(ItemFlag... flags) {
            if (this.flags == null)
                this.flags = new HashSet<>();
            this.flags.addAll(Arrays.asList(flags));
            return this;
        }

        public ItemBuilder flags(Set<ItemFlag> flags) {
            this.flags = flags;
            return this;
        }

        public ItemBuilder nbtTag(String key, Object value) {
            if (nbtTags == null)
                nbtTags = new HashMap<>();
            nbtTags.put(key, value);
            return this;
        }

        public boolean hasNbtTag(String key) {
            if (nbtTags == null)
                return false;
            return nbtTags.containsKey(key) || nbtTags.containsKey(key.toLowerCase()) || nbtTags.containsKey("wlib:" + key.toLowerCase());
        }

        public boolean hasFlag(ItemFlag flag) {
            if (flags == null)
                flags = new HashSet<>();
            return flags.contains(flag);
        }

        public Object getNbtTag(String key) {
            if (nbtTags == null)
                return null;
            return ADAPTER.nbtToJava(nbtTags.getOrDefault(key, nbtTags.get(key.toLowerCase())));
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> getNbtTags() {
            if (nbtTags == null)
                return new HashMap<>();
            return (Map<String, Object>) ADAPTER.nbtToJava(nbtTags);
        }

        public ItemBuilder copy(ItemStack item) {
            ItemBuilder builder = Item.fromItemStack(item);
            return type(builder.type)
                    .amount(builder.amount)
                    .durability(builder.durability)
                    .displayName(builder.displayName)
                    .lore(builder.lore)
                    .enchantments(builder.enchantments)
                    .nbtTags(builder.nbtTags)
                    .flags(builder.flags)
                    .glow(builder.glow)
                    .unbreakable(builder.unbreakable)
                    .customModelData(builder.customModelData)
                    .potionColor(builder.potionColor);
        }

        public ItemStack build() {
            ItemStack itemStack;
            if (wmaterial != null) //ill add all the items in WMaterial :D
                itemStack = fixMaterial();
            else
                itemStack = new ItemStack(type, amount == null ? 1 : amount, durability);

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null)
                return itemStack;
            itemMeta.setDisplayName(displayName == null ? null : displayName.replace('&', '§'));
            itemMeta.setLore(lore);

            itemStack.setItemMeta(itemMeta);
            ItemAdapter itemAdapter = ADAPTER.getItemAdapter(itemStack);
            itemAdapter.setUnbreakable(unbreakable);
            itemStack = itemAdapter.getTarget();
            itemMeta = itemStack.getItemMeta();

            itemMeta.addItemFlags(flags == null ? new ItemFlag[0] : flags.toArray(new ItemFlag[]{}));
            itemStack.setItemMeta(itemMeta);
            itemStack.addUnsafeEnchantments(enchantments == null ? new HashMap<>() : enchantments);

            itemAdapter = ADAPTER.getItemAdapter(itemStack);

            if (enchantments == null || enchantments.isEmpty()) {
                itemAdapter.setGlow(glow);
                itemStack = itemAdapter.getTarget();
                itemAdapter = ADAPTER.getItemAdapter(itemStack);
            }
            if (nbtTags != null) {
                itemAdapter.setNbtTags(nbtTags, false);
                itemStack = itemAdapter.getTarget();
                itemAdapter = ADAPTER.getItemAdapter(itemStack);
            }
            if (customModelData != null) {
                itemAdapter.setCustomModelData(customModelData);
                itemStack = itemAdapter.getTarget();
            }
            if (potionColor != null && itemStack.getType() == Material.POTION) {
                itemAdapter.setMinecraftNbtTag("CustomPotionColor", potionColor);
                itemStack = itemAdapter.getTarget();
                itemAdapter = ADAPTER.getItemAdapter(itemStack);
            }

            return itemStack;
        }

        public List<String> getLore() {
            return lore == null ? new ArrayList<>() : new ArrayList<>(lore);
        }

        public boolean hasGlow() {
            return glow;
        }

        public boolean isUnbreakable() {
            return unbreakable;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new LinkedHashMap<>();

            fixMaterial();
            map.put("material", type.name());
            if (amount != null)
                map.put("amount", amount);
            if (durability != 0)
                map.put("durability", durability);
            if (displayName != null)
                map.put("display-name", displayName);
            if (lore != null)
                map.put("lore", lore);
            if (flags != null)
                map.put("flags", flags.stream().map(ItemFlag::name).collect(Collectors.toList()));
            if (nbtTags != null)
                map.put("nbt-tags", nbtTags);
            if (unbreakable)
                map.put("unbreakable", true);
            if (glow)
                map.put("glow", true);
            if (enchantments != null)
                map.put("enchantments", MapUtils.mapKeys(enchantments, Enchantment::getName));
            if (customModelData != null)
                map.put("custom-model-data", customModelData);
            if (potionColor != null)
                map.put("custom-potion-color", potionColor);

            return map;
        }

        @SuppressWarnings("unchecked")
        public ItemBuilder setData(Map<String, Object> args) {
            List<String> lore = new ArrayList<>();
            if (args.get("lore") != null)
                lore = new CollectionUtil<>((List<String>) args.get("lore")).replace('&', '§').getCollection();

            Set<ItemFlag> flags = new HashSet<>();
            if (args.get("flags") != null) {
                final List<String> flagList = (List<String>) args.get("flags");
                for (String s : flagList)
                    flags.add(ItemFlag.valueOf(s.toUpperCase()));
            }

            Map<Enchantment, Integer> enchantments = new HashMap<>();
            if (args.get("enchantments") != null) {
                Map<String, Number> enchantmentsMap = (Map<String, Number>) args.get("enchantments");
                for (Map.Entry<String, Number> entry : enchantmentsMap.entrySet())
                    enchantments.put(Enchantment.getByName(entry.getKey()), entry.getValue().intValue());
            }

            if (args.containsKey("material") || args.containsKey("type"))
                type(Material.valueOf(args.getOrDefault("material", args.get("type")).toString().toUpperCase()));
            if (args.containsKey("amount"))
                amount((int) args.get("amount"));
            if (args.containsKey("durability"))
                durability(((Number) args.get("durability")).shortValue());
            if (args.containsKey("display-name"))
                displayName(args.get("display-name").toString().replace('&', '§'));
            if (args.containsKey("lore"))
                lore(lore);
            if (args.containsKey("flags"))
                flags(flags);
            if (args.containsKey("unbreakable"))
                unbreakable((boolean) args.get("unbreakable"));
            if (args.containsKey("glow"))
                glow((boolean) args.get("glow"));
            if (args.containsKey("enchantments"))
                enchantments(enchantments);
            if (args.containsKey("nbt-tags"))
                nbtTags((Map<String, Object>) args.get("nbt-tags"));
            if (args.containsKey("custom-model-data"))
                customModelData((int) args.get("custom-model-data"));
            if (args.containsKey("custom-potion-color"))
                potionColor((Integer) args.get("custom-potion-color"));

            return this;
        }

        public static ItemBuilder deserialize(Map<String, Object> args) {
            return new ItemBuilder().setData(args);
        }

        private ItemStack fixMaterial() {
            if (wmaterial != null) {
                if (wmaterial.dataDependent())
                    return new ItemStack(Material.valueOf(wmaterial.name()), 1, durability);
                final ItemStack stack = wmaterial.getItemStack();
                type = stack.getType();
                durability = stack.getDurability();
                return stack;
            }
            return null;
        }

        @Override
        public ItemBuilder clone() {
            try {
                ItemBuilder builder = ((ItemBuilder) super.clone());
                if (nbtTags != null)
                    builder.nbtTags = new HashMap<>(nbtTags);
                if (enchantments != null)
                    builder.enchantments = new HashMap<>(enchantments);
                if (lore != null)
                    builder.lore = new ArrayList<>(lore);
                if (flags != null)
                    builder.flags = new HashSet<>(flags);
                return builder;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return this;
            }
        }
    }
}
