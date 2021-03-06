package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import lombok.Data;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PaginatedInventory {
    
    private final List<CustomInventory> inventories = new ArrayList<>();
    private final Map<HumanEntity, Integer> playerPages = new HashMap<>();

    public void addInventory(CustomInventory inventory) {
        inventories.add(inventory);
    }

    public void show(HumanEntity player, int page) {
        if (page < 0 || page >= inventories.size())
            return;
        if (playerPages.containsKey(player))
            if (inventories.get(page).equals(inventories.get(playerPages.get(player))))
                return;

        playerPages.put(player, page);
        CustomInventory inventory = inventories.get(page);
        if (inventory instanceof UpdatableInventory)
            ((UpdatableHolder) inventory.getOwner()).start();
        player.openInventory(inventory.getBukkitInventory());
    }

    public void show(HumanEntity player) {
        show(player, 0);
    }

    public void showNextPage(HumanEntity player) {
        show(player, playerPages.getOrDefault(player, -1) + 1);
    }

    public void showPreviousPage(HumanEntity player) {
        show(player, playerPages.getOrDefault(player, 1) - 1);
    }
}
