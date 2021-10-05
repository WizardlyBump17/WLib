package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.ListenerBuilder;
import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.*;

@Data
public class PaginatedInventory {

    private final Set<ListenerBuilder<? extends Event>> listeners;
    private final List<CustomInventory> inventories;
    private int page;
    private HumanEntity entity;
    private boolean listenersStarted;

    public void addInventory(CustomInventory inventory) {
        inventories.add(inventory);
    }

    public void show(HumanEntity player, int page) {
        if (entity == null)
            entity = player;

        if (entity != player) {
            new PaginatedInventory(listeners, inventories).show(player, page);
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
            for (ListenerBuilder<? extends Event> listener : listeners)
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

    public void show(HumanEntity player) {
        show(player, 0);
    }

    public void showNextPage(HumanEntity player) {
        show(player, page + 1);
    }

    public void showPreviousPage(HumanEntity player) {
        show(player, page - 1);
    }

    public void stopListeners() {
        for (ListenerBuilder<? extends Event> listener : listeners)
            HandlerList.unregisterAll(listener.getListener());
    }
}
