package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.adapter.util.StringUtil;
import com.wizardlybump17.wlib.util.MapUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Sets the CustomModelData to the nbt tags
     *
     * @param customModelData the CustomModelData
     * @return this
     */
    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder customModelData(Integer customModelData) {
        return nbtTag("CustomModelData", customModelData);
    }

    /**
     * Sets the Unbreakable to the nbt tags
     *
     * @param unbreakable if the item is unbreakable
     * @return this
     */
    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder unbreakable(Boolean unbreakable) {
        return nbtTag("Unbreakable", unbreakable);
    }

    /**
     * Sets the {@link NMSAdapter#GLOW_TAG} to the nbt tags
     *
     * @param glow if the item is glowing
     * @return this
     */
    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder glow(Boolean glow) {
        return nbtTag(NMSAdapter.GLOW_TAG, glow);
    }

    @SuppressWarnings("UnusedReturnValue")
    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    @Nullable
    public Boolean unbreakable() {
        return (Boolean) nbtTags.get("Unbreakable");
    }

    @Nullable
    public Boolean glow() {
        return (Boolean) nbtTags.get(NMSAdapter.GLOW_TAG);
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
        int amount = this.amount == null ? 1 : this.amount;
        ItemStack result = new ItemStack(type, amount);

        ItemAdapter adapter = ADAPTER.getItemAdapter(result);
        Boolean unbreakable = unbreakable();
        if (unbreakable != null)
            adapter.setUnbreakable(unbreakable);

        adapter.setNbtTags(nbtTags);
        result = adapter.getTarget();

        ItemMeta itemMeta = result.getItemMeta();

        itemMeta.setDisplayName(applyColor ? ADAPTER.getStringUtil().colorize(displayName) : displayName);
        itemMeta.setLore(applyColor ? ADAPTER.getStringUtil().colorize(lore) : lore);
        itemMeta.addItemFlags(itemFlags.toArray(EMPTY_ITEM_FLAG_ARRAY));
        result.addUnsafeEnchantments(enchantments);

        result.setItemMeta(itemMeta);

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
        result.put("item-flags", itemFlags.stream().map(Enum::name).collect(Collectors.toList()));
        result.put("enchantments", MapUtils.mapKeys(enchantments, Enchantment::getName));
        result.put("nbt-tags", nbtTags);

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
     * @param item the item to copy from
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

        result.type = Material.valueOf(map.get("type").toString().toUpperCase());

        if (map.containsKey("amount"))
            result.amount((Integer) map.get("amount"));
        if (map.containsKey("durability"))
            result.durability(Integer.parseInt(map.get("durability").toString()));
        if (map.containsKey("display-name"))
            result.displayName(stringUtil.colorize(map.get("display-name").toString()));
        if (map.containsKey("lore"))
            result.lore(stringUtil.colorize((List<String>) map.get("lore")));
        if (map.containsKey("item-flags"))
            result.itemFlags(((List<String>) map.get("item-flags")).stream().map(ItemFlag::valueOf).collect(Collectors.toSet()));

        if (map.containsKey("enchantments")) {
            Map<String, Integer> enchantments = (Map<String, Integer>) map.get("enchantments");
            enchantments.forEach((key, value) -> result.enchantment(Enchantment.getByName(key), value));
        }

        if (map.containsKey("nbt-tags"))
            result.nbtTags((Map<String, Object>) map.get("nbt-tags"));

        return result;
    }
}
