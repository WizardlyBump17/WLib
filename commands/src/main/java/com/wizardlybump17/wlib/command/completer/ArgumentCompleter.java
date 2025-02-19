package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;

import java.util.List;

public interface ArgumentCompleter {

    @NonNull
    List<String> complete(@NonNull CommandSender<?> sender, @NonNull String @NonNull [] args);
}
