package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.args.ArgsNode;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.args.ArgsReaderType;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
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
        int index = 1; //skipping the first type because of the CommandSender
        for (String commandArg : commandArgs) {
            if (!requiredArgs(commandArg)) {
                nodes.add(new ArgsNode(
                        commandArg,
                        false,
                        null,
                        null,
                        false
                ));
                continue;
            }

            Description description = parameters[index].getAnnotation(Description.class);

            ArgsReaderType argsReaderType = parameters[index].getAnnotation(ArgsReaderType.class);
            if (argsReaderType == null && Argument.class.isAssignableFrom(types[index]))
                throw new IllegalArgumentException("the \"" + commandArg + "\" argument requires the " + ArgsReaderType.class.getName() + " annotation");

            ArgsReader<?> reader;
            if (argsReaderType == null)
                reader = ArgsReaderRegistry.INSTANCE.getReader(types[index]);
            else
                reader = ArgsReaderRegistry.INSTANCE.get(argsReaderType.value());
            if (reader == null)
                throw new IllegalArgumentException("no reader found for " + types[index].getName());

            nodes.add(new ArgsNode(
                    trim(commandArg),
                    true,
                    reader,
                    description == null ? null : description.value(),
                    Argument.class.isAssignableFrom(types[index])
            ));

            index++;
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
        StringBuilder builder = new StringBuilder();
        boolean inArray = false;
        for (String s : input.split(" ")) {
            if (s.startsWith("\"") && s.endsWith("\"") && !s.endsWith("\\\"")) { //"string" | single word
                target.add(s.substring(1, s.length() - 1));
                continue;
            }

            if (s.startsWith("\"")) { //"string | it is starting the array
                builder.append(s.substring(1).replace("\\\"", "\"")).append(" ");
                inArray = true;
                continue;
            }

            if (s.endsWith("\"") && !s.endsWith("\\\"")) { //string" | it is ending the array
                builder.append(s.replace("\\\"", "\""), 0, s.length() - 1);
                target.add(builder.toString());
                builder = new StringBuilder();
                inArray = false;
                continue;
            }

            if (!builder.isEmpty()) { //string | it is in the array
                builder.append(s.replace("\\\"", "\"")).append(" ");
                continue;
            }

            target.add(s.replace("\\\"", "\"")); //string | it is not in the array
        }

        if (inArray)
            throw new ArgsReaderException("Invalid array");
    }

    public CommandResult execute(CommandSender<?> sender, String string) {
        try {
            Optional<Object[]> parse = parse(string);
            if (parse.isEmpty())
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
        } catch (NoSuchMethodException ignored) {
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