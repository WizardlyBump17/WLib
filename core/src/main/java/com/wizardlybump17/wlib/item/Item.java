package com.wizardlybump17.wlib.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.wizardlybump17.wlib.adapter.ItemAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.adapter.WMaterial;
import com.wizardlybump17.wlib.util.ArrayUtils;
import com.wizardlybump17.wlib.util.ListUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Item {

    private static final NMSAdapter ADAPTER = NMSAdapterRegister.getInstance().current();

    private Material type;
    private int amount;
    private short durability;
    private String displayName;
    private List<String> lore;
    private boolean glow, unbreakable;
    private Map<Enchantment, Integer> enchantments;
    private Set<ItemFlag> flags;
    private Map<String, Object> nbtTags;

    public static ItemBuilder getHead(String base64, int amount) {
        try {
            ItemStack itemStack = builder().type(WMaterial.PLAYER_HEAD).amount(amount).build();
            SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();

            GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(base64.getBytes()), null);
            PropertyMap properties = gameProfile.getProperties();

            properties.put("textures", new Property("textures", base64));

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

    public static ItemBuilder getHead(UUID player, int amount) {
        ItemStack item = builder().type(WMaterial.PLAYER_HEAD).amount(amount).build();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(Bukkit.getOfflinePlayer(player).getName());
        item.setItemMeta(meta);
        return fromItemStack(item);
    }

    public static ItemBuilder fromItemStack(ItemStack item) {
        if (item == null)
            return builder();
        ItemAdapter itemAdapter = ADAPTER.getItemAdapter(item);
        ItemMeta itemMeta = item.getItemMeta();
        return builder()
                .type(item.getType())
                .amount(item.getAmount())
                .durability(item.getDurability())
                .displayName(itemMeta.getDisplayName())
                .lore(itemMeta.getLore())
                .flags(itemMeta.getItemFlags())
                .unbreakable(itemAdapter.isUnbreakable())
                .enchantments(item.getEnchantments())
                .nbtTags(itemAdapter.getNbtTags());
    }

    public static ItemBuilder deserialize(Map<String, Object> args) {
        return builder().setData(args);
    }

    public static class ItemBuilder implements ConfigurationSerializable {

        private boolean amountSet;
        private WMaterial wmaterial;

        public String getDisplayName() {
            return displayName;
        }

        public Material getType() {
            fixMaterial();
            return type;
        }

        public int getAmount() {
            return amount;
        }

        public short getDurability() {
            return durability;
        }

        public Map<Enchantment, Integer> getEnchantments() {
            return enchantments == null ? enchantments = new HashMap<>() : enchantments;
        }

        public Set<ItemFlag> getFlags() {
            return flags;
        }

        public ItemBuilder amount(int amount) {
            this.amount = amount;
            amountSet = true;
            return this;
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
                nbtTags = new HashMap<>();
            return nbtTags.containsKey(key);
        }

        public boolean hasFlag(ItemFlag flag) {
            if (flags == null)
                flags = new HashSet<>();
            return flags.contains(flag);
        }

        public Object getNbtTag(String key) {
            if (nbtTags == null)
                nbtTags = new HashMap<>();
            return ADAPTER.nbtToJava(nbtTags.get(key));
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
                    .unbreakable(builder.unbreakable);
        }

        public ItemStack build() {
            ItemStack itemStack;
            if (wmaterial != null) { //ill add all the items in WMaterial :D
                itemStack = fixMaterial();
                itemStack.setAmount(amountSet ? amount : 1);
            } else
                itemStack = new ItemStack(type, amountSet ? amount : 1, durability);

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
            if (nbtTags != null) {
                itemAdapter.setNbtTags(nbtTags, false);
                itemStack = itemAdapter.getTarget();
            }
            return itemStack;
        }

        public List<String> getLore() {
            return lore == null ? lore = new ArrayList<>() : new ArrayList<>(lore);
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new LinkedHashMap<>();

            fixMaterial();
            map.put("material", type);
            map.put("amount", amountSet ? amount : 1);
            if (durability != 0) map.put("durability", durability);
            if (displayName != null) map.put("display-name", displayName);
            if (lore != null) map.put("lore", lore);
            if (flags != null) map.put("flags", flags);
            if (nbtTags != null) map.put("nbt-tags", nbtTags);
            if (unbreakable) map.put("unbreakable", true);
            if (glow) map.put("glow", true);
            if (enchantments != null) map.put("enchantments", enchantments);

            return map;
        }

        public ItemBuilder setData(Map<String, Object> args) {
            List<String> lore = new ArrayList<>();
            if (args.get("lore") != null)
                lore = new ListUtil((List<String>) args.get("lore")).replace('&', '§').getList();

            Set<ItemFlag> flags = new HashSet<>();
            if (args.get("flags") != null) {
                Collection<Object> flags1 = (Collection<Object>) args.get("flags");
                if (!flags1.isEmpty()) {
                    Object object = flags1.iterator().next();
                    if (object instanceof String)
                        for (Object o : flags1)
                            flags.add(ItemFlag.valueOf(o.toString().toUpperCase()));

                    if (object instanceof ItemFlag)
                        flags.add((ItemFlag)object);
                }
            }

            Map<Enchantment, Integer> enchantments = new HashMap<>();
            if (args.get("enchantments") != null) {
                Map enchantments1 = (Map) args.get("enchantments");
                if (!enchantments1.isEmpty()) {
                    for (Object o : enchantments1.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        if (entry.getKey() instanceof String) {
                            Map<String, Integer> ench = (Map<String, Integer>) enchantments1;
                            ench.forEach((name, level) -> enchantments.put(Enchantment.getByName(name.toUpperCase()), level));
                        }
                        if (entry.getKey() instanceof Enchantment)
                            enchantments.putAll(enchantments1);
                        break;
                    }
                }
            }

            if (args.containsKey("material"))
                type(Material.valueOf(args.get("material").toString().toUpperCase()));
            if (args.containsKey("amount"))
                amount((int) args.get("amount"));
            if (args.containsKey("durability"))
                durability((short) args.get("durability"));
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
            return this;
        }

        private ItemStack fixMaterial() {
            if (wmaterial != null) {
                ItemStack item;
                if (ArrayUtils.contains(wmaterial.getAcceptedData(), (int) durability))
                    item = new ItemStack(getFixedMaterialFromRelated(), 1, durability);
                else
                    item = ADAPTER.getFixedMaterial(wmaterial);
                type = item.getType();

                if (type.name().equals("AIR") && !wmaterial.name().equals("AIR")) //lets try get the right item using the related
                    item.setType(type = getFixedMaterialFromRelated());

                if (!wmaterial.name().endsWith("_SPAWN_EGG"))
                    durability = item.getDurability();
                return item;
            }
            return null;
        }

        private Material getFixedMaterialFromRelated() {
            WMaterial[] values = WMaterial.values();
            for (WMaterial value : values)
                if (value.getData() == durability && ArrayUtils.contains(value.getRelated(), wmaterial.name()))
                    return value.getMaterial();
            return Material.AIR;
        }
    }
}
