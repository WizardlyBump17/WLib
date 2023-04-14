package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialArgumentCompleter implements ArgumentCompleter {

    @Override
    public List<String> complete(CommandSender<?> sender, String[] args) {
        Material[] values = Material.values();
        List<String> materials = new ArrayList<>(values.length);
        for (Material material : values)
            materials.add(material.name());
        return materials;
    }
}
