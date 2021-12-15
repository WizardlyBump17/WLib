package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.listener.InventoryListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class PaginatedInventory {

    private final List<CustomInventory> inventories;
    private int currentPage;
    private HumanEntity player;
    private final List<InventoryListener<?>> listeners;
    @Setter
    private boolean unregisterListeners = true;
    private boolean listenersRegistered;
    private final Map<String, Object> data;

    public void show(@NotNull HumanEntity player, int page) {
        if (page < 0 || page >= inventories.size())
            return;

        if (this.player == null) {
            this.player = player;
            if (updatePlayerInventory(page))
                return;
        }

        if (this.player != player) {
            weakCopy().show(player, page);
            return;
        }

        currentPage = page;
        if (updatePlayerInventory(page))
            return;

        player.openInventory(inventories.get(page).getBukkitInventory());
        inventories.get(page).setButtons();

        startListeners();
    }

    private boolean updatePlayerInventory(int page) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory != null && inventory.getHolder() instanceof CustomInventoryHolder && inventory.getSize() == inventories.get(page).getSize()) {
            inventories.get(page).setButtons(inventory);
            ((CustomInventoryHolder) inventory.getHolder()).setHolder(this);
            ((CustomInventoryHolder) inventory.getHolder()).setInventory(inventory);
            currentPage = page;
            startListeners();
            return true;
        }
        return false;
    }

    public void show(@NotNull HumanEntity player) {
        show(player, 0);
    }

    public void showNextPage(@NotNull HumanEntity player) {
        unregisterListeners = false;
        show(player, currentPage + 1);
        unregisterListeners = true;
    }

    public void showPreviousPage(@NotNull HumanEntity player) {
        unregisterListeners = false;
        show(player, currentPage - 1);
        unregisterListeners = true;
    }

    public CustomInventory getCurrentInventory() {
        return inventories.get(currentPage);
    }

    public void startListeners() {
        if (listenersRegistered)
            return;

        for (InventoryListener<?> listener : listeners)
            Bukkit.getPluginManager().registerEvent(
                    listener.getEventClass(),
                    listener,
                    listener.getPriority(),
                    (l, event) -> listener.fire(event, this),
                    listener.getPlugin(),
                    listener.isIgnoreCancelled()
            );

        listenersRegistered = true;
    }

    public void setData(String key, Object data) {
        this.data.put(key, data);
    }

    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    @Nullable
    public Object getData(String key) {
        return data.get(key);
    }

    public void removeData(String key) {
        data.remove(key);
    }

    public void stopListeners() {
        if (!listenersRegistered)
            return;

        for (InventoryListener<?> listener : listeners)
            HandlerList.unregisterAll(listener);

        listenersRegistered = false;
    }

    private PaginatedInventory weakCopy() {
        PaginatedInventory inventory = new PaginatedInventory(inventories, listeners, data);
        inventory.currentPage = currentPage;
        inventory.unregisterListeners = unregisterListeners;
        return inventory;
    }
}
