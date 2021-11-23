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
import java.util.Optional;

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
            if (requiredArgs(commandArg))
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

    public Optional<Object[]> parse(String string) throws ArgsReaderException {
        String[] stringSplit = string.split(" ");

        arrayCheck: { //check for array nodes
            for (ArgsNode node : nodes)
                if (node.getReader() != null && node.getReader().getType().isArray())
                    break arrayCheck;

            if (stringSplit.length < nodes.size())
                return Optional.empty();
        }

        userInputCheck: { //check for nodes that requires the user input
            for (ArgsNode node : nodes)
                if (node.isUserInput())
                    break userInputCheck;

            if (string.toLowerCase().startsWith(command.execution().toLowerCase()))
                return Optional.of(new Object[0]);

            return Optional.empty();
        }

        if (stringSplit.length < nodes.size())
            return Optional.empty();

        List<Object> resultList = new ArrayList<>(nodes.size());

        int nodeIndex = 0;
        List<String> currentArray = new ArrayList<>(stringSplit.length);
        boolean inArray = false;
        for (String arg : stringSplit) {
            if (nodeIndex == nodes.size())
                break;

            ArgsNode node = nodes.get(nodeIndex);
            ArgsReader<?> reader = node.getReader();
            if (!node.isUserInput() && !node.getName().equalsIgnoreCase(arg))
                return Optional.empty();

            nodeIndex++;

            if (!node.isUserInput())
                continue;

            if (inArray) { //we are in an array
                if (arg.endsWith("\"") && !arg.endsWith("\\\"")) { //end of array
                    inArray = false;
                    currentArray.add(arg);

                    Object read = reader.read(String.join(" ", currentArray));
                    resultList.add(read);
                    currentArray.clear();
                    continue;
                }

                currentArray.add(arg); //continue the array
                nodeIndex--;
                continue;
            }

            if (reader.isArray() && arg.startsWith("\"")) { //start of an array
                if (arg.endsWith("\"") && !arg.endsWith("\\\"")) {
                    resultList.add(reader.read(arg));
                    continue;
                }

                inArray = true;
                currentArray.add(arg);
                nodeIndex--;
                continue;
            }

            resultList.add(reader.read(arg));
        }

        if (inArray)
            throw new ArgsReaderException("last array never finished");

        return Optional.of(resultList.toArray());
    }

    public void execute(CommandSender<?> sender, String string) throws ArgsReaderException {
        Optional<Object[]> parse = parse(string);
        if (!parse.isPresent())
            throw new ArgsReaderException("invalid args for command " + command.execution());

        Object[] objects = parse.get();
        try {
            if (!command.permission().isEmpty() && !sender.hasPermission(command.permission()))
                return;

            final ArrayList<Object> list = new ArrayList<>(Arrays.asList(objects));
            list.add(0, sender);
            if (isSenderGeneric())
                list.set(0, sender.toGeneric());
            method.invoke(object, list.toArray());
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new ArgsReaderException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "RegisteredCommand{" + command.execution() + "}";
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
}
