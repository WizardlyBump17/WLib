package com.wizardlybump17.wlib.inventory.item;

import com.wizardlybump17.wlib.inventory.action.ItemClickAction;
import com.wizardlybump17.wlib.inventory.action.UpdateItemAction;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashSet;

@Getter
public class UpdatableItemButton extends ItemButton {

    private final int updateTime;
    private final UpdateItemAction updateAction;
    private final LinkedHashSet<UpdatableItemButton> children = new LinkedHashSet<>();
    private int placeAt;

    public UpdatableItemButton(ItemStack itemStack, ItemClickAction itemClickAction, int updateTime, UpdateItemAction updateAction) {
        super(itemStack, itemClickAction);
        this.updateTime = updateTime;
        this.updateAction = updateAction;
    }

    public UpdatableItemButton(ItemStack itemStack, int updateTime, UpdateItemAction updateAction) {
        this(itemStack, null, updateTime, updateAction);
    }

    public void addChild(UpdatableItemButton item, int placeAt) {
        children.add(item);
        item.placeAt = placeAt;
    }

    public void removeChild(UpdatableItemButton item) {
        children.remove(item);
    }

    public boolean hasChild(UpdatableItemButton item) {
        return children.contains(item);
    }
}
