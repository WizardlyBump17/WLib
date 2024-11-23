package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Arrays;
import java.util.List;

public class BlockDataArgsReader extends ArgsReader<BlockData> {

    public static final List<String> SUGGESTIONS = Arrays.stream(Material.values())
            .filter(Material::isBlock)
            .map(material -> material.createBlockData().getAsString(true))
            .toList();

    @Override
    public Class<BlockData> getType() {
        return BlockData.class;
    }

    @Override
    public BlockData read(String string) throws ArgsReaderException {
        try {
            return Bukkit.createBlockData(string);
        } catch (IllegalArgumentException e) {
            throw new ArgsReaderException(e);
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
