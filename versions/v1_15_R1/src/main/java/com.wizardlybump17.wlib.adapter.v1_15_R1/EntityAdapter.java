package com.wizardlybump17.wlib.adapter.v1_15_R1;

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
        return ((HumanEntity) entity).getInventory().getItemInMainHand();
    }

    @Override
    public ItemStack getItemInOffHand() {
        if (!(entity instanceof HumanEntity))
            return null;
        return ((HumanEntity) entity).getInventory().getItemInOffHand();
    }
}
