package com.wizardlybump17.wlib.bungee.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@UtilityClass
public class PluginUtil {

    public static void saveResource(Plugin plugin, String name, File file) {
        InputStream stream = plugin.getResourceAsStream(name);
        if (stream == null)
            throw new IllegalStateException("Resource not found: " + name);

        if (file.exists())
            return;

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (OutputStream out = Files.newOutputStream(file.toPath())) {
            byte[] buf = new byte[1024];

            int len;
            while ((len = stream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveResource(Plugin plugin, String name) {
        saveResource(plugin, name, new File(plugin.getDataFolder(), name));
    }
}
