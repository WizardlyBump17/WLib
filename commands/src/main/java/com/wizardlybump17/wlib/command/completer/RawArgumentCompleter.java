package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.args.ArgsNode;

import java.util.List;

public class RawArgumentCompleter implements ArgumentCompleter {

    private final ArgsNode node;

    public RawArgumentCompleter(ArgsNode node) {
        this.node = node;
    }

    @Override
    public List<String> complete(CommandSender<?> sender, String[] args) {
        return List.of(node.getName());
    }
}
