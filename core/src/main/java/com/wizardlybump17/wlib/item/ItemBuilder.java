package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.item.enchantment.GlowEnchantment;
import com.wizardlybump17.wlib.util.CollectionUtil;
import com.wizardlybump17.wlib.util.MapUtils;
import com.wizardlybump17.wlib.util.bukkit.StringUtil;
import lombok.NonNull;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@SerializableAs("item-builder")
public class ItemBuilder implements ConfigurationSerializable, Cloneable {

    private static final ItemFlag[] EMPTY_ITEM_FLAG_ARRAY = new ItemFlag[0];

    private final ItemStack item;
    private final Map<Object, Object> customData;

    public ItemBuilder(ItemStack item, Map<Object, Object> customData) {
        this.item = item == null ? new ItemStack(Material.AIR) : item;
        this.customData = customData;
    }

    public ItemBuilder() {
        this(new ItemStack(Material.AIR), new HashMap<>());
    }

    public ItemBuilder consumeMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return this;

        consumer.accept(meta);
        item.setItemMeta(meta);

        return this;
    }
    
    public ItemMeta getItemMeta() {
        return item.getItemMeta();
    }

    private <T> T getFromMeta(Function<ItemMeta, T> supplier, T def) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return def;

        T t = supplier.apply(meta);
        return t == null ? def : t;
    }

    public ItemBuilder type(@NonNull Material material) {
        item.setType(material);
        return this;
    }

    public Material type() {
        return item.getType();
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public int amount() {
        return item.getAmount();
    }

    public ItemBuilder damage(int durability) {
        return consumeMeta(meta -> {
            if (meta instanceof Damageable damageable)
                damageable.setDamage(durability);
        });
    }

    public int damage() {
        return getFromMeta(meta -> {
            if (meta instanceof Damageable damageable)
                return damageable.getDamage();
            return null;
        }, 0);
    }

    public ItemBuilder lore(@Nullable String... lore) {
        return consumeMeta(meta -> meta.setLore(lore == null ? null : StringUtil.colorize(Arrays.asList(lore))));
    }

    public ItemBuilder lore(@Nullable List<String> lore) {
        return consumeMeta(meta -> meta.setLore(StringUtil.colorize(lore)));
    }

    public ItemBuilder itemFlags(@NotNull ItemFlag... itemFlags) {
        return consumeMeta(meta -> {
            meta.removeItemFlags(meta.getItemFlags().toArray(EMPTY_ITEM_FLAG_ARRAY));
            meta.addItemFlags(itemFlags);
        });
    }

    public ItemBuilder itemFlags(@NotNull Set<ItemFlag> itemFlags) {
        return consumeMeta(meta -> {
            meta.removeItemFlags(meta.getItemFlags().toArray(EMPTY_ITEM_FLAG_ARRAY));
            meta.addItemFlags(itemFlags.toArray(EMPTY_ITEM_FLAG_ARRAY));
        });
    }

    public <Z> ItemBuilder nbtTag(@NonNull String key, @NonNull PersistentDataType<?, Z> type, @NonNull Z value) {
        return nbtTag(NamespacedKey.fromString(key), type, value);
    }

    public <Z> ItemBuilder nbtTag(@NonNull NamespacedKey key, @NonNull PersistentDataType<?, Z> type, @NonNull Z value) {
        return consumeMeta(meta -> meta.getPersistentDataContainer().set(key, type, value));
    }

    public ItemBuilder removeNbtTag(@NonNull String key) {
        return removeNbtTag(NamespacedKey.fromString(key));
    }

    public ItemBuilder removeNbtTag(@NonNull NamespacedKey key) {
        return consumeMeta(meta -> meta.getPersistentDataContainer().remove(key));
    }

    public ItemBuilder nbtTags(@NonNull PersistentDataContainer container) {
        return consumeMeta(meta -> {
            PersistentDataContainer targetContainer = meta.getPersistentDataContainer();
            ItemAdapter.getInstance().transferPersistentData(container, targetContainer);
        });
    }

    public Map<String, Object> nbtTags() {
        return ItemAdapter.getInstance().serializeContainer(container());
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        return consumeMeta(meta -> meta.addEnchant(enchantment, level, true));
    }

    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        return consumeMeta(meta -> enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true)));
    }

    public Map<Enchantment, Integer> enchantments() {
        return getFromMeta(ItemMeta::getEnchants, new HashMap<>());
    }

    public ItemBuilder glow(boolean glow) {
        return consumeMeta(meta -> {
            if (glow)
                meta.addEnchant(GlowEnchantment.INSTANCE, 0, true);
            else
                meta.removeEnchant(GlowEnchantment.INSTANCE);
        });
    }

    public boolean glow() {
        return getFromMeta(meta -> meta.hasEnchant(GlowEnchantment.INSTANCE), false);
    }

    @NotNull
    public String displayName() {
        return getFromMeta(ItemMeta::getDisplayName, "");
    }

    public ItemBuilder displayName(@Nullable String displayName) {
        return consumeMeta(meta -> meta.setDisplayName(StringUtil.colorize(displayName)));
    }

    @NotNull
    public List<String> lore() {
        return getFromMeta(ItemMeta::getLore, new ArrayList<>());
    }

    @NotNull
    public Set<ItemFlag> itemFlags() {
        return getFromMeta(ItemMeta::getItemFlags, new HashSet<>());
    }

    public PersistentDataContainer container() {
        return getFromMeta(PersistentDataHolder::getPersistentDataContainer, ItemAdapter.PERSISTENT_DATA_ADAPTER_CONTEXT.newPersistentDataContainer());
    }

    public ItemBuilder replaceDisplayNameLore(Map<String, Object> replacements) {
        String displayName = displayName();
        List<String> lore = lore();

        for (Map.Entry<String, Object> entry : replacements.entrySet()) {
            Object value = entry.getValue();
            if (value == null)
                value = "null";

            displayName = displayName.replace(entry.getKey(), value.toString());
            if (value instanceof String string)
                lore = new CollectionUtil<>(lore).replace(entry.getKey(), string).getCollection();
            else if (value instanceof Iterable<?> iterable)
                lore = new CollectionUtil<>(lore).replace(entry.getKey(), iterable).getCollection();
            else
                lore = new CollectionUtil<>(lore).replace(entry.getKey(), value.toString()).getCollection();
        }

        return displayName(displayName).lore(lore);
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        return consumeMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    public boolean unbreakable() {
        return getFromMeta(ItemMeta::isUnbreakable, false);
    }

    public ItemBuilder customModelData(Integer customModelData) {
        return consumeMeta(meta -> meta.setCustomModelData(customModelData));
    }

    public Integer customModelData() {
        return getFromMeta(meta -> {
            if (meta.hasCustomModelData())
                return meta.getCustomModelData();
            return null;
        }, null);
    }

    public String skullUrl() {
        if (getItemMeta() instanceof SkullMeta meta)
            return ItemAdapter.getInstance().getSkullUrl(meta);
        return null;
    }

    public ItemBuilder skull(String url) {
        return consumeMeta(meta -> {
            if (meta instanceof SkullMeta skull)
                ItemAdapter.getInstance().setSkull(skull, url);
        });
    }

    public OfflinePlayer skullOwner() {
        if (getItemMeta() instanceof SkullMeta meta)
            return meta.getOwningPlayer();
        return null;
    }

    public ItemBuilder skull(OfflinePlayer owner) {
        return consumeMeta(meta -> {
            if (meta instanceof SkullMeta skull)
                skull.setOwningPlayer(owner);
        });
    }

    public ItemBuilder customData(Object key, Object value) {
        this.customData.put(key, value);
        return this;
    }

    public ItemBuilder customData(Map<Object, Object> customData) {
        this.customData.clear();
        this.customData.putAll(customData);
        return this;
    }

    public Map<Object, Object> customData() {
        return this.customData;
    }

    public ItemBuilder color(Color color) {
        return consumeMeta(meta -> {
            if (meta instanceof LeatherArmorMeta leatherMeta)
                leatherMeta.setColor(color);
        });
    }

    public Color color() {
        return getFromMeta(meta -> {
            if (meta instanceof LeatherArmorMeta leatherMeta)
                return leatherMeta.getColor();
            return null;
        }, null);
    }

    public ItemStack build() {
        return item;
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("type", type().name());
        if (amount() != 1)
            result.put("amount", amount());
        if (damage() != 0)
            result.put("damage", damage());
        if (!displayName().isEmpty())
            result.put("display-name", displayName());
        if (!lore().isEmpty())
            result.put("lore", lore());
        if (!itemFlags().isEmpty())
            result.put("item-flags", itemFlags().stream().map(Enum::name).toList());
        if (!enchantments().isEmpty())
            result.put("enchantments", MapUtils.mapKeys(enchantments(), enchantment -> enchantment.getKey().toString()));
        if (!container().isEmpty())
            result.put("nbt-tags", ItemAdapter.getInstance().serializeContainer(container()));
        if (unbreakable())
            result.put("unbreakable", true);
        if (customModelData() != null)
            result.put("custom-model-data", customModelData());
        if (skullUrl() != null)
            result.put("skull", skullUrl());
        if (skullOwner() != null)
            result.put("skull", skullOwner().getUniqueId().toString());
        if (color() != null)
            result.put("color", color());
        if (!customData().isEmpty())
            result.put("custom-data", customData());

        return MapUtils.removeEmptyValues(MapUtils.removeNullValues(result));
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(item.clone(), new HashMap<>(customData));
    }

    /**
     * Copies the data from the given ItemStack into a new builder.
     * If the item is null, air or not have an ItemMeta, the builder will be empty
     *
     * @param item the item to copy from
     * @return a new builder with the data from the given item
     */
    public static ItemBuilder fromItemStack(@Nullable ItemStack item) {
        return new ItemBuilder(item, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public static ItemBuilder deserialize(Map<String, Object> map) {
        ItemBuilder result = new ItemBuilder();

        result
                .type(Material.valueOf((String) map.get("type")))
                .amount((int) map.getOrDefault("amount", 1));

        if (map.get("damage") != null)
            result.damage((int) map.get("damage"));
        if (map.get("display-name") != null)
            result.displayName((String) map.get("display-name"));
        if (map.get("lore") != null)
            result.lore((List<String>) map.get("lore"));
        if (map.get("item-flags") != null)
            result.itemFlags(((List<String>) map.get("item-flags")).stream().map(ItemFlag::valueOf).collect(Collectors.toSet()));
        if (map.get("enchantments") != null)
            ((Map<String, Integer>) map.get("enchantments")).forEach((key, value) -> result.enchantment(Enchantment.getByKey(NamespacedKey.fromString(key.toLowerCase())), value));
        if (map.get("nbt-tags") != null)
            result.nbtTags(ItemAdapter.getInstance().deserializeContainer((Map<String, Object>) map.get("nbt-tags")));

        if (map.get("skull") != null) {
            String skull = map.get("skull").toString();
            try {
                result.skull(Bukkit.getOfflinePlayer(UUID.fromString(skull)));
            } catch (IllegalArgumentException ignored) {
                result.skull(skull);
            }
        }

        result
                .unbreakable((boolean) map.getOrDefault("unbreakable", false))
                .customModelData((Integer) map.get("custom-model-data"))
                .color(getColor(map.get("color")))
                .customData((Map<Object, Object>) map.getOrDefault("custom-data", Collections.emptyMap()))
                .glow((boolean) map.getOrDefault("glow", false));

        return result;
    }

    private static Color getColor(Object object) {
        if (object instanceof Color color)
            return color;

        if (object instanceof String string) {
            try {
                return Color.fromRGB(Integer.parseInt(string.substring(1), 16));
            } catch (NumberFormatException ignored) {
            }
        }

        return null;
    }
}
