package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import com.wizardlybump17.wlib.inventory.listener.InventoryListener;
import com.wizardlybump17.wlib.util.CollectionUtil;
import com.wizardlybump17.wlib.util.MapUtils;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class PaginatedInventory {

    private final Set<InventoryListener<? extends Event>> listeners;
    private final List<CustomInventory> inventories;
    private int page;
    private HumanEntity entity;
    private boolean listenersStarted;
    @NotNull
    private final Map<String, Object> data;
    private boolean changingPages;

    public CustomInventory current() {
        return inventories.get(page);
    }

    public void addInventory(CustomInventory inventory) {
        inventories.add(inventory);
    }

    /**
     * Opens the other inventory to the player. It also inserts the current data to the other inventory
     * @param player the player
     * @param page the inventory page to be open
     * @param other the other inventory
     */
    public void show(HumanEntity player, int page, PaginatedInventory other) {
        other.data.putAll(data);
        other.show(player, page);
    }

    /**
     * Opens the other inventory to the player. It also inserts the current data to the other inventory
     * @param player the player
     * @param other the other inventory
     */
    public void show(HumanEntity player, PaginatedInventory other) {
        show(player, 0, other);
    }

    public void show(HumanEntity player, int page) {
        if (entity == null)
            entity = player;

        if (entity != player) {
            new PaginatedInventory(
                    CollectionUtil.clone(listeners),
                    CollectionUtil.clone(inventories),
                    MapUtils.clone(data)
            ).show(player, page);
            return;
        }

        if (page < 0 || page >= inventories.size())
            return;

        this.page = page;

        CustomInventory inventory = inventories.get(page);
        if (inventory instanceof UpdatableInventory)
            ((UpdatableHolder) inventory.getOwner()).start();
        player.openInventory(inventory.getBukkitInventory());

        startListeners();
    }

    private void startListeners() {
        if (!listenersStarted) {
            for (InventoryListener<? extends Event> listener : listeners)
                Bukkit.getPluginManager().registerEvent(
                        listener.getEvent(),
                        listener.getListener(),
                        listener.getPriority(),
                        (l, event) ->
                                listener.call(event, inventories.get(page)),
                        WLib.getInstance()
                );
            listenersStarted = true;
        }
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public PaginatedInventory setData(String key, Object data) {
        this.data.put(key, data);
        return this;
    }

    public void show(HumanEntity player) {
        show(player, 0);
    }

    public void showNextPage(HumanEntity player) {
        changingPages = true;
        show(player, page + 1);
        changingPages = false;
    }

    public void showPreviousPage(HumanEntity player) {
        changingPages = true;
        show(player, page - 1);
        changingPages = false;
    }

    public void stopListeners() {
        for (InventoryListener<? extends Event> listener : listeners)
            HandlerList.unregisterAll(listener.getListener());
    }
}
