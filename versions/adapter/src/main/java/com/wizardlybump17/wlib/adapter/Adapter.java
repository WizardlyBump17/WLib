package com.wizardlybump17.wlib.adapter;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Adapter {

    @NotNull Set<String> getMinecraftVersions();

    default void checkIsOnRightVersion() {
        String current = Bukkit.getMinecraftVersion();
        Set<String> expected = getMinecraftVersions();
        if (!expected.contains(current))
            throw new IllegalStateException("Expected Minecraft version(s) " + expected + ", but currently running " + current);
    }
}
