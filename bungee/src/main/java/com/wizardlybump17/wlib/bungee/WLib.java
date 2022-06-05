package com.wizardlybump17.wlib.bungee;

import com.wizardlybump17.wlib.bungee.config.BungeeConfig;
import com.wizardlybump17.wlib.bungee.config.BungeeConfigHolderFactory;
import com.wizardlybump17.wlib.config.ConfigInfo;
import com.wizardlybump17.wlib.config.Path;
import com.wizardlybump17.wlib.config.registry.ConfigHandlerRegistry;
import com.wizardlybump17.wlib.config.registry.ConfigHolderFactoryRegistry;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

@ConfigInfo(name = "test2.yml", holderType = WLib.class)
public class WLib extends Plugin {

    @Path("test")
    public static String test;

    @Override
    public void onLoad() {
        ConfigHolderFactoryRegistry.getInstance().put(WLib.class, new BungeeConfigHolderFactory(this));
        ConfigHandlerRegistry.getInstance().register(WLib.class);

        BungeeConfig config = BungeeConfig.load("test.yml", this);
        config.saveDefaultConfig();
        getProxy().getPluginManager().registerCommand(this, new Command("test") {
            @Override
            public void execute(CommandSender sender, String[] args) {
                switch (args[0]) {
                    case "reload": {
                        config.reloadConfig();
                        return;
                    }

                    case "set": {
                        config.set(args[1], args[2]);
                        config.saveConfig();
                        return;
                    }

                    case "test2": {
                        ConfigHandlerRegistry.getInstance().reloadAll(WLib.class);
                        sender.sendMessage(TextComponent.fromLegacyText(test));
                        return;
                    }
                }

                Object o = config.get(args[0]);
                sender.sendMessage(TextComponent.fromLegacyText(o == null ? "null" : o.toString()));
            }
        });
    }
}
