package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.args.ArgsNode;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public class RegisteredCommand implements Comparable<RegisteredCommand> {

    private final Command command;
    private final Object object;
    private final Method method;
    private final List<ArgsNode> nodes = new ArrayList<>();

    public RegisteredCommand(Command command, Object object, Method method) {
        this.command = command;
        this.object = object;
        this.method = method;
        prepareNodes();
    }

    private void prepareNodes() {
        String[] commandArgs = command.execution().split(" ");

        Class<?>[] types = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();
        int currentIndex = 1; //skipping the first type because of the CommandSender
        for (String commandArg : commandArgs) {
            if (requiredArgs(commandArg)) {
                Description annotation = parameters[currentIndex].getAnnotation(Description.class);
                String description = annotation == null ? null : annotation.value();

                nodes.add(new ArgsNode(
                        trim(commandArg),
                        requiredArgs(commandArg),
                        true,
                        ArgsReaderRegistry.INSTANCE.get(types[currentIndex++]),
                        description
                ));
            } else
                nodes.add(new ArgsNode(
                        commandArg,
                        true,
                        false,
                        null,
                        null
                ));
        }
    }

    public String getName() {
        return command.execution().split(" ")[0];
    }

    @Override
    public int compareTo(@NotNull RegisteredCommand o) {
        if (o.command.priority() == -1 && command.priority() == -1) {
            int args = compareArgs(o);

            if (args == 0)
                return compareSize(o);

            return args;
        }

        return Integer.compare(o.command.priority(), command.priority());
    }

    private int compareArgs(RegisteredCommand other) {
        return Integer.compare(other.command.execution().split(" ").length, command.execution().split(" ").length);
    }

    private int compareSize(RegisteredCommand other) {
        return Integer.compare(other.command.execution().length(), command.execution().length());
    }

    public Optional<Object[]> parse(String input) throws ArgsReaderException {
        List<String> toParse = new ArrayList<>();
        checkArrays(input, toParse);

        List<Object> result = new ArrayList<>(nodes.size());

        if (parseRequiredOnly(result, toParse))
            return Optional.of(result.toArray());

        return Optional.empty();
    }

    private boolean parseRequiredOnly(List<Object> target, List<String> strings) throws ArgsReaderException {
        if (nodes.size() > strings.size())
            return false;

        for (int i = 0; i < nodes.size(); i++) {
            ArgsNode node = nodes.get(i);
            if (!node.isUserInput()) {
                if (!node.getName().equalsIgnoreCase(strings.get(i)))
                    return false;

                continue;
            }

            Object parse = node.parse(strings.get(i));
            if (parse == ArgsNode.EMPTY)
                continue;

            target.add(parse);
        }

        return true;
    }

    private void checkArrays(String input, List<String> target) throws ArgsReaderException {
        if (input.indexOf('"') == -1) {
            target.addAll(Arrays.asList(input.split(" ")));
            return;
        }

        int old = 0;
        while (true) {
            int start = indexOf(input, '"', old);
            if (start++ == -1) {
                target.addAll(Arrays.asList(input.substring(old).split(" ")));
                return;
            }

            int end = indexOf(input, '"', start);
            if (end == -1)
                throw new ArgsReaderException("invalid string");

            if (end - 1 >= 0 && input.charAt(end - 1) == '\\')
                continue;

            String substring = input.substring(old, start - 1);
            if (!substring.isEmpty())
                target.addAll(Arrays.asList(substring.split(" ")));

            target.add(input.substring(start, end));
            if (indexOf(input, '"', old = end + 1) != -1)
                continue;

            String s = input.substring(old);
            if (!s.isEmpty())
                target.addAll(Arrays.asList(s.split(" ")));
            return;
        }
    }

    private static int indexOf(String string, char c, int start) {
        if (start >= string.length())
            return -1;

        int i = string.indexOf(c, start);
        if (i == -1)
            return -1;

        if (i - 1 < 0)
            return i;

        if (string.charAt(i - 1) == '\\')
            return indexOf(string, c, start + 1);

        return i;
    }

    public CommandResult execute(CommandSender<?> sender, String string) {
        try {
            Optional<Object[]> parse = parse(string);
            if (!parse.isPresent())
                return CommandResult.ARGS_FAIL;

            if (!getSenderType().isInstance(sender) && !isSenderGeneric())
                return CommandResult.INVALID_SENDER;

            Object[] objects = parse.get();
            return executeInternal(sender, objects);
        } catch (ArgsReaderException e) {
            return CommandResult.ARGS_FAIL;
        }
    }

    private CommandResult executeInternal(CommandSender<?> sender, Object[] objects) {
        try {
            if (!command.permission().isEmpty() && !sender.hasPermission(command.permission()))
                return CommandResult.PERMISSION_FAIL;

            List<Object> list = new ArrayList<>(Arrays.asList(objects));

            list.add(0, isSenderGeneric() ? sender.toGeneric() : sender);

            if (list.size() != method.getParameterCount())
                return CommandResult.ARGS_FAIL;

            method.invoke(object, list.toArray());
            return CommandResult.SUCCESS;
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return CommandResult.METHOD_FAIL;
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
        boolean result = false;
        try {
            result = (Boolean) getSenderType().getDeclaredMethod("isGeneric").invoke(null);
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String trim(String string) {
        return string.substring(1, string.length() - 1);
    }

    private static boolean requiredArgs(String string) {
        return string.startsWith("<") && string.endsWith(">");
    }
}