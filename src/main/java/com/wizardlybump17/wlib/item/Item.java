package com.wizardlybump17.wlib.item;

import com.wizardlybump17.wlib.reflection.ItemAdapter;
import com.wizardlybump17.wlib.reflection.ReflectionAdapter;
import com.wizardlybump17.wlib.reflection.ReflectionAdapterRegister;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Item {

    private Material type;
    private int amount;
    private short durability;
    private String displayName;
    private List<String> lore;
    private boolean glow, unbreakable;
    private Map<Enchantment, Integer> enchantments;
    private Set<ItemFlag> flags;
    private Map<String, Object> nbtTags;

    public static ItemBuilder fromItemStack(ItemStack item) {
        ItemAdapter itemAdapter = ReflectionAdapterRegister.getInstance().getServerAdapter().getItemAdapter(item);
        ItemMeta itemMeta = item.getItemMeta();
        return builder()
                .type(item.getType())
                .amount(item.getAmount())
                .durability(item.getDurability())
                .displayName(itemMeta.getDisplayName())
                .lore(itemMeta.getLore())
                .flags(itemMeta.getItemFlags())
                .unbreakable(itemMeta.spigot().isUnbreakable())
                .enchantments(item.getEnchantments())
                .nbtTags(itemAdapter.getNbtTags());
    }

    public static class ItemBuilder {

        private boolean amountSet;

        public ItemBuilder amount(int amount) {
            this.amount = amount;
            amountSet = true;
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
            ReflectionAdapter adapter = ReflectionAdapterRegister.getInstance().getServerAdapter();
            return adapter.nbtToJava(nbtTags.get(key));
        }

        public ItemStack build() {
            ItemStack itemStack = new ItemStack(type, amountSet ? amount : 1, durability);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(displayName == null ? null : displayName.replace('&', '§'));
                itemMeta.setLore(lore);
                itemMeta.spigot().setUnbreakable(unbreakable);
                itemMeta.addItemFlags(flags == null ? new ItemFlag[0] : flags.toArray(new ItemFlag[]{}));
                itemStack.setItemMeta(itemMeta);
            }
            itemStack.addUnsafeEnchantments(enchantments == null ? new HashMap<>() : enchantments);

            if (nbtTags != null) {
                ItemAdapter itemAdapter = ReflectionAdapterRegister.getInstance().getServerAdapter().getItemAdapter(itemStack);
                itemAdapter.setNbtTags(nbtTags, false);
                itemStack = itemAdapter.getTarget();
            }

            return itemStack;
        }
    }
}
