package com.wizardlybump17.wlib.inventory;

import com.wizardlybump17.wlib.inventory.holder.UpdatableHolder;
import lombok.Getter;

@Getter
public class UpdatableInventory extends CustomInventory {

    public UpdatableInventory(String title, int size, int updateTime) {
        super(title, size, new UpdatableHolder(null, updateTime));
        owner.setInventory(this);
    }

    public UpdatableInventory(String title, int size, int updateTime, int page) {
        super(title, size, new UpdatableHolder(null, updateTime, page));
        owner.setInventory(this);
    }
}
