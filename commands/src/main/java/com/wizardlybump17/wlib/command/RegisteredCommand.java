package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.args.ArgsNode;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.args.ArgsReaderType;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import com.wizardlybump17.wlib.object.Pair;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Getter
public class RegisteredCommand implements Comparable<RegisteredCommand> {

    private final Command command;
    private final Object object;
    private final Method method;
    private final List<ArgsNode> nodes = new ArrayList<>();
    private final @NonNull MethodHandle methodHandle;

    public RegisteredCommand(Command command, Object object, Method method, @NonNull MethodHandle methodHandle) {
        this.command = command;
        this.object = object;
        this.method = method;
        this.methodHandle = methodHandle;
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

    public Optional<List<Pair<ArgsNode, Object>>> parse(String input, boolean autoComplete) throws ArgsReaderException {
        List<String> toParse = new ArrayList<>();
        checkArrays(input, toParse, autoComplete);

        List<Pair<ArgsNode, Object>> result = new ArrayList<>(nodes.size());

        if (parseRequiredOnly(result, toParse, autoComplete))
            return Optional.of(result);

        return Optional.empty();
    }

    private boolean parseRequiredOnly(List<Pair<ArgsNode, Object>> target, List<String> strings, boolean autoComplete) throws ArgsReaderException {
        if (autoComplete && nodes.size() > strings.size())
            return false;

        for (int i = 0; i < nodes.size() && i < strings.size(); i++) {
            ArgsNode node = nodes.get(i);
            if (!node.isUserInput()) {
                if (!node.getName().equalsIgnoreCase(strings.get(i)))
                    return false;

                continue;
            }

            Object parse = node.parse(strings.get(i));
            if (parse == ArgsNode.EMPTY)
                continue;

            target.add(new Pair<>(node, parse));
        }

        return true;
    }

    private void checkArrays(String input, List<String> target, boolean autoComplete) throws ArgsReaderException {
        StringBuilder builder = new StringBuilder();
        boolean inArray = false;
        for (String s : input.split(" ")) {
            if (s.startsWith("\"") && s.endsWith("\"") && !s.endsWith("\\\"") && s.length() != 1) { //"string" | single word
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

        if (inArray && !autoComplete)
            throw new ArgsReaderException("Invalid array");
    }

    public CommandResult execute(CommandSender<?> sender, String string) {
        try {
            Optional<List<Pair<ArgsNode, Object>>> parse = parse(string, true);
            if (parse.isEmpty())
                return CommandResult.ARGS_FAIL;

            if (!getSenderType().isInstance(sender) && !isSenderGeneric())
                return CommandResult.INVALID_SENDER;

            Object[] objects = parse.get().stream().map(Pair::getSecond).toArray();
            return executeInternal(sender, objects);
        } catch (ArgsReaderException e) {
            return CommandResult.EXCEPTION;
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

            list.add(0, object);
            methodHandle.invokeWithArguments(list);
            return CommandResult.SUCCESS;
        } catch (Throwable e) {
            e.printStackTrace();
            return CommandResult.METHOD_FAIL;
        }
    }

    @NonNull
    public List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String string) {
        if (nodes.size() == 1)
            return Collections.emptyList();

        if (!getSenderType().isInstance(sender) && !isSenderGeneric())
            return Collections.emptyList();

        try {
            Optional<List<Pair<ArgsNode, Object>>> parse = parse(string, false);
            if (parse.isEmpty())
                return Collections.emptyList();

            String[] split = string.split(" ");
            String lastString = split[split.length - 1];

            List<Pair<ArgsNode, Object>> pairs = parse.get();

            if (pairs.isEmpty()) {
                ArgsNode secondNode = nodes.get(1);
                ArgsReader<?> secondNodeReader = secondNode.getReader();
                return secondNodeReader == null ? List.of(secondNode.getName()) : secondNodeReader.autoComplete(sender, lastString);
            }

            Pair<ArgsNode, Object> lastPair = pairs.get(pairs.size() - 1);

            ArgsNode node = lastPair.getFirst();
            ArgsReader<?> reader = node.getReader();

            return reader == null ? List.of(node.getName()) : reader.autoComplete(sender, lastString);
        } catch (ArgsReaderException e) {
            e.printStackTrace();
            return Collections.emptyList();
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