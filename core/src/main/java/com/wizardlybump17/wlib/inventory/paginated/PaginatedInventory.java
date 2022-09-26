package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.listener.InventoryListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@RequiredArgsConstructor
public class PaginatedInventory {

    private final int id = ThreadLocalRandom.current().nextInt();
    private final List<CustomInventory> inventories;
    private int currentPage;
    private HumanEntity player;
    private final List<InventoryListener<?>> listeners;
    @Setter
    private boolean unregisterListeners = true;
    private boolean listenersRegistered;
    private final Map<Object, Object> data;

    public void show(@NotNull HumanEntity player, int page) {
        if (page < 0 || page >= inventories.size())
            return;

        if (this.player == null)
            this.player = player;

        if (this.player != player) {
            weakCopy().show(player, page);
            return;
        }

        currentPage = page;

        player.openInventory(inventories.get(page).getBukkitInventory());
        inventories.get(page).setButtons();

        startListeners();
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

    public void setData(Object key, Object data) {
        this.data.put(key, data);
    }

    public boolean hasData(Object key) {
        return data.containsKey(key);
    }

    /**
     * Gets the data with the provided key.<br>
     * It will try to cast the data to the provided type
     *
     * @param key the key
     * @param <T> the type of the data
     * @return the data
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getData(Object key) {
        return (T) data.get(key);
    }

    public void removeData(Object key) {
        data.remove(key);
    }

    public void stopListeners() {
        if (!listenersRegistered)
            return;

        for (InventoryListener<?> listener : listeners)
            listener.unregister();

        listenersRegistered = false;
    }

    private PaginatedInventory weakCopy() {
        PaginatedInventory inventory = new PaginatedInventory(inventories, listeners, data);
        inventory.currentPage = currentPage;
        inventory.unregisterListeners = unregisterListeners;
        return inventory;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PaginatedInventory other))
            return false;

        return other.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
