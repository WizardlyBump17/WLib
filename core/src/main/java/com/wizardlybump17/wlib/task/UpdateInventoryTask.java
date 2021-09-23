package com.wizardlybump17.wlib.task;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class UpdateInventoryTask extends BukkitRunnable {

    private static UpdateInventoryTask INSTANCE;

    private int currentTick;
    private final Set<UpdatableHolder> holders = new HashSet<>();
    private final Set<UpdatableHolder> cancelled = new HashSet<>();

    @Override
    public void run() {
        for (UpdatableHolder holder : holders) {
            if (holder.getUpdateTime() < 1)
                holder.update();
            else if (currentTick != 0 && currentTick % holder.getUpdateTime() == 0)
                holder.update();
        }
        currentTick++;
        holders.removeAll(cancelled);
    }

    public void add(UpdatableHolder holder) {
        holders.add(holder);
    }

    public void remove(UpdatableHolder holder) {
        cancelled.add(holder);
    }

    public static UpdateInventoryTask getInstance() {
        return INSTANCE == null ? INSTANCE = new UpdateInventoryTask() {{
            runTaskTimer(WLib.getInstance(), 1, 1);
        }} : INSTANCE;
    }
}
