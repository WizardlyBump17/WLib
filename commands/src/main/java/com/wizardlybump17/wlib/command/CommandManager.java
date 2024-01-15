package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.holder.CommandHolder;
import com.wizardlybump17.wlib.util.ReflectionUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CommandManager {

    private final List<RegisteredCommand> commands = new ArrayList<>();
    protected final CommandHolder<?> holder;
    private final @NonNull Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>();

    public void registerCommands(Object... objects) {
        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Command.class) || method.getParameterCount() == 0 || !CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))
                    continue;

                RegisteredCommand command = new RegisteredCommand(
                        method.getAnnotation(Command.class),
                        object,
                        method
                );

                commands.add(command);
                com.wizardlybump17.wlib.command.holder.Command holderCommand = holder.getCommand(command.getName());
                if (holderCommand != null)
                    holderCommand.setExecutor(holderCommand.getDefaultExecutor(this, command.getName()));
            }
        }

        commands.sort(null);
    }

    public void unregisterCommands() {
        commands.clear();
    }

    public void execute(CommandSender<?> sender, String string) {
        if (commands.isEmpty())
            return;

        for (RegisteredCommand registeredCommand : commands) {
            Command command = registeredCommand.getCommand();
            CommandResult result = registeredCommand.execute(sender, string);

            switch (result) {
                case PERMISSION_FAIL -> {
                    handleMessage(registeredCommand, sender, command.permissionMessage(), command.permissionMessageIsField());
                    return;
                }

                case INVALID_SENDER -> {
                    handleMessage(registeredCommand, sender, command.invalidSenderMessage(), command.invalidSenderMessageIsField());
                    return;
                }

                case SUCCESS -> {
                    return;
                }
            }
        }
    }

    protected void handleMessage(@NonNull RegisteredCommand registeredCommand, @NonNull CommandSender<?> sender, @NonNull String message, boolean isField) {
        if (!isField) {
            if (!message.isEmpty())
                sender.sendMessage(message);
            return;
        }

        String fieldMessage = getFieldMessage(registeredCommand, message);
        if (fieldMessage != null)
            sender.sendMessage(fieldMessage);
    }

    protected @Nullable String getFieldMessage(@NonNull RegisteredCommand registeredCommand, @NonNull String fieldName) {
        Map<String, Field> fields = fieldCache.computeIfAbsent(registeredCommand.getObject().getClass(), clazz -> {
            Map<String, Field> map = new HashMap<>();
            for (Field field : clazz.getDeclaredFields())
                map.put(field.getName(), field);
            return map;
        });

        Field field = fields.get(fieldName);
        if (field == null)
            return null;

        Object fieldValue = ReflectionUtil.getFieldValue(field, registeredCommand.getObject());
        return fieldValue == null ? null : fieldValue.toString();
    }

    @NonNull
    public List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String string) {
        List<String> result = new ArrayList<>();
        for (RegisteredCommand command : commands)
            result.addAll(command.autoComplete(sender, string));
        return result;
    }

    public List<RegisteredCommand> getCommand(String name) {
        List<RegisteredCommand> commands = new ArrayList<>(this.commands.size());
        for (RegisteredCommand command : this.commands)
            if (command.getName().equalsIgnoreCase(name))
                commands.add(command);
        return commands;
    }

    public List<RegisteredCommand> getCommands(Object object) {
        List<RegisteredCommand> commands = new ArrayList<>(this.commands.size());
        for (RegisteredCommand command : this.commands)
            if (command.getObject() == object)
                commands.add(command);
        return commands;
    }
}