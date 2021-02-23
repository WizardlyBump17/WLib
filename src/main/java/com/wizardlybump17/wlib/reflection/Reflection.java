package com.wizardlybump17.wlib.reflection;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    public static void setFieldValue(Object obj, String fieldName, Object newValue) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object... params) {
        try {
            Method method;
            if (obj instanceof Class) method = ((Class<?>) obj).getDeclaredMethod(methodName, paramTypes);
            else method = obj.getClass().getDeclaredMethod(methodName, paramTypes);
            return method.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Object obj, String methodName) {
        return invokeMethod(obj, methodName, new Class[]{});
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

    public static Object newInstance(Class<?> clazz, Class<?>[] constructorTypes, Object... params) {
        try {
            return clazz.getConstructor(constructorTypes).newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object newInstance(Class<?> clazz) {
        return newInstance(clazz, new Class[]{});
    }

    public static Object convertToNBTTag(Object object) {
        try {
            if (getNMSClass("NBTBase").isInstance(object))
                return object;
            if (object instanceof Byte)
                return newInstance(getNMSClass("NBTTagByte"), new Class[]{byte.class}, object);
            if (object instanceof Byte[])
                return newInstance(getNMSClass("NBTTagByteArray"), new Class[]{byte[].class}, object);
            if (object instanceof Short)
                return newInstance(getNMSClass("NBTTagShort"), new Class[]{short.class}, object);
            if (object instanceof Integer)
                return newInstance(getNMSClass("NBTTagInt"), new Class[]{int.class}, object);
            if (object instanceof Integer[])
                return newInstance(getNMSClass("NBTTagIntArray"), new Class[]{int[].class}, object);
            if (object instanceof Long)
                return newInstance(getNMSClass("NBTTagLong"), new Class[]{long.class}, object);
            if (object instanceof Float)
                return newInstance(getNMSClass("NBTTagFloat"), new Class[]{float.class}, object);
            if (object instanceof Double)
                return newInstance(getNMSClass("NBTTagDouble"), new Class[]{double.class}, object);
            if (object instanceof String)
                return newInstance(getNMSClass("NBTTagString"), new Class[]{String.class}, object);
            if (object instanceof List) {
                Object tagList = newInstance(getNMSClass("NBTTagList"));
                List<?> list = (List<?>) object;
                for (Object listValue : list)
                    invokeMethod(
                            tagList,
                            "add",
                            new Class[]{Reflection.getNMSClass("NBTBase")},
                            convertToNBTTag(listValue));
                return tagList;
            }
            if (object instanceof Map) {
                Object tagCompound = newInstance(getNMSClass("NBTTagCompound"));
                Map<?, ?> map = (Map<?, ?>) object;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String key = entry.getKey().toString();
                    Object value = entry.getValue();
                    invokeMethod(
                            tagCompound,
                            "set",
                            new Class[]{String.class, Reflection.getNMSClass("NBTBase")},
                            key, convertToNBTTag(value));
                }
                return tagCompound;
            }
            return newInstance(getNMSClass("NBTTagString"), new Class[]{String.class}, object.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object convertToJavaType(Object nbtTag) {
        try {
            if (!getNMSClass("NBTBase").isInstance(nbtTag)) return nbtTag;
            if (getNMSClass("NBTBase.NBTNumber").isInstance(nbtTag)
                    || getNMSClass("NBTTagString").isInstance(nbtTag)) {
                return getFieldValue(nbtTag, "data");
            }
            if (getNMSClass("NBTTagList").isInstance(nbtTag)) {
                List<Object> result = new ArrayList<>();
                for (Object o : (List<?>) getFieldValue(nbtTag, "list")) result.add(convertToJavaType(o));
                return result;
            }
            if (getNMSClass("NBTTagCompound").isInstance(nbtTag)) {
                Map<String, Object> result = new HashMap<>();
                Map<String, ?> map = (Map<String, ?>) getFieldValue(nbtTag, "map");
                for (Map.Entry<String, ?> entry : map.entrySet()) result.put(entry.getKey(), convertToJavaType(entry.getValue()));
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
