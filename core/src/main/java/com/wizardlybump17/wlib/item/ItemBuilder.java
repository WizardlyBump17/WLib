package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.item.enchantment.GlowEnchantment;
import com.wizardlybump17.wlib.util.CollectionUtil;
import com.wizardlybump17.wlib.util.MapUtils;
import com.wizardlybump17.wlib.util.bukkit.StringUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
@Getter
@Setter
@SerializableAs("item-builder")
public class ItemBuilder implements ConfigurationSerializable, Cloneable {

    private static final ItemFlag[] EMPTY_ITEM_FLAG_ARRAY = new ItemFlag[0];

    @NotNull
    private Material type;
    private Integer amount;
    private int durability;
    @NotNull
    private String displayName = "";
    @NotNull
    private List<String> lore = new ArrayList<>();
    @NotNull
    private Set<ItemFlag> itemFlags = new HashSet<>();
    @NotNull
    private final PersistentDataContainer container = ItemAdapter.PERSISTENT_DATA_ADAPTER_CONTEXT.newPersistentDataContainer();
    @NotNull
    private Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
    private boolean unbreakable;
    private Integer customModelData;

    public ItemBuilder lore(@NotNull String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder lore(@NotNull List<String> lore) {
        this.lore = new ArrayList<>(lore);
        return this;
    }

    public ItemBuilder itemFlags(@NotNull ItemFlag... itemFlags) {
        this.itemFlags = new HashSet<>(Arrays.asList(itemFlags));
        return this;
    }

    public ItemBuilder itemFlags(@NotNull Set<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public <Z> ItemBuilder nbtTag(@NonNull String key, @NonNull PersistentDataType<?, Z> type, @NonNull Z value) {
        return nbtTag(NamespacedKey.fromString(key), type, value);
    }

    public <Z> ItemBuilder nbtTag(@NonNull NamespacedKey key, @NonNull PersistentDataType<?, Z> type, @NonNull Z value) {
        container.set(key, type, value);
        return this;
    }

    public ItemBuilder removeNbtTag(@NonNull String key) {
        return removeNbtTag(NamespacedKey.fromString(key));
    }

    public ItemBuilder removeNbtTag(@NonNull NamespacedKey key) {
        container.remove(key);
        return this;
    }

    public ItemBuilder nbtTags(@NonNull PersistentDataContainer container) {
        ItemAdapter.getInstance().transferPersistentData(container, this.container);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (glow)
            enchantments.put(GlowEnchantment.INSTANCE, 0);
        else
            enchantments.remove(GlowEnchantment.INSTANCE);
        return this;
    }

    public boolean glow() {
        return enchantments.containsKey(GlowEnchantment.INSTANCE);
    }

    @NotNull
    public String displayName() {
        return displayName;
    }

    @NotNull
    public List<String> lore() {
        return lore;
    }

    @NotNull
    public Set<ItemFlag> itemFlags() {
        return itemFlags;
    }

    public ItemBuilder replaceDisplayNameLore(Map<String, Object> replacements) {
        for (Map.Entry<String, Object> entry : replacements.entrySet()) {
            displayName = displayName.replace(entry.getKey(), entry.getValue().toString());
            lore = new CollectionUtil<>(lore).replace(entry.getKey(), entry.getValue().toString()).getCollection();
        }
        return this;
    }

    public ItemStack build() {
        ItemStack result = new ItemStack(type, amount == null ? 1 : amount);
        ItemMeta meta = result.getItemMeta();
        if (meta == null)
            return result;

        meta.setDisplayName(StringUtil.colorize(displayName));
        meta.setLore(StringUtil.colorize(lore));
        meta.addItemFlags(itemFlags.toArray(EMPTY_ITEM_FLAG_ARRAY));
        meta.setUnbreakable(unbreakable);
        meta.setCustomModelData(customModelData);
        result.addUnsafeEnchantments(enchantments);
        if (meta instanceof Damageable damageable)
            damageable.setDamage(durability);
        ItemAdapter.getInstance().transferPersistentData(container, meta.getPersistentDataContainer());

        result.setItemMeta(meta);

        return result;
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("type", type.name()); //implicit null check
        result.put("amount", amount);
        if (durability != 0)
            result.put("durability", durability);
        result.put("display-name", displayName);
        result.put("lore", lore);
        result.put("item-flags", itemFlags.stream().map(Enum::name).toList());
        result.put("enchantments", MapUtils.mapKeys(enchantments, enchantment -> enchantment.getKey().toString()));
        result.put("nbt-tags", ItemAdapter.getInstance().serializeContainer(container));
        result.put("unbreakable", unbreakable);
        result.put("custom-model-data", customModelData);

        return MapUtils.removeEmptyValues(MapUtils.removeNullValues(result));
    }

    @Override
    public ItemBuilder clone() {
        try {
            ItemBuilder builder = (ItemBuilder) super.clone();

            builder.lore = new ArrayList<>(lore);
            builder.itemFlags = new HashSet<>(itemFlags);
            builder.enchantments = new HashMap<>(enchantments);
            ItemAdapter.getInstance().transferPersistentData(ItemAdapter.getInstance().deserializeContainer(ItemAdapter.getInstance().serializeContainer(container)), container);

            return builder;
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

    /**
     * Copies the data from the given ItemStack into a new builder.
     * If the item is null, air or not have an ItemMeta, the builder will be empty
     *
     * @param item the item to copy from
     * @return a new builder with the data from the given item
     */
    public static ItemBuilder fromItemStack(@Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return new ItemBuilder();

        ItemBuilder result = new ItemBuilder();

        result.type = item.getType();
        result.amount = item.getAmount() == 1 ? null : item.getAmount();
        result.enchantments = item.getEnchantments();

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return result;

        result.displayName = meta.getDisplayName();
        result.lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        result.itemFlags = meta.getItemFlags();
        if (meta instanceof Damageable damageable)
            result.durability = damageable.getDamage();
        ItemAdapter.getInstance().transferPersistentData(meta.getPersistentDataContainer(), result.container);

        return result;
    }

    @SuppressWarnings("unchecked")
    public static ItemBuilder deserialize(Map<String, Object> map) {
        ItemBuilder result = new ItemBuilder();

        result.type = Material.valueOf((String) map.get("type"));
        result.amount = (Integer) map.get("amount");
        result.durability = (int) map.getOrDefault("durability", 0);
        if (map.get("display-name") != null)
            result.displayName(StringUtil.colorize(map.get("display-name").toString()));
        if (map.get("lore") != null)
            result.lore(StringUtil.colorize((List<String>) map.get("lore")));
        if (map.get("item-flags") != null)
            result.itemFlags(((List<String>) map.get("item-flags")).stream().map(ItemFlag::valueOf).collect(Collectors.toSet()));
        if (map.get("enchantments") != null)
            ((Map<String, Integer>) map.get("enchantments")).forEach((key, value) -> result.enchantment(Enchantment.getByKey(NamespacedKey.fromString(key)), value));
        if (map.get("nbt-tags") != null)
            result.nbtTags(ItemAdapter.getInstance().deserializeContainer((Map<Object, Object>) map.get("nbt-tags")));
        result.unbreakable((boolean) map.getOrDefault("unbreakable", false));
        result.customModelData((Integer) map.get("custom-model-data"));

        return result;
    }
}
