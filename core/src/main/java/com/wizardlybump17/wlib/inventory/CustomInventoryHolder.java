package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.paginated.PaginatedInventory;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class CustomInventoryHolder implements InventoryHolder {

    @NotNull
    private PaginatedInventory holder;
    @NotNull
    private Inventory inventory;

    public CustomInventoryHolder(@NotNull PaginatedInventory holder) {
        this.holder = holder;
    }
}
