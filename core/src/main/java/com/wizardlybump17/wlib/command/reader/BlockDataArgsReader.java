package com.wizardlybump17.wlib.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

public class BlockDataArgsReader extends ArgsReader<BlockData> {

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
}
