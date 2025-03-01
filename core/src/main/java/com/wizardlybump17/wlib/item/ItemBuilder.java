package com.wizardlybump17.wlib.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.wizardlybump17.wlib.adapter.AttributeAdapter;
import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.item.handler.ItemMetaHandler;
import com.wizardlybump17.wlib.item.handler.model.ItemMetaHandlerModel;
import com.wizardlybump17.wlib.util.MapUtils;
import com.wizardlybump17.wlib.util.bukkit.ConfigUtil;
import com.wizardlybump17.wlib.util.bukkit.NamespacedKeyUtil;
import com.wizardlybump17.wlib.util.bukkit.StringUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SerializableAs("item-builder")
public class ItemBuilder implements ConfigurationSerializable, Cloneable {

    private static final ItemFlag[] EMPTY_ITEM_FLAG_ARRAY = new ItemFlag[0];

    private @NotNull Material type;
    private int amount;
    private final Map<Object, Object> customData;
    private ItemMetaHandler<?> metaHandler;
    private ItemMeta meta;

    public ItemBuilder(ItemStack item, Map<Object, Object> customData, ItemMetaHandler<?> metaHandler) {
        type = item == null ? Material.AIR : item.getType();
        amount = item == null ? 1 : item.getAmount();
        this.customData = customData;
        this.metaHandler = metaHandler;
        meta = item == null ? null : item.getItemMeta();
    }

    public ItemBuilder(ItemStack item, Map<Object, Object> customData) {
        this(item, customData, null);
        ItemMetaHandlerModel<?> metaHandlerModel = ItemMetaHandlerModel.getApplicableModel(type);
        if (metaHandlerModel != null)
            this.metaHandler = metaHandlerModel.createHandler(this);
    }

    public ItemBuilder() {
        this(new ItemStack(Material.AIR), new HashMap<>(), null);
    }

