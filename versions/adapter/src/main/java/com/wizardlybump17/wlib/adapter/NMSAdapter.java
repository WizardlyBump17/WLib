package com.wizardlybump17.wlib.adapter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NMSAdapter {

    public abstract Object nbtToJava(Object nbt);

    public abstract Object javaToNbt(Object java);

    public abstract String getTargetVersion();

    public abstract ItemAdapter getItemAdapter(ItemStack item);
}
