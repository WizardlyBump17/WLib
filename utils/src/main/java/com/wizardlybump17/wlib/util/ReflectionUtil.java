package com.wizardlybump17.wlib.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@UtilityClass
public class ReflectionUtil {

    public static final @NonNull Logger LOGGER = Logger.getLogger(ReflectionUtil.class.getName());

    public static Field getField(@NonNull String name, @NonNull Class<?> clazz) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getFieldValue(@NonNull Field field, @Nullable Object caller) {
        try {
            field.setAccessible(true);
            T result = (T) field.get(caller);
            field.setAccessible(false);
            return result;
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Could not get value of field " + field.getName() + " in class " + field.getDeclaringClass().getName(), e);
            field.setAccessible(false);
            return null;
        }
    }

    public static void setFieldValue(@NonNull Field field, @Nullable Object caller, @Nullable Object value) {
        try {
            field.setAccessible(true);
            field.set(caller, value);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Could not set value of field " + field.getName() + " in class " + field.getDeclaringClass().getName(), e);
            field.setAccessible(false);
        }
    }

    public static Method getMethod(@NonNull String name, @NonNull Class<?> clazz, @NonNull Class<?>... parameters) {
        try {
            return clazz.getDeclaredMethod(name, parameters);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T invokeMethod(@Nullable Method method, @Nullable Object caller, @NonNull Object @Nullable ... parameters) {
        if (method == null)
            return null;

        try {
            method.setAccessible(true);
            T result = (T) method.invoke(caller, parameters);
            method.setAccessible(false);
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not invoke method " + method.getName() + " in class " + method.getDeclaringClass().getName(), e);
            method.setAccessible(false);
            return null;
        }
    }

    public static <T> @Nullable Constructor<T> getConstructor(@NonNull Class<T> clazz, @NonNull Class<?>... parameters) {
        try {
            return clazz.getDeclaredConstructor(parameters);
        } catch (NoSuchMethodException e) {
            LOGGER.log(Level.SEVERE, "Error while fetching the " + Arrays.stream(parameters).map(Class::getName).toList() + " constructor in the " + clazz.getName() + " class.");
            return null;
        }
    }

    public static <T> @NonNull T invokeConstructor(@NonNull Constructor<T> constructor, Object... parameters) {
        try {
            constructor.setAccessible(true);
            T value = constructor.newInstance(parameters);
            constructor.setAccessible(false);
            return value;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Error while creating a new " + constructor.getDeclaringClass().getName() + " instance with the parameters " + Arrays.toString(parameters));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(@NotNull String name) {
        try {
            return (Class<T>) Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
