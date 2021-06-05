package com.wizardlybump17.wlib.command;

import lombok.Data;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Data
public class RegisteredCommand {

    private static final Pattern REQUIRED_ARGS = Pattern.compile("<[a-zA-Z0-9]+>");

    private final Command command;
    private final CommandExecutor executor;
    private final Method method;

    /*
     * do a system that automatically do it for us. This system will not runt this entire method, it will only parse the args directly to what we expect
     *
     * Blabla#getParams(String[] args);
     * Blabla#setupCommandParams(this);
     */
    public List<Object> getParams(String[] args) {
        List<Object> params = new ArrayList<>();

        String[] commandArgs = command.execution().split(" ");
        List<String> commandArgsList = Arrays.asList(commandArgs);
        List<String> argsList = Arrays.asList(args);

        Class<?>[] paramTypes = method.getParameterTypes();

        int currentArrayIndex = 0, paramIndex = 1;
        if (commandArgsList.stream().anyMatch(arg -> REQUIRED_ARGS.matcher(arg).matches())) {
            for (String commandArg : commandArgs) {
                if (commandArgs.length > args.length)
                    return null;
                if (REQUIRED_ARGS.matcher(commandArg).matches()) {
                    if (paramTypes[paramIndex].equals(String.class)) {
                        params.add(args[currentArrayIndex++]);
                        paramIndex++;
                        continue;
                    }
                    if (paramTypes[paramIndex].equals(String[].class)) {
                        params.add(Arrays.copyOfRange(args, currentArrayIndex, args.length));
                        return params;
                    }
                    continue;
                }
                if (!commandArg.equalsIgnoreCase(args[currentArrayIndex++]))
                    return null;
            }
            return params;
        }

        return commandArgsList.toString().equalsIgnoreCase(argsList.toString()) ? new ArrayList<>() : null;
    }

    public void fire(CommandSender sender, String[] args) {
        List<Object> params = getParams(args);
        if (params != null) {
            params.add(0, sender);
            executor.execute(sender, args, params.toArray());
        }
    }
}
