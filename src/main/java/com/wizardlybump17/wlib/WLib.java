package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.command.TestCommand;
import com.wizardlybump17.wlib.listener.InventoryClickListener;
import org.bukkit.plugin.java.JavaPlugin;

public class WLib extends JavaPlugin {

    @Override
    public void onEnable() {
        initEvents();
        initCommands();
    }

    private void initCommands() {
        new TestCommand(this);
    }

    private void initEvents() {
        new InventoryClickListener(this);
    }
}
