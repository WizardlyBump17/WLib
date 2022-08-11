package com.wizardlybump17.wlib.adapter;

import com.wizardlybump17.wlib.adapter.util.LegacyStringUtil;
import com.wizardlybump17.wlib.adapter.util.StringUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NMSAdapter {

    public static final String GLOW_TAG = "WLib-glowing";

    public abstract Object nbtToJava(Object nbt);

    public abstract Object javaToNbt(Object java);

    public abstract String getTargetVersion();

    public abstract ItemAdapter getItemAdapter(ItemStack item);

    /**
     * @return the StringUtil instance for the current version
     */
    public StringUtil getStringUtil() {
        return new LegacyStringUtil();
    }
}
