package com.wizardlybump17.wlib.task;

import com.wizardlybump17.wlib.WLib;
import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

public class UpdateInventoryTask extends BukkitRunnable {

    private static UpdateInventoryTask INSTANCE;

    private int currentTick;
    private final Set<UpdatableHolder> holders = new HashSet<>();

    @Override
    public void run() {
        final Iterator<UpdatableHolder> iterator = holders.iterator();
        while (iterator.hasNext()) {
            final UpdatableHolder holder = iterator.next();

            if (holder.isStopped()) {
                iterator.remove();
                continue;
            }

            try {
                if (holder.getUpdateTime() < 1)
                    holder.update();
                else if (currentTick != 0 && currentTick % holder.getUpdateTime() == 0)
                    holder.update();
            } catch (Throwable throwable) {
                WLib.getInstance().getLogger().log(
                        Level.SEVERE,
                        "An exception happened when trying to update an inventory named " + holder.getOriginalInventory().getTitle() + ". It was removed from the update list to avoid new exceptions.",
                        throwable
                );
                iterator.remove();
            }
        }
        currentTick++;
    }

    public void add(UpdatableHolder holder) {
        holders.add(holder);
    }

    public static UpdateInventoryTask getInstance() {
        return INSTANCE == null ? INSTANCE = new UpdateInventoryTask() {{
            runTaskTimer(WLib.getInstance(), 1, 1);
        }} : INSTANCE;
    }
}
