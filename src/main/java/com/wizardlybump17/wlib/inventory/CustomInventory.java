package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.item.ItemButton;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

@Getter
public class CustomInventory {

    private final Inventory inventory;
    private final CustomInventoryHolder holder;
    private final String title;
    private final int size;

    public CustomInventory(String title, int size) {
        inventory = (holder = new CustomInventoryHolder(this.title = title, this.size = size)).getInventory();
    }

    public CustomInventory item(int slot, ItemButton itemButton) {
        holder.addButton(slot, itemButton);
        return this;
    }

    public CustomInventory item(ItemButton itemButton) {
        if (inventory.firstEmpty() == -1) return this;
        holder.addButton(inventory.firstEmpty(), itemButton);
        return this;
    }

    public CustomInventory items(List<ItemButton> itemButtons) {
        for (ItemButton itemButton : itemButtons) item(itemButton);
        return this;
    }

    public CustomInventory items(ItemButton... itemButtons) {
        return items(Arrays.asList(itemButtons));
    }

    public CustomInventory removeItem(int slot) {
        holder.removeButton(slot);
        return this;
    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }

    public boolean isFull() {
        return inventory.firstEmpty() == -1;
    }

    public ItemButton[] getItems() {
        ItemButton[] items = new ItemButton[size];
        for (int i = 0; i < size; i++) items[i] = holder.getButtons().get(i);
        return items;
    }
}
