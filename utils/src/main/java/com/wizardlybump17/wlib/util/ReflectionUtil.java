package com.wizardlybump17.wlib.util;

import lombok.experimental.UtilityClass;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ReflectionUtil {

    private static final Map<Class<?>, Class<?>> BOXED_CLASSES = new HashMap<>();

    static {
        BOXED_CLASSES.put(byte.class, Byte.class);
        BOXED_CLASSES.put(short.class, Short.class);
        BOXED_CLASSES.put(int.class, Integer.class);
        BOXED_CLASSES.put(long.class, Long.class);
        BOXED_CLASSES.put(float.class, Float.class);
        BOXED_CLASSES.put(double.class, Double.class);
        BOXED_CLASSES.put(boolean.class, Boolean.class);
        BOXED_CLASSES.put(char.class, Character.class);
    }

    /**
     * Checks if the given {@code clazz} is assignable from the {@code other} class.<br>
     * If one class is primitive, it will be boxed and checked against the other class, which can be also boxed.
     * @param clazz the class which will check
     * @param other the class which will be checked against
     * @return if the given {@code clazz} is assignable from the {@code other} class
     */
    public static boolean isAssignableFrom(Class<?> clazz, Class<?> other) {
        if (clazz.isAssignableFrom(other))
            return true;

        if (clazz.isPrimitive()) {
            Class<?> boxed = BOXED_CLASSES.get(clazz);
            return boxed != null && boxed.isAssignableFrom(BOXED_CLASSES.getOrDefault(other, other));
        }

        return false;
    }

    /**
     * @param obj the object to box
     * @return the boxed object class
     */
    public static Class<?> boxed(Object obj) {
        if (!BOXED_CLASSES.containsKey(obj.getClass()))
            return obj.getClass();

        return BOXED_CLASSES.get(obj.getClass());
    }

    /**
     * Sets the value to the given field reflectively.<br>
     * If it fails, it will do nothing (exceptions are just printed to the console)
     * @param field the field to set
     * @param value the value to set
     */
    public static void set(Field field, Object value) {
        if (Modifier.isFinal(field.getModifiers())) {
            setFinalField(field, value);
            return;
        }

        try {
            field.setAccessible(true);
            field.set(null, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
        }
    }

    /**
     * Special method to update a final field
     * @param field the field to update
     * @param value the value to set
     */
    private static void setFinalField(Field field, Object value) {
        if (setFinalFieldJDK11(field, value))
            return;
        setFinalFieldJDK12(field, value);
    }

    /**
     * This will try to set the final field using the modifiers
     * @apiNote This method will only work until JDK 11
     * @param field the field to update
     * @param value the value to set
     * @return if the field was successfully updated
     */
    private static boolean setFinalFieldJDK11(Field field, Object value) {
        try {
            Field modifiersField;
            try {
                modifiersField = Field.class.getDeclaredField("modifiers");
            } catch (NoSuchFieldException e) {
                return false;
            }

            modifiersField.setAccessible(true);

            int modifiers = field.getModifiers();
            modifiersField.setInt(field, modifiers & ~Modifier.FINAL);

            field.set(null, value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * This will try to set the final field using the Unsafe API.
     * @apiNote This method will only work above JDK 12
     * @param field the field to update
     * @param value the value to set
     * @return if the field was successfully updated
     */
    private static boolean setFinalFieldJDK12(Field field, Object value) {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);

            Object base = unsafe.staticFieldBase(field);
            long offset = unsafe.staticFieldOffset(field);
            unsafe.putObject(base, offset, value);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
