package com.wizardlybump17.wlib.config.handler;

import com.wizardlybump17.wlib.config.Configuration;
import com.wizardlybump17.wlib.config.Path;
import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Data
public class ConfigHandler {

    private final Class<?> clazz;
    private final Configuration config;

    public void reload() {
        config.reloadConfig();

        for (Field field : clazz.getDeclaredFields()) {
            reloadStaticField(field);
            //TODO reload non-static fields
        }
    }

    private boolean reloadStaticField(Field field) {
        if (!field.isAnnotationPresent(Path.class) || !Modifier.isStatic(field.getModifiers()))
            return false;

        Path path = field.getAnnotation(Path.class);
        if (path.immutable())
            return true;

        String configPath = path.value();
        String defaultValue = path.defaultValue();
        Object object = config.get(configPath, config.get(defaultValue));

        if (object == null)
            return true;

        if (!ReflectionUtil.isAssignableFrom(field.getType(), object.getClass()))
            return true;

        ReflectionUtil.set(field, object);
        return true;
    }

    public void save() {
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Path.class) || !Modifier.isStatic(field.getModifiers()))
                continue;

            Path path = field.getAnnotation(Path.class);
            String configPath = path.value();
            try {
                config.set(configPath, field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
