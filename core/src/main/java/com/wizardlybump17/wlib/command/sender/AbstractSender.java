package com.wizardlybump17.wlib.command.sender;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractSender<S extends org.bukkit.command.CommandSender> implements CommandSender<S> {

    private final S handle;

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(message);
    }

    @Override
    public void sendMessage(String... message) {
        handle.sendMessage(String.join("\n", message));
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return handle.hasPermission(permission);
    }

    @Override
    @NonNull
    public GenericSender toGeneric() {
        return new GenericSender(handle);
    }
}
