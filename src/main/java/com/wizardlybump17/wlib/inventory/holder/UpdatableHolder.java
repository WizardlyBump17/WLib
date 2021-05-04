package com.wizardlybump17.wlib.inventory.holder;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.item.UpdatableItem;
import com.wizardlybump17.wlib.item.Item;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Map;

@Getter
public class UpdatableHolder extends CustomInventoryHolder {

    private final int updateTime;
    private int taskId;

    public UpdatableHolder(UpdatableInventory inventory, int updateTime) {
        super(inventory);
        this.updateTime = updateTime;
    }

    public void start() {
        if (taskId == 0)
            taskId = Bukkit.getScheduler().runTaskTimer(WLib.getInstance(), this::update, updateTime, updateTime).getTaskId();
    }

    public void update() {
        for (Map.Entry<Integer, ItemButton> entry : getButtons().entrySet()) {
            ItemButton value = entry.getValue();
            if (value instanceof UpdatableItem) {
                UpdatableItem item = (UpdatableItem) value;
                if (item.getUpdateAction() != null) {
                    Item.ItemBuilder builder = Item.fromItemStack(item.getItemStack());
                    item.getUpdateAction().update(builder, (UpdatableInventory) inventory);
                    item.setItemStack(builder.build());
                    inventory.getBukkitInventory().setItem(entry.getKey(), item.getItemStack());

                    for (HumanEntity viewer : inventory.getBukkitInventory().getViewers())
                        ((Player) viewer).updateInventory();
                }
            }
        }
    }

    public void stop() {
        if (taskId == 0) return;
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = 0;
    }
}
