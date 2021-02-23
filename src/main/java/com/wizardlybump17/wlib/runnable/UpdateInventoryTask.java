package com.wizardlybump17.wlib.runnable;

import com.wizardlybump17.wlib.inventory.CustomInventoryHolder;
import com.wizardlybump17.wlib.inventory.item.ItemButton;
import com.wizardlybump17.wlib.inventory.item.UpdatableItemButton;
import com.wizardlybump17.wlib.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

@Getter
public class UpdateInventoryTask extends BukkitRunnable {

    private final CustomInventoryHolder holder;
    private int currentTick;
    private boolean started;

    public UpdateInventoryTask(CustomInventoryHolder holder) {
        this.holder = holder;
        currentTick = holder.getUpdateTime();
    }

    @Override
    public void run() {
        started = true;
        for (Map.Entry<Integer, ItemButton> entry : holder.getButtons().entrySet()) {
            ItemButton item = entry.getValue();
            if (!(item instanceof UpdatableItemButton)) continue;
            updateItem(entry.getKey(), (UpdatableItemButton) item);
        }
        currentTick += holder.getUpdateTime();
    }

    private void updateItem(int slot, UpdatableItemButton item) {
        if (currentTick % item.getUpdateTime() != 0) return;
        ItemBuilder builder = ItemBuilder.fromItemStack(item.getItemStack());
        item.getUpdateAction().onUpdate(builder);
        item.setItemStack(builder.build());
        holder.addButton(slot, item);
    }
}
