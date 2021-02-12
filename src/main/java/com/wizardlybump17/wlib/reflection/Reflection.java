package com.wizardlybump17.wlib.reflection;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reflection {

    public static final String SERVER_VERSION;
    private static final Map<String, Class<?>> NMS_CLASSES_CACHE = new HashMap<>();
    private static final Map<String, Class<?>> CRAFT_BUKKIT_CLASSES_CACHE = new HashMap<>();

    static {
        String serverPackage = Bukkit.getServer().getClass().getPackage().getName();
        SERVER_VERSION = serverPackage.substring(serverPackage.lastIndexOf('.') + 1);
    }

    public static Class<?> getNMSClass(String className) {
        try {
            if (NMS_CLASSES_CACHE.containsKey(className)) return NMS_CLASSES_CACHE.get(className);
            Class<?> clazz = Class.forName("net.minecraft.server." + SERVER_VERSION + "." + className);
            NMS_CLASSES_CACHE.put(className, clazz);
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftBukkitClass(String className) {
        try {
            if (CRAFT_BUKKIT_CLASSES_CACHE.containsKey(className)) return CRAFT_BUKKIT_CLASSES_CACHE.get(className);
            Class<?> clazz = Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + "." + className);
            CRAFT_BUKKIT_CLASSES_CACHE.put(className, clazz);
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object convertToNBTTag(Object object) {
        try {
            if (getNMSClass("NBTBase").isInstance(object))
                return object;
            if (object instanceof Byte)
                return getNMSClass("NBTTagByte").getConstructor(byte.class)
                        .newInstance(object);
            if (object instanceof Byte[])
                return getNMSClass("NBTTagByteArray").getConstructor(byte[].class)
                        .newInstance(object);
            if (object instanceof Short)
                return getNMSClass("NBTTagShort").getConstructor(short.class)
                        .newInstance(object);
            if (object instanceof Integer)
                return getNMSClass("NBTTagInt").getConstructor(int.class)
                        .newInstance(object);
            if (object instanceof Integer[])
                return getNMSClass("NBTTagIntArray").getConstructor(int[].class)
                        .newInstance(object);
            if (object instanceof Long)
                return getNMSClass("NBTTagLong").getConstructor(long.class)
                        .newInstance(object);
            if (object instanceof Float)
                return getNMSClass("NBTTagFloat").getConstructor(float.class)
                        .newInstance(object);
            if (object instanceof Double)
                return getNMSClass("NBTTagDouble").getConstructor(double.class)
                        .newInstance(object);
            if (object instanceof String)
                return getNMSClass("NBTTagString").getConstructor(String.class)
                        .newInstance(object);
            if (object instanceof List) {
                Object tagList = getNMSClass("NBTTagList").newInstance();
                List<?> list = (List<?>) object;
                for (Object listValue : list)
                    tagList.getClass().getDeclaredMethod(
                            "add",
                            Reflection.getNMSClass("NBTBase"))
                            .invoke(tagList, convertToNBTTag(listValue));
                return tagList;
            }
            if (object instanceof Map) {
                Object tagCompound = getNMSClass("NBTTagCompound").newInstance();
                Map<?, ?> map = (Map<?, ?>) object;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String key = entry.getKey().toString();
                    Object value = entry.getValue();
                    tagCompound.getClass().getDeclaredMethod(
                            "set",
                            String.class,
                            Reflection.getNMSClass("NBTBase"))
                            .invoke(tagCompound, key, convertToNBTTag(value));
                }
                return tagCompound;
            }
            return getNMSClass("NBTTagString").getConstructor(String.class)
                    .newInstance(object.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
