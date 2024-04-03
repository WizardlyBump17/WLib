package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.adapter.PotionEffectTypeAdapter;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class PotionEffectTypeReader extends ArgsReader<PotionEffectType> {

    @Override
    public @Nullable Class<PotionEffectType> getType() {
        return PotionEffectType.class;
    }

    @Override
    public PotionEffectType read(String string) {
        return PotionEffectTypeAdapter.getInstance().getEffectType(string);
    }
}
