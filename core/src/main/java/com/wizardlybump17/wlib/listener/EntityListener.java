package com.wizardlybump17.wlib.listener;

import com.wizardlybump17.wlib.adapter.EntityAdapter;
import com.wizardlybump17.wlib.adapter.NMSAdapterRegister;
import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class EntityListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CustomInventoryHolder))
            return;

        final PaginatedInventory paginatedInventory = ((CustomInventoryHolder) inventory.getHolder()).getHolder();

        event.setCancelled(true);

        final CustomInventory customInventory = paginatedInventory.getCurrentInventory();

        ItemButton item = customInventory.getButton(event.getRawSlot());
        if (item != null && item.getClickAction() != null)
            item.getClickAction().onClick(event, paginatedInventory);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        final InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof CustomInventoryHolder))
            return;

        final PaginatedInventory paginatedInventory = ((CustomInventoryHolder) holder).getHolder();
        if (!paginatedInventory.isUnregisterListeners())
            return;

        paginatedInventory.stopListeners();
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
