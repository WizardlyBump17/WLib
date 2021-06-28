package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.inventory.UpdatableInventory;
import com.wizardlybump17.wlib.item.Item;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class UpdatableItem extends ItemButton {

    private final UpdateAction updateAction;

    public UpdatableItem(ItemStack itemStack, ClickAction clickAction, UpdateAction updateAction) {
        super(itemStack, clickAction);
        this.updateAction = updateAction;
    }

    public UpdatableItem(ItemStack itemStack, UpdateAction updateAction) {
        this(itemStack, null, updateAction);
    }

    public interface UpdateAction {

        /**
         * @param builder the item builder of this item. Useful for who wants to edit the item
         * @param inventory the inventory that is requiring the update
         */
        void update(Item.ItemBuilder builder, UpdatableInventory inventory);
    }
}
