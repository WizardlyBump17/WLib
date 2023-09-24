package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import lombok.NonNull;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class MaterialReader extends ArgsReader<Material> {

    public static final List<String> SUGGESTION = Arrays.stream(Material.values()).map(Material::name).toList();

    @Override
    public Class<Material> getType() {
        return Material.class;
    }

    @Override
    public Material read(String string) {
        return Material.matchMaterial(string);
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTION;
    }
}
