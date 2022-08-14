package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.adapter.util.StringUtil;
import com.wizardlybump17.wlib.item.enchantment.GlowEnchantment;
import com.wizardlybump17.wlib.util.MapUtils;
import lombok.Getter;
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
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Accessors(chain = true, fluent = true)
@Getter
@Setter
@SerializableAs("item-builder")
public class ItemBuilder implements ConfigurationSerializable, Cloneable {

    private static final NMSAdapter ADAPTER = NMSAdapterRegister.getInstance().current();
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
    private Map<String, Object> nbtTags = new LinkedHashMap<>();
    @NotNull
    private Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
    private boolean applyColor = true;
    private boolean unbreakable;
    private Integer customModelData;

    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder type(@NotNull Material type) {
        this.type = type;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder lore(@NotNull String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder lore(@NotNull List<String> lore) {
        this.lore = new ArrayList<>(lore);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder itemFlags(@NotNull ItemFlag... itemFlags) {
        this.itemFlags = new HashSet<>(Arrays.asList(itemFlags));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder itemFlags(@NotNull Set<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder applyColor(boolean applyColor) {
        this.applyColor = applyColor;
        return this;
    }

    /**
     * Sets or removes the nbt tag.
     * The value will be converted to a common Java object
     *
     * @param key   the key of the nbt tag
     * @param value the value of the nbt tag
     * @return this
     */
    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder nbtTag(@NotNull String key, @NotNull Object value) {
        nbtTags.put(key, ADAPTER.nbtToJava(value));
        return this;
    }

    /**
     * Removes the nbt tag with the given key
     *
     * @param key the key of the nbt tag
     * @return this
     */
    public ItemBuilder removeNbtTag(@NotNull String key) {
        nbtTags.remove(key);
        return this;
    }

    /**
     * Sets all nbt tags to this item.
     * If the map is not null or empty, the values will be converted to a common Java object
     *
     * @param nbtTags the nbt tags
     * @return this
     */
    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder nbtTags(@NotNull Map<String, Object> nbtTags) {
        for (Map.Entry<String, Object> entry : nbtTags.entrySet())
            entry.setValue(ADAPTER.nbtToJava(entry.getValue()));
        this.nbtTags = nbtTags;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
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

    public ItemStack build() {
        ItemStack result = new ItemStack(type, amount == null ? 1 : amount);
        ItemMeta meta = result.getItemMeta();
        if (meta == null)
            return result;

        meta.setUnbreakable(unbreakable);
        meta.setCustomModelData(customModelData);

        ItemAdapter adapter = ADAPTER.getItemAdapter(result);

        adapter.setNbtTags(nbtTags);
        result = adapter.getTarget();

        meta.setDisplayName(applyColor ? ADAPTER.getStringUtil().colorize(displayName) : displayName);
        meta.setLore(applyColor ? ADAPTER.getStringUtil().colorize(lore) : lore);
        meta.addItemFlags(itemFlags.toArray(EMPTY_ITEM_FLAG_ARRAY));
        result.addUnsafeEnchantments(enchantments);

        result.setItemMeta(meta);

        return result;
    }

    @Override
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
        result.put("nbt-tags", nbtTags);
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
            builder.nbtTags = new LinkedHashMap<>(nbtTags);
            builder.enchantments = new HashMap<>(enchantments);

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
    public static ItemBuilder fromItemStack(ItemStack item) {
        return fromItemStack(item, true);
    }

    /**
     * Copies the data from the given ItemStack into a new builder.
     * If the item is null, air or not have an ItemMeta, the builder will be empty
     *
     * @param item              the item to copy from
     * @param ignoreDefaultTags if the default nbt tags should be ignored
     * @return a new builder with the data from the given item
     */
    public static ItemBuilder fromItemStack(ItemStack item, boolean ignoreDefaultTags) {
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

        result.nbtTags = ADAPTER.getItemAdapter(item).getNbtTags(ignoreDefaultTags);

        return result;
    }

    @SuppressWarnings("unchecked")
    public static ItemBuilder deserialize(Map<String, Object> map) {
        StringUtil stringUtil = NMSAdapterRegister.getInstance().current().getStringUtil();

        ItemBuilder result = new ItemBuilder();

        result.type = Material.valueOf((String) map.get("type"));
        result.amount = (Integer) map.get("amount");
        result.durability = (int) map.getOrDefault("durability", 0);
        if (map.get("display-name") != null)
            result.displayName(stringUtil.colorize(map.get("display-name").toString()));
        if (map.get("lore") != null)
            result.lore(stringUtil.colorize((List<String>) map.get("lore")));
        if (map.get("item-flags") != null)
            result.itemFlags(((List<String>) map.get("item-flags")).stream().map(ItemFlag::valueOf).collect(Collectors.toSet()));
        if (map.get("enchantments") != null)
            ((Map<String, Integer>) map.get("enchantments")).forEach((key, value) -> result.enchantment(Enchantment.getByKey(NamespacedKey.fromString(key)), value));
        if (map.get("nbt-tags") != null)
            result.nbtTags((Map<String, Object>) map.get("nbt-tags"));
        result.unbreakable((boolean) map.getOrDefault("unbreakable", false));
        result.customModelData((Integer) map.get("custom-model-data"));

        return result;
    }
}
