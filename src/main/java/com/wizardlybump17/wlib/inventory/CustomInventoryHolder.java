package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.action.CloseInventoryAction;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.item.UpdatableItemButton;
import com.wizardlybump17.wlib.runnable.UpdateInventoryTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomInventoryHolder implements InventoryHolder {

    private int updateTaskId;
    private boolean updateStarted;
    private final Inventory inventory;
    private final Map<Integer, ItemButton> buttons = new HashMap<>();
    private final CloseInventoryAction closeAction;
    private final int updateTime;
    @Setter private boolean ignoreCloseEvent;

    public CustomInventoryHolder(String title, int size) {
        this(title, size, null, 20);
    }

    public CustomInventoryHolder(String title, int size, int updateTime) {
        this(title, size, null, updateTime);
    }

    public CustomInventoryHolder(String title, int size, CloseInventoryAction closeAction, int updateTime) {
        inventory = Bukkit.createInventory(this, size, title);
        this.closeAction = closeAction;
        this.updateTime = updateTime;
    }

    public void addButton(int slot, ItemButton button) {
        buttons.put(slot, button);
        inventory.setItem(slot, button == null ? new ItemStack(Material.AIR) : button.getItemStack());
        if (!(button instanceof UpdatableItemButton)) return;
        for (UpdatableItemButton child : ((UpdatableItemButton) button).getChildren())
            addButton(slot + child.getPlaceAt(), child);
        if (!updateStarted) {
            updateTaskId = new UpdateInventoryTask(this)
                    .runTaskTimer(WLib.getPlugin(WLib.class), updateTime, updateTime).getTaskId();
            updateStarted = true;
        }
    }

    public void removeButton(int slot) {
        if (hasButton(slot)) return;
        ItemButton button = getButton(slot);
        inventory.clear(slot);
        buttons.remove(slot);
        if (!(button instanceof UpdatableItemButton)) return;
        for (UpdatableItemButton child : ((UpdatableItemButton) button).getChildren())
            removeButton(slot + child.getPlaceAt());
    }

    public ItemButton getButton(int slot) {
        return buttons.get(slot);
    }

    public boolean hasButton(int slot) {
        return buttons.containsKey(slot);
    }

    public void onClick(InventoryClickEvent event) {
        if (!buttons.containsKey(event.getRawSlot())) return;
        ItemButton itemButton = buttons.get(event.getRawSlot());
        if (itemButton == null || itemButton.getItemClickAction() == null) return;
        itemButton.getItemClickAction().execute(event);
    }

    public void onClose(InventoryCloseEvent event) {
        if (updateStarted) Bukkit.getScheduler().cancelTask(updateTaskId);
        if (closeAction != null && !ignoreCloseEvent) closeAction.execute(event);
    }
}
