package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import lombok.Getter;

@Getter
public class UpdatableInventory extends CustomInventory {

    public UpdatableInventory(String title, int size, int updateTime) {
        super(title, size, new UpdatableHolder(null, updateTime));
        owner.setInventory(this);
    }
}
