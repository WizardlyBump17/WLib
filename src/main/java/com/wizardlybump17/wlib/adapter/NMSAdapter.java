package com.wizardlybump17.wlib.adapter;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public abstract class NMSAdapter {

    public abstract Object nbtToJava(Object nbt);
    public abstract Object javaToNbt(Object java);
    public abstract String getTargetVersion();
    public abstract ItemAdapter getItemAdapter(ItemStack item);
}
