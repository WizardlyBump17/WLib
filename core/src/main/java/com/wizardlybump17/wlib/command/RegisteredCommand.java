package com.wizardlybump17.wlib.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class RegisteredCommand {

    public static final Pattern REQUIRED = Pattern.compile(" ?<[a-zA-Z0-9_]+> ?");
    public static final Pattern OPTIONAL = Pattern.compile(" ?\\[[a-zA-Z0-9_]+] ?");

    private final Command command;
    private final Method method;
    private final Object object;
    private final CommandExecutor executor;

    private List<String> groupsName;
    private List<Arg> groups;
    private Pattern pattern;

    private void prepareGroupsList() {
        Matcher requiredMatcher = REQUIRED.matcher(command.execution());
        Matcher optionalMatcher = OPTIONAL.matcher(command.execution());

        Map<Integer, String> indexes = new HashMap<>();
        while (requiredMatcher.find())
            indexes.put(requiredMatcher.start(), command.execution().substring(requiredMatcher.start(), requiredMatcher.end()));
        while (optionalMatcher.find())
            indexes.put(optionalMatcher.start(), command.execution().substring(optionalMatcher.start(), optionalMatcher.end()));

        ArrayList<Integer> ints = new ArrayList<>(indexes.keySet());
        Collections.sort(ints);

        groupsName = new ArrayList<>(ints.size());
        for (int i : ints)
            groupsName.add(indexes.get(i).trim());
    }

    protected void preparePattern() {
        prepareGroupsList();
        groups = new ArrayList<>(groupsName.size());
        String currentCommand = command.execution();

        Class<?>[] types = method.getParameterTypes();
        for (int i = 1; i < method.getParameterCount(); i++) {
            String groupName = groupsName.get(i - 1);
            currentCommand = addGroup(types[i], groupName, currentCommand);
        }

        pattern = Pattern.compile(currentCommand.trim() + (command.acceptAny() ? ".*?" : ""));
    }

    String addGroup(Class<?> type, String groupName, String currentCommand) {
        if (type.equals(String.class)) {
            if (groupName.matches(REQUIRED.pattern()))
                currentCommand = currentCommand.replaceFirst(REQUIRED.pattern(), " ?(\\\\S+) ?");
            if (groupName.matches(OPTIONAL.pattern()))
                currentCommand = currentCommand.replaceFirst(OPTIONAL.pattern(), " ?(\\\\S+)? ?");
            groups.add(new Arg(groupName, Arg.Type.STRING));
        }
        if (type.equals(String[].class)) {
            if (groupName.matches(REQUIRED.pattern()))
                currentCommand = currentCommand.replaceFirst(REQUIRED.pattern(), " ?(.*) ?");
            if (groupName.matches(OPTIONAL.pattern()))
                currentCommand = currentCommand.replaceFirst(OPTIONAL.pattern(), " ?(.*)? ?");
            groups.add(new Arg(groupName, Arg.Type.ARRAY));
        }
        return currentCommand;
    }

    public ArgsMap getArgs(String[] args) {
        Map<String, Args> map = new LinkedHashMap<>();
        Matcher matcher = pattern.matcher(String.join(" ", args));
        if (!matcher.matches())
            return null;
        for (int i = 0; i < matcher.groupCount(); i++) {
            String groupName = groupsName.get(i);
            Arg group = groups.get(i);
            if (group.type == Arg.Type.ARRAY)
                map.put(groupName.toLowerCase(), new Args(matcher.group(i + 1).split(" ")));
            if (group.type == Arg.Type.STRING)
                map.put(groupName.toLowerCase(), new Args(matcher.group(i + 1)));
        }
        return new ArgsMap(map);
    }

    public void execute(ArgsMap map, CommandSender sender) {
        if (map == null)
            return;
        try {
            if (!executor.execute(sender))
                return;
            List<Object> objects = toList(map);
            objects.add(0, sender);
            method.invoke(object, objects.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Object> toList(ArgsMap map) {
        List<Object> list = new ArrayList<>(map.map.size());
        map.map.forEach((name, value) -> list.add(value.object));
        return list;
    }

    @RequiredArgsConstructor
    public static class Args {

        final Object object;

        String asString() {
            if (object instanceof String)
                return object.toString();
            return object == null ? null : String.join(" ", (String[]) object);
        }

        String[] asArray() {
            if (object instanceof String[])
                return (String[]) object;
            return asString() == null ? null : object.toString().split(" ");
        }
    }

    @RequiredArgsConstructor
    public static class ArgsMap {

        final Map<String, Args> map;

        Args get(String name) {
            return map.get(name.toLowerCase());
        }

        String asString(String name) {
            return get(name).asString();
        }

        String[] asArray(String name) {
            return get(name).asArray();
        }
    }

    @RequiredArgsConstructor
    public static class Arg {

        final String name;
        final Type type;

        enum Type {
            STRING, ARRAY
        }
    }
}
