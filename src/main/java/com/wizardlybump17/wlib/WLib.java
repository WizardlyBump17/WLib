package com.wizardlybump17.wlib;

import com.wizardlybump17.wlib.listener.InventoryClickListener;
import com.wizardlybump17.wlib.listener.InventoryCloseListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class WLib extends JavaPlugin {

    @Override
    public void onEnable() {
        initEvents();
    }

    private void initEvents() {
        new InventoryClickListener(this);
        new InventoryCloseListener(this);
    }
}