    @SuppressWarnings("unchecked")
    public <M extends ItemMeta> ItemBuilder consumeMeta(Consumer<M> consumer) {
        if (meta != null)
            consumer.accept((M) meta);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Contract("_, null -> null; _, !null -> !null")
    public <M extends ItemMeta, T> @Nullable T consumeMetaAndReturn(Function<M, T> consumer, @Nullable T defaultValue) {
        if (meta == null)
            return defaultValue;

        T result = consumer.apply((M) meta);
        return result == null ? defaultValue : result;
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemMeta> T getItemMeta() {
        return (T) meta;
    }

    @SuppressWarnings("unchecked")
    public <T, M extends ItemMeta> T getFromMeta(Function<M, T> supplier, T def) {
        if (meta == null)
            return def;

        T t = supplier.apply((M) meta);
        return t == null ? def : t;
    }

    @SuppressWarnings("unchecked")
    public <T, M extends ItemMeta> T getFromMeta(Function<M, T> supplier, Supplier<T> def) {
        if (meta == null)
            return def.get();

        T t = supplier.apply((M) meta);
        return t == null ? def.get() : t;
    }

    public ItemBuilder type(@NonNull Material material) {
        ItemFactory itemFactory = Bukkit.getItemFactory();

        type = material;
        meta = meta == null ? itemFactory.getItemMeta(type) : itemFactory.asMetaFor(meta, type);

        ItemMetaHandlerModel<?> model = ItemMetaHandlerModel.getApplicableModel(type);
        metaHandler = model == null ? null : model.createHandler(this);

        return this;
    }

    public Material type() {
        return type;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public int amount() {
        return amount;
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
        return consumeMeta(meta -> meta.setLore(lore == null ? null : Arrays.asList(lore)));
    }

    public ItemBuilder lore(@Nullable List<String> lore) {
        return consumeMeta(meta -> meta.setLore(lore));
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

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        return consumeMeta(meta -> meta.addEnchant(enchantment, level, true));
    }

    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        return consumeMeta(meta -> enchantments.forEach((enchantment, level) -> meta.addEnchant(enchantment, level, true)));
    }

    public Map<Enchantment, Integer> enchantments() {
        return getFromMeta(ItemMeta::getEnchants, Collections.emptyMap());

    }

    public ItemBuilder glow(@Nullable Boolean glow) {
        return consumeMeta(meta -> meta.setEnchantmentGlintOverride(glow));
    }

    public boolean glow() {
        return getFromMeta(meta -> {
            if (meta.hasEnchantmentGlintOverride())
                return meta.getEnchantmentGlintOverride();
            return false;
        }, false);
    }

    public boolean overrideGlow() {
        return getFromMeta(ItemMeta::hasEnchantmentGlintOverride, false);
    }

    @NotNull
    public String displayName() {
        return getFromMeta(ItemMeta::getDisplayName, "");
    }

    public ItemBuilder displayName(@Nullable String displayName) {
        return consumeMeta(meta -> meta.setDisplayName(displayName));
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
        Map<String, Object> actualReplacements = new HashMap<>();
        replacements.forEach((key, value) -> {
            if (key.charAt(0) != '{' || key.charAt(key.length() - 1) != '}')
                actualReplacements.put(key, value);
            else
                actualReplacements.put(key.substring(1, key.length() - 1), value);
        });

        List<String> lore = new ArrayList<>(lore());
        ListIterator<String> loreIterator = lore.listIterator();
        while (loreIterator.hasNext()) {
            String line = loreIterator.next();

            if (line.length() < 2 || line.charAt(0) != com.wizardlybump17.wlib.util.StringUtil.PLACEHOLDER_BEGIN || line.charAt(line.length() - 1) != com.wizardlybump17.wlib.util.StringUtil.PLACEHOLDER_END) {
                loreIterator.set(com.wizardlybump17.wlib.util.StringUtil.applyPlaceholders(line, actualReplacements));
                continue;
            }

            Object object = actualReplacements.get(line.substring(1, line.length() - 1));
            if (object instanceof Collection<?> collection) {
                loreIterator.remove();
                collection.forEach(element -> loreIterator.add(String.valueOf(element)));
                continue;
            }

            loreIterator.set(com.wizardlybump17.wlib.util.StringUtil.applyPlaceholders(line, actualReplacements));
        }

        return displayName(com.wizardlybump17.wlib.util.StringUtil.applyPlaceholders(displayName(), actualReplacements))
                .lore(lore);
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
        }, (Integer) null);
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

    @SuppressWarnings("unchecked")
    public <T extends ItemMetaHandler<?>> T metaHandler() {
        return (T) metaHandler;
    }

    public ItemBuilder metaHandler(ItemMetaHandler<?> metaHandler) {
        this.metaHandler = metaHandler;
        return this;
    }

    public ItemBuilder mergeLore(ItemBuilder other) {
        if (other == this)
            return this;

        consumeMeta(meta -> {
            List<String> lore = meta.getLore();
            if (lore == null)
                lore = new ArrayList<>();

            lore.addAll(other.lore());
            meta.setLore(lore);
        });
        return this;
    }

    public ItemBuilder mergeItemFlags(ItemBuilder other) {
        if (other == this)
            return this;

        consumeMeta(meta -> meta.addItemFlags(other.itemFlags().toArray(EMPTY_ITEM_FLAG_ARRAY)));
        return this;
    }

    public ItemBuilder mergeEnchantments(ItemBuilder other) {
        if (other == this)
            return this;

        consumeMeta(meta -> {
            for (Map.Entry<Enchantment, Integer> entry : other.enchantments().entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        });
        return this;
    }

    public ItemBuilder mergeNbtTags(ItemBuilder other) {
        if (other == this)
            return this;

        ItemAdapter.getInstance().copyPersistentData(other.container(), container());
        return this;
    }

    public @NonNull ItemBuilder attribute(@NonNull Attribute attribute, @NonNull AttributeModifier modifier) {
        return consumeMeta(meta -> meta.addAttributeModifier(attribute, modifier));
    }

    public @NonNull ItemBuilder attributes(@Nullable Multimap<Attribute, AttributeModifier> modifiers) {
        return consumeMeta(meta -> meta.setAttributeModifiers(modifiers));
    }

    public @NonNull ItemBuilder attributes(@Nullable Map<Attribute, Collection<AttributeModifier>> attributes) {
        consumeMeta(meta -> meta.setAttributeModifiers(null));
        if (attributes == null || attributes.isEmpty())
            return this;
        return consumeMeta(meta -> attributes.forEach((attribute, modifiers) -> modifiers.forEach(modifier -> meta.addAttributeModifier(attribute, modifier))));
    }

    public @NonNull Multimap<Attribute, AttributeModifier> attributes() {
        return getFromMeta(ItemMeta::getAttributeModifiers, ImmutableMultimap.of());
    }

    public @NotNull ItemStack build() {
        ItemStack item = new ItemStack(type);
        item.setItemMeta(meta);
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
        if (unbreakable())
            result.put("unbreakable", true);
        if (customModelData() != null)
            result.put("custom-model-data", customModelData());
        if (!customData().isEmpty())
            result.put("custom-data", customData());
        if (overrideGlow())
            result.put("glow", glow());

        Multimap<Attribute, AttributeModifier> attributes = attributes();
        if (!attributes.isEmpty())
            result.put("attributes", AttributeAdapter.getInstance().serialize(attributes));

        if (metaHandler != null)
            metaHandler.serialize(result);

        return MapUtils.removeEmptyValues(MapUtils.removeNullValues(result));
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(build(), new HashMap<>(customData), metaHandler);
    }

    /**
     * <p>Accesses and uses the {@link ItemMetaHandler}, if it is not {@code null}, with the given {@link Consumer}.</p>
     * @param consumer the consumer to accept the {@link ItemMetaHandler}
     * @return this {@link ItemBuilder}
     * @param <M> the type of the {@link ItemMetaHandler}
     */
    @SuppressWarnings("unchecked")
    public <M extends ItemMetaHandler<?>> @NonNull ItemBuilder consumeMetaHandler(@NonNull Consumer<M> consumer) {
        if (metaHandler != null)
            consumer.accept((M) metaHandler);
        return this;
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

    public static ItemBuilder deserialize(Map<String, Object> map) {
        ItemBuilder result = new ItemBuilder();

        result
                .type(Material.valueOf(ConfigUtil.<String>get("type", map).toUpperCase()))
                .amount(ConfigUtil.get("amount", map, 1))
                .damage(ConfigUtil.get("damage", map, 0))
                .displayName(ConfigUtil.map("display-name", map, () -> null, StringUtil::fancy))
                .lore(ConfigUtil.<List<String>, List<String>>map("lore", map, Collections::emptyList, lore -> StringUtil.colorize(lore, ArrayList::new)))
                .itemFlags(ConfigUtil.<List<String>>get("item-flags", map, Collections.emptyList()).stream().map(ItemFlag::valueOf).collect(Collectors.toSet()))
                .enchantments(MapUtils.mapKeys(ConfigUtil.<Map<String, Integer>>get("enchantments", map, Collections.emptyMap()), string -> Registry.ENCHANTMENT.get(NamespacedKeyUtil.fromString(string))))
                .unbreakable(ConfigUtil.get("unbreakable", map, false))
                .customModelData(ConfigUtil.get("custom-model-data", map, (Integer) null))
                .customData(ConfigUtil.get("custom-data", map, Collections.emptyMap()))
                .glow(ConfigUtil.get("glow", map, () -> null));

        Optional
                .ofNullable(ConfigUtil.<Map<String, Collection<AttributeModifier>>, Multimap<Attribute, AttributeModifier>>map(
                        "attributes",
                        map,
                        () -> null,
                        attributes -> AttributeAdapter.getInstance().deserialize(attributes)
                ))
                .ifPresent(result::attributes);

        ItemMetaHandlerModel<?> metaHandlerModel = ItemMetaHandlerModel.getApplicableModel(result.type());
        result.metaHandler(metaHandlerModel == null ? null : metaHandlerModel.createHandler(result));

        if (result.metaHandler != null)
            result.metaHandler.deserialize(map);

        return result;
    }
}
