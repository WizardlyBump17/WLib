package com.wizardlybump17.wlib.adapter;

public final class NMSAdapterRegister {

    private static NMSAdapterRegister instance;

    private NMSAdapter serverAdapter;

    public void registerAdapter(NMSAdapter adapter) {
        System.out.println("kappa " + adapter.getClass().getName());
        serverAdapter = adapter;
    }

    public NMSAdapter current() {
        return serverAdapter;
    }

    public static NMSAdapterRegister getInstance() {
        return instance == null ? instance = new NMSAdapterRegister() : instance;
    }
}
