package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class MaterialArgumentCompleter implements ArgumentCompleter {

    public static final List<String> MATERIALS = Arrays.stream(Material.values())
            .map(Enum::name)
            .toList();

    @Override
    public @NonNull List<String> complete(@NonNull CommandSender<?> sender, @NonNull String @NonNull [] args) {
        return MATERIALS;
    }
}
