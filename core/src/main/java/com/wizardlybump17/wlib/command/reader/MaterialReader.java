package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import org.bukkit.Material;

public class MaterialReader extends ArgsReader<Material> {

    @Override
    public Class<Material> getType() {
        return Material.class;
    }

    @Override
    public Material read(String string) {
        return Material.matchMaterial(string);
    }
}
