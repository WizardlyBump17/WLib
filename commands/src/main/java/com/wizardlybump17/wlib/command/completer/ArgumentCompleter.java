package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public interface ArgumentCompleter {

    @NonNull
    List<String> complete(CommandSender<?> sender, String[] args);
}
