package com.wizardlybump17.wlib.reflection;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public final class ReflectionAdapterRegister {

    private static ReflectionAdapterRegister instance;

    private final Map<String, ReflectionAdapter> adapters = new HashMap<>();
    private ReflectionAdapter serverAdapter;

    public void registerAdapter(ReflectionAdapter adapter) {
        adapters.put(adapter.getTargetVersion(), adapter);
    }

    public boolean isAdapterRegistered(String targetVersion) {
        return adapters.containsKey(targetVersion);
    }

    public ReflectionAdapter getAdapter(String targetVersion) {
        return adapters.get(targetVersion);
    }

    public ReflectionAdapter getServerAdapter() {
        if (serverAdapter != null)
            return serverAdapter;
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        ReflectionAdapter adapter = getAdapter(serverVersion);
        if (adapter == null)
            throw new NullPointerException("this server version has no ReflectionAdapter available from WLib");
        return serverAdapter = adapter;
    }

    public static ReflectionAdapterRegister getInstance() {
        return instance == null ? instance = new ReflectionAdapterRegister() : instance;
    }
}
