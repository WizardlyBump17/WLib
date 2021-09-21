package com.wizardlybump17.wlib.inventory.holder;

import com.wizardlybump17.wlib.UpdateInventoryTask;
import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.item.UpdatableItem;
import com.wizardlybump17.wlib.item.Item;
import lombok.Getter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Map;

@Getter
public class UpdatableHolder extends CustomInventoryHolder {

    private final int updateTime;

    public UpdatableHolder(UpdatableInventory inventory, int updateTime) {
        super(inventory);
        this.updateTime = updateTime;
    }

    public UpdatableHolder(UpdatableInventory inventory, int updateTime, int page) {
        super(inventory, page);
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
                if (item.getUpdateAction() != null) {
                    Item.ItemBuilder builder = Item.fromItemStack(item.getItemStack());
                    item.getUpdateAction().update(builder, (UpdatableInventory) inventory);

                    getInventory().setItem(entry.getKey(), builder.build());
                }
            }
        }

        for (HumanEntity viewer : inventory.getBukkitInventory().getViewers())
            ((Player) viewer).updateInventory();
    }

    public void stop() {
        UpdateInventoryTask.getInstance().remove(this);
    }
}
