package com.wizardlybump17.wlib.adapter;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public final class NMSAdapterRegister {

    private static NMSAdapterRegister instance;

    private final Map<String, NMSAdapter> adapters = new HashMap<>();
    private NMSAdapter serverAdapter;

    public void registerAdapter(NMSAdapter adapter) {
        adapters.put(adapter.getTargetVersion(), adapter);
    }

    public boolean isAdapterRegistered(String targetVersion) {
        return adapters.containsKey(targetVersion);
    }

    public NMSAdapter getAdapter(String targetVersion) {
        return adapters.get(targetVersion);
    }

    public NMSAdapter getServerAdapter() {
        if (serverAdapter != null)
            return serverAdapter;
        String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        NMSAdapter adapter = getAdapter(serverVersion);
        if (adapter == null)
            throw new NullPointerException("this server version has no ReflectionAdapter available from WLib");
        return serverAdapter = adapter;
    }

    public static NMSAdapterRegister getInstance() {
        return instance == null ? instance = new NMSAdapterRegister() : instance;
    }
}
