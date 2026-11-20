package com.wizardlybump17.wlib.adapter.v1_21_R3;

import com.wizardlybump17.wlib.adapter.Adapter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface BaseAdapter extends Adapter {

    @Override
    default @NotNull Set<String> getMinecraftVersions() {
        return Set.of("1.21.4");
    }
}
