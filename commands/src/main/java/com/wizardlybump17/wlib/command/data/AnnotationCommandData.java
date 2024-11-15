package com.wizardlybump17.wlib.command.data;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnnotationCommandData extends CommandData {

    private final @NotNull Command annotation;
    private final @NotNull Object object;
    private final @NotNull Map<String, Field> fieldCache = new HashMap<>();

    public AnnotationCommandData(@NotNull Command annotation, @NotNull Object object) {
        this.annotation = annotation;
        this.object = object;
    }

    @Override
    public @NotNull String getExecution() {
        return annotation.execution();
    }

    @Override
    public @Nullable String getPermission() {
        String permission = annotation.permission();
        return permission.isBlank() ? super.getPermission() : permission;
    }

    @Override
    public @Nullable String getPermissionMessage() {
        return getMessage(annotation.permissionMessage(), annotation.permissionMessageIsField(), super.getPermissionMessage());
    }

    @Override
    public int getPriority() {
        int priority = annotation.priority();
        return priority == -1 ? super.getPriority() : priority;
    }

    @Override
    public @Nullable String getDescription() {
        String description = annotation.description();
        return description.isBlank() ? super.getDescription() : description;
    }

    @Override
    public @Nullable String getInvalidSenderMessage() {
        return getMessage(annotation.invalidSenderMessage(), annotation.invalidSenderMessageIsField(), super.getInvalidSenderMessage());
    }

    protected @Nullable String getMessage(@NotNull String message, boolean isField, @Nullable String defaultMessage) {
        if (isField) {
            return ReflectionUtil.getFieldValue(
                    fieldCache.computeIfAbsent(
                            message,
                            $ -> ReflectionUtil.getField(message, object.getClass())
                    ),
                    object
            );
        }
        return message.isBlank() ? defaultMessage : message;
    }

    public @NotNull Command getAnnotation() {
        return annotation;
    }

    @Override
    public @NotNull Class<?> getSenderType() {
        return annotation.senderType();
    }

    @Override
    public boolean equals(Object object1) {
        if (object1 == null || getClass() != object1.getClass())
            return false;
        AnnotationCommandData that = (AnnotationCommandData) object1;
        return Objects.equals(annotation, that.annotation) && Objects.equals(object, that.object) && Objects.equals(fieldCache, that.fieldCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation, object, fieldCache);
    }

    @Override
    public String toString() {
        return "AnnotationCommandData{" +
                "annotation=" + annotation +
                ", object=" + object +
                ", fieldCache=" + fieldCache +
                '}';
    }
}
