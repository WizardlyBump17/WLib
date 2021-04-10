package com.wizardlybump17.wlib.reflection;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public abstract class ReflectionAdapter {

    public abstract Object nbtToJava(Object nbt);
    public abstract Object javaToNbt(Object java);
    public abstract String getTargetVersion();
    public abstract ItemAdapter getItemAdapter(ItemStack item);
}
