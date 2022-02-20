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
    private final Class<?> holder;

    public void reload() {
        config.reloadConfig();

        for (Field field : clazz.getDeclaredFields())
            reloadStaticField(field);
    }

    private void reloadStaticField(Field field) {
        if (!field.isAnnotationPresent(Path.class) || !Modifier.isStatic(field.getModifiers()))
            return;

        Path path = field.getAnnotation(Path.class);
        if (path.immutable())
            return;

        String configPath = path.value();
        String defaultValue = path.defaultValue();
        Object object = config.get(configPath, config.get(defaultValue), field.getType(), path);

        if (object == null)
            return;

        if (!ReflectionUtil.isAssignableFrom(field.getType(), object.getClass()))
            return;

        ReflectionUtil.set(field, object);
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
