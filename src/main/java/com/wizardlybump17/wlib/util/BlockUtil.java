package com.wizardlybump17.wlib.util;

import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class BlockUtil {

    public static Skull setSkullTexture(Skull skull, String base64) {
        try {
            Object gameProfile = Class.forName("com.mojang.authlib.GameProfile")
                    .getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), null);
            Object properties = gameProfile.getClass().getDeclaredMethod("getProperties")
                    .invoke(gameProfile);

            properties.getClass().getSuperclass().getDeclaredMethod(
                    "put",
                    Object.class,
                    Object.class).invoke(
                    properties,
                    "textures",
                    Class.forName("com.mojang.authlib.properties.Property")
                            .getConstructor(String.class, String.class).newInstance(
                            "textures",
                            base64));

            Field profileField = skull.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skull, gameProfile);

            skull.update(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return skull;
    }
}
