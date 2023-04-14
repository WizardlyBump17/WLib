package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;

import java.util.List;

public interface ArgumentCompleter {

    List<String> complete(CommandSender<?> sender, String[] args);
}
