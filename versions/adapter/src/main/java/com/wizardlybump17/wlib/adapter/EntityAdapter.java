package com.wizardlybump17.wlib.adapter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public abstract class EntityAdapter {

    protected final Entity entity;

    public abstract ItemStack getItemInMainHand();
    public abstract ItemStack getItemInOffHand();
}
