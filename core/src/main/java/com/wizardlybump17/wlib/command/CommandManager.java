package com.wizardlybump17.wlib.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

@RequiredArgsConstructor
public class CommandManager {

    public static final Comparator<RegisteredCommand> COMMAND_COMPARATOR = Comparator.comparingInt(command -> -command.getCommand().execution().split(" ").length);

    private final JavaPlugin plugin;
    private final Map<String, List<RegisteredCommand>> commands = new HashMap<>();
    
    public void registerCommands(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class))
                continue;

            if (!CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))
                continue;

            Command command = method.getAnnotation(Command.class);
            String commandName = command.execution().split(" ")[0].toLowerCase();
            List<RegisteredCommand> commands = this.commands.getOrDefault(commandName, new ArrayList<>());
            RegisteredCommand registeredCommand = new RegisteredCommand(
                    command,
                    method,
                    object,
                    sender -> {
                        try {
                            if (!method.getParameterTypes()[0].isInstance(sender))
                                return false;

                            if (!command.permission().isEmpty()) {
                                if (!sender.hasPermission(command.permission())) {
                                    if (!command.permissionMessage().isEmpty())
                                        sender.sendMessage(command.permissionMessage().replace('&', '§'));
                                    return false;
                                }
                            }
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    });
            commands.add(registeredCommand);
            registeredCommand.preparePattern();
            if (!this.commands.containsKey(commandName)) {
                PluginCommand pluginCommand = plugin.getCommand(commandName);
                if (pluginCommand == null) {
                    plugin.getLogger().log(Level.WARNING, "Tried to create the command " + commandName + " but it is not in the plugin.yml");
                    continue;
                }
                pluginCommand.setExecutor(new BukkitCommandHandler(this));
                this.commands.put(commandName, commands);
            }
        }

        for (List<RegisteredCommand> commandList : commands.values())
            commandList.sort(COMMAND_COMPARATOR);
    }

    public void execute(CommandSender sender, String commandName, String[] args) {
        for (RegisteredCommand command : commands.get(commandName.toLowerCase())) {
            RegisteredCommand.ArgsMap map = command.getArgs(args);
            if (map != null) {
                command.execute(map, sender);
                return;
            }
        }
    }
}
