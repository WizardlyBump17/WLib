package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.adapter.WMaterial;
import com.wizardlybump17.wlib.util.MapUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
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

    @NotNull
    private Material type;
    private Integer amount;
    private short durability;
    private String displayName;
    @Nullable
    private List<String> lore;
    @Nullable
    private Set<ItemFlag> itemFlags;
    @Nullable
    private Map<String, Object> nbtTags;
    @Nullable
    private Map<Enchantment, Integer> enchantments;

    public ItemBuilder type(Material type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the type of this builder to the given {@link WMaterial}.
     * The durability and nbt tags may be set to this item
     * @param type the type
     * @return this
     * @throws IllegalArgumentException if the provided type is not supported in the current server version
     */
    public ItemBuilder type(WMaterial type) {
        ItemStack item = type.getItemStack();

        if (item == null)
            throw new IllegalArgumentException(type.name() + " is not supported in your current version (" + ADAPTER.getTargetVersion() + ")");

        this.type = item.getType();
        this.durability = item.getDurability();
        this.nbtTags = ADAPTER.getItemAdapter(item).getNbtTags();
        return this;
    }

    public ItemBuilder lore(String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder itemFlags(ItemFlag... itemFlags) {
        this.itemFlags = new HashSet<>(Arrays.asList(itemFlags));
        return this;
    }

    public ItemBuilder itemFlags(Set<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    /**
     * Sets or removes the nbt tag.
     * The value will be converted to a common Java object
     * @param key the key of the nbt tag
     * @param value the value of the nbt tag
     * @return this
     */
    public ItemBuilder nbtTag(String key, Object value) {
        if (nbtTags == null)
            nbtTags = new LinkedHashMap<>();

        if (value == null)
            nbtTags.remove(key);
        else
            nbtTags.put(key, ADAPTER.nbtToJava(value));

        return this;
    }

    /**
     * Sets all nbt tags to this item.
     * If the map is not null or empty, the values will be converted to a common Java object
     * @param nbtTags the nbt tags
     * @return this
     */
    public ItemBuilder nbtTags(Map<String, Object> nbtTags) {
        if (nbtTags != null)
            for (Map.Entry<String, Object> entry : nbtTags.entrySet())
                entry.setValue(ADAPTER.nbtToJava(entry.getValue()));
        this.nbtTags = nbtTags;
        return this;
    }

    /**
     * Sets the CustomModelData to the nbt tags
     * @param customModelData the CustomModelData
     * @return this
     */
    public ItemBuilder customModelData(Integer customModelData) {
        return nbtTag("CustomModelData", customModelData);
    }

    /**
     * Sets the Unbreakable to the nbt tags
     * @param unbreakable if the item is unbreakable
     * @return this
     */
    public ItemBuilder unbreakable(Boolean unbreakable) {
        return nbtTag("Unbreakable", unbreakable);
    }

    /**
     * Sets the {@link NMSAdapter#GLOW_TAG} to the nbt tags
     * @param glow if the item is glowing
     * @return this
     */
    public ItemBuilder glow(Boolean glow) {
        return nbtTag(NMSAdapter.GLOW_TAG, glow);
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        if (enchantments == null)
            enchantments = new HashMap<>();

        enchantments.put(enchantment, level);
        return this;
    }

    public Boolean unbreakable() {
        return nbtTags == null ? null : (Boolean) nbtTags.get("Unbreakable");
    }

    public Boolean glow() {
        return nbtTags == null ? null : (Boolean) nbtTags.get(NMSAdapter.GLOW_TAG);
    }

    public ItemStack build() {
        ItemStack result = new ItemStack(type, amount == null ? 1 : amount, durability);

        ItemAdapter adapter = ADAPTER.getItemAdapter(result);
        if (unbreakable() != null) {
            adapter.setUnbreakable(unbreakable());
            result = adapter.getTarget();
        }

        if (nbtTags != null) {
            adapter.setNbtTags(nbtTags);
            result = adapter.getTarget();
        }

        ItemMeta itemMeta = result.getItemMeta();

        if (displayName != null)
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore != null)
            itemMeta.setLore(lore);
        if (itemFlags != null)
            itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[]{}));
        if (enchantments != null)
            result.addUnsafeEnchantments(enchantments);

        result.setItemMeta(itemMeta);

        return result;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("type", type.name()); //implicit null check
        result.put("amount", amount);
        result.put("durability", durability);
        result.put("display-name", displayName);
        result.put("lore", lore);
        result.put("item-flags", itemFlags);
        result.put("enchantments", enchantments);
        result.put("nbt-tags", nbtTags);

        MapUtils.removeNullValues(result);

        return result;
    }

    @Override
    public ItemBuilder clone() {
        try {
            ItemBuilder builder = (ItemBuilder) super.clone();

            if (lore != null)
                builder.lore = new ArrayList<>(lore);
            if (itemFlags != null)
                builder.itemFlags = new HashSet<>(itemFlags);
            if (nbtTags != null)
                builder.nbtTags = new LinkedHashMap<>(nbtTags);
            if (enchantments != null)
                builder.enchantments = new HashMap<>(enchantments);

            return builder;
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

    /**
     * Copies the data from the given ItemStack into a new builder.
     * If the item is null, air or not have an ItemMeta, the builder will be empty
     * @param item the item to copy from
     * @return a new builder with the data from the given item
     */
    public static ItemBuilder fromItemStack(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta())
            return new ItemBuilder();

        ItemBuilder result = new ItemBuilder();

        result.type = item.getType();
        result.amount = item.getAmount();
        result.durability = item.getDurability();
        result.enchantments = item.getEnchantments();

        ItemMeta meta = item.getItemMeta();
        result.displayName = meta.hasDisplayName() ? meta.getDisplayName() : null;
        result.lore = meta.hasLore() ? meta.getLore() : null;
        result.itemFlags = meta.getItemFlags();

        result.nbtTags = ADAPTER.getItemAdapter(item).getNbtTags();

        return result;
    }

    @SuppressWarnings("unchecked")
    public static ItemBuilder deserialize(Map<String, Object> map) {
        ItemBuilder result = new ItemBuilder();

        try {
            result.type(WMaterial.valueOf(map.get("type").toString().toUpperCase()));
        } catch (IllegalArgumentException ignored) {
            result.type = Material.valueOf(map.get("type").toString().toUpperCase());
        }

        if (map.containsKey("amount"))
            result.amount(Integer.parseInt(map.get("amount").toString()));
        if (map.containsKey("durability"))
            result.durability(Short.parseShort(map.get("durability").toString()));
        if (map.containsKey("display-name"))
            result.displayName(map.get("display-name").toString());
        if (map.containsKey("lore"))
            result.lore((List<String>) map.get("lore"));
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
