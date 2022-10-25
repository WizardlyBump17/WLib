package com.wizardlybump17.wlib.util.bukkit;

import lombok.experimental.UtilityClass;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@UtilityClass
public class BukkitStreamsUtil {

    public static byte[] serialize(Object object) {
        try (
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(stream)
        ) {
            bukkitStream.writeObject(object);
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static Object deserialize(byte[] bytes) {
        try (BukkitObjectInputStream stream = new BukkitObjectInputStream(new ByteArrayInputStream(bytes))) {
            return stream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
