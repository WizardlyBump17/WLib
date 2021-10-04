package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.adapter.EntityAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.inventory.holder.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.util.MapUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EntityListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CustomInventoryHolder)) return;
        event.setCancelled(true);

        CustomInventoryHolder holder = (CustomInventoryHolder) inventory.getHolder();
        if (!holder.hasButton(event.getRawSlot())) return;

        ItemButton item = holder.getButton(event.getRawSlot());
        if (item != null) {
            if (item.getClickAction() != null)
                item.getClickAction().onClick(event, MapUtils.mapOf("page", holder.getPage()));
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof UpdatableHolder)
            ((UpdatableHolder) holder).stop();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        EntityAdapter.deleteFromCache(event.getPlayer());
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        EntityAdapter.deleteFromCache(event.getEntity());
    }

    @EventHandler
    public void onCreativeClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE)
            return;

        final ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType() == Material.AIR)
            return;

        if (NMSAdapterRegister.getInstance().current().getItemAdapter(cursor).hasGlow()) {
            final ItemMeta itemMeta = cursor.getItemMeta();
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.removeEnchant(Enchantment.ARROW_INFINITE);
            cursor.setItemMeta(itemMeta);
        }
    }
}
