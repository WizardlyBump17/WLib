package com.wizardlybump17.wlib.config.handler;

import com.wizardlybump17.wlib.config.Configuration;
import com.wizardlybump17.wlib.config.Path;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Data
@AllArgsConstructor
public class ConfigHandler {

    private final Class<?> clazz;
    private final Configuration<?> config;
    private final Class<?> holder;
    private boolean saveDefault;

    public void reload() {
        config.reloadConfig();
        loadFields();
    }

    public void save() {
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Path.class) || !Modifier.isStatic(field.getModifiers()))
                continue;

            Path path = field.getAnnotation(Path.class);
            String configPath = path.value();
            try {
                if (!config.isSet(configPath))
                    config.set(configPath, field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        config.saveConfig();
    }

    public void loadFields() {
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Path.class) || !Modifier.isStatic(field.getModifiers()))
                continue;

            Path path = field.getAnnotation(Path.class);
            String configPath = path.value();
            try {
                field.set(null, config.get(configPath, field.getType(), path));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
