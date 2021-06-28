package com.wizardlybump17.wlib.adapter.v1_8_R3;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class EntityAdapter extends com.wizardlybump17.wlib.adapter.EntityAdapter {

    public EntityAdapter(Entity entity) {
        super(entity);
    }

    @Override
    public ItemStack getItemInMainHand() {
        if (!(entity instanceof HumanEntity))
            return null;
        return ((HumanEntity) entity).getItemInHand();
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getItemInMainHand();
    }
}
