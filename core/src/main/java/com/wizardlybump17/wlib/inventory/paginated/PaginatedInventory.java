package com.wizardlybump17.wlib.inventory.paginated;

import com.wizardlybump17.wlib.inventory.CustomInventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PaginatedInventory {

    private final List<CustomInventory> inventories;
    private int currentPage;
    private HumanEntity player;

    public void show(@NotNull HumanEntity player, int page) {
        if (this.player == null)
            this.player = player;

        if (this.player != player) {
            weakCopy().show(player, page);
            return;
        }

        if (page < 0 || page >= inventories.size())
            return;

        currentPage = page;
        player.openInventory(inventories.get(page).getBukkitInventory());
    }

    public void show(@NotNull HumanEntity player) {
        show(player, 0);
    }

    public void showNextPage(@NotNull HumanEntity player) {
        show(player, currentPage + 1);
    }

    public void showPreviousPage(@NotNull HumanEntity player) {
        show(player, currentPage - 1);
    }

    public CustomInventory getCurrentInventory() {
        return inventories.get(currentPage);
    }

    private PaginatedInventory weakCopy() {
        PaginatedInventory inventory = new PaginatedInventory(inventories);
        inventory.currentPage = currentPage;
        return inventory;
    }
}
