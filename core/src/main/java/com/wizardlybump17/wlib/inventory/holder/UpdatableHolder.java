package com.wizardlybump17.wlib.inventory.holder;

import com.wizardlybump17.wlib.task.UpdateInventoryTask;
import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.item.UpdatableItem;
import com.wizardlybump17.wlib.item.Item;
import lombok.Getter;

import java.util.Map;

@Getter
public class UpdatableHolder extends CustomInventoryHolder {

    private final int updateTime;
    private boolean stopped;

    public UpdatableHolder(UpdatableInventory inventory, int updateTime) {
        super(inventory);
        this.updateTime = updateTime;
    }

    public void start() {
        UpdateInventoryTask.getInstance().add(this);
    }

    public void update() {
        for (Map.Entry<Integer, ItemButton> entry : getButtons().entrySet()) {
            ItemButton value = entry.getValue();
            if (value instanceof UpdatableItem) {
                UpdatableItem item = (UpdatableItem) value;
                Item.ItemBuilder builder = Item.fromItemStack(item.getItemStack());
                item.getUpdateAction().update(builder);
                getInventory().setItem(entry.getKey(), builder.build());
                continue;
            }
            if (Item.fromItemStack(value.getItemStack()).hasGlow()) //set the item in the inventory again so the packet is sent
                getInventory().setItem(entry.getKey(), value.getItemStack());
        }
    }

    public void stop() {
        stopped = true;
    }
}
