package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.args.ArgsNode;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisteredCommand implements Comparable<RegisteredCommand> {

    @Getter
    private final Command command;
    @Getter
    private final Object object;
    @Getter
    private final Method method;
    @Getter
    private final List<ArgsNode> nodes = new ArrayList<>();
    private Boolean senderGeneric;

    public RegisteredCommand(Command command, Object object, Method method) {
        this.command = command;
        this.object = object;
        this.method = method;
        prepareNodes();
    }

    private void prepareNodes() {
        final String[] commandArgs = command.execution().split(" ");

        final Class<?>[] types = method.getParameterTypes();
        int currentIndex = 1; //skipping the first type because of the CommandSender
        for (String commandArg : commandArgs) {
            if (requiredArgs(commandArg) || optionalArgs(commandArg))
                nodes.add(new ArgsNode(
                        trim(commandArg),
                        requiredArgs(commandArg),
                        true,
                        ArgsReaderRegistry.INSTANCE.get(types[currentIndex++])
                ));
            else
                nodes.add(new ArgsNode(commandArg, true, false, null));
        }
    }

    @Override
    public int compareTo(@NotNull RegisteredCommand o) {
        return Integer.compare(o.command.execution().split(" ").length, command.execution().split(" ").length);
    }

    public Object[] parse(String string) throws ArgsReaderException {
        List<Object> list = new ArrayList<>();
        final String[] args = string.split(" ");

        for (ArgsNode node : nodes)
            if (node.isUserInput())
                if (args.length != nodes.size())
                    return null;

        boolean plainCommand = true;
        for (ArgsNode node : nodes)
            if (node.isUserInput()) {
                plainCommand = false;
                break;
            }

        if (plainCommand) {
            if (string.toLowerCase().startsWith(command.execution().toLowerCase()))
                return new Object[0];
            return null;
        }


        List<Object> arrayElements = new ArrayList<>();
        ArgsNode lastNode = null;
        for (int i = 0; i < args.length; i++) {
            try {
                String arg = args[i];
                ArgsNode node = arrayElements.isEmpty() ? nodes.get(i) : lastNode;

                final ArgsReader<?> reader = node.getReader();
                if (reader == null) {
                    if (arg.equalsIgnoreCase(node.getName()))
                        continue;
                    throw new ArgsReaderException("expected " + node.getName() + " but got " + arg);
                }
                if (reader.getType().isArray()) {
                    arrayElements.add(reader.read(arg));
                    lastNode = node;
                } else {
                    if (!arrayElements.isEmpty()) {
                        fix(lastNode, arrayElements, list);
                        arrayElements.clear();
                    }
                    list.add(reader.read(arg));
                }
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }

        if (!arrayElements.isEmpty())
            fix(lastNode, arrayElements, list);

        return list.toArray();
    }

    private static void fix(ArgsNode node, List<Object> list, List<Object> target) {
        final ArgsReader<?> reader = node.getReader();
        if (reader.isArray())
            target.add(reader.cast(list.toArray()));
    }

    public void execute(CommandSender<?> sender, String string) throws ArgsReaderException {
        Object[] objects = parse(string);
        try {
            if (objects == null)
                throw new IllegalArgumentException("invalid args for command " + command.execution());

            if (!command.permission().isEmpty() && !sender.hasPermission(command.permission()))
                return;

            final ArrayList<Object> list = new ArrayList<>(Arrays.asList(objects));
            list.add(0, sender);
            if (isSenderGeneric())
                list.set(0, sender.toGeneric());
            method.invoke(object, list.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Class<? extends CommandSender<?>> getSenderType() {
        return (Class<? extends CommandSender<?>>) method.getParameterTypes()[0];
    }

    public boolean isSenderGeneric() {
        if (senderGeneric == null) {
            try {
                senderGeneric = (Boolean) getSenderType().getDeclaredMethod("isGeneric").invoke(null);
            } catch (NoSuchMethodException e) {
                senderGeneric = false;
            } catch (Exception e) {
                senderGeneric = false;
                e.printStackTrace();
            }
        }
        return senderGeneric;
    }

    private static String trim(String string) {
        return string.substring(1, string.length() - 1);
    }

    private static boolean requiredArgs(String string) {
        return string.startsWith("<") && string.endsWith(">");
    }

    private static boolean optionalArgs(String string) {
        return string.startsWith("[") && string.endsWith("]");
    }
}
