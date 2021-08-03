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

    public static final Pattern REQUIRED = Pattern.compile(" ?<\\S+> ?");
    public static final Pattern OPTIONAL = Pattern.compile(" ?\\[\\S+] ?");
    public static final Pattern RAW_PATTERN = Pattern.compile(" ?\\(\\S+\\) ?");

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
        Matcher rawPatternMatcher = RAW_PATTERN.matcher(command.execution());

        Map<Integer, String> indexes = new HashMap<>();
        List<String> rawPatterns = new ArrayList<>();
        while (rawPatternMatcher.find()) {
            String pattern = command.execution().substring(rawPatternMatcher.start(), rawPatternMatcher.end());
            indexes.put(rawPatternMatcher.start(), pattern);
            rawPatterns.add(pattern);
        }
        while (requiredMatcher.find()) {
            String substring = command.execution().substring(requiredMatcher.start(), requiredMatcher.end());
            if (rawPatterns.stream().noneMatch(string -> string.contains(substring)))
                indexes.put(requiredMatcher.start(), substring);
        }
        while (optionalMatcher.find()) {
            String substring = command.execution().substring(optionalMatcher.start(), optionalMatcher.end());
            if (rawPatterns.stream().noneMatch(string -> string.contains(substring)))
                indexes.put(optionalMatcher.start(), substring);
        }

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
        int typeIndex = 1;
        for (String groupName : groupsName)
            currentCommand = addGroup(groupName.matches(RAW_PATTERN.pattern()) ? null : types[typeIndex++], groupName.trim(), currentCommand);

        pattern = Pattern.compile(currentCommand.trim() + (command.acceptAny() ? ".*?" : ""));
    }

    String addGroup(Class<?> type, String groupName, String currentCommand) {
        boolean required = false;
        Arg.Type argType = null;
        if (groupName.matches(RAW_PATTERN.pattern())) {
            argType = Arg.Type.RAW_PATTERN;
            required = true;
        } else if (type.equals(String.class)) {
            argType = Arg.Type.STRING;
            if (groupName.matches(REQUIRED.pattern())) {
                currentCommand = currentCommand.replaceFirst(REQUIRED.pattern(), " ?(\\\\S+) ?");
                required = true;
            }
            if (groupName.matches(OPTIONAL.pattern()))
                currentCommand = currentCommand.replaceFirst(OPTIONAL.pattern(), " ?(\\\\S+)? ?");
        } else if (type.equals(String[].class)) {
            argType = Arg.Type.ARRAY;
            if (groupName.matches(REQUIRED.pattern())) {
                currentCommand = currentCommand.replaceFirst(REQUIRED.pattern(), " (.+) ?");
                required = true;
            }
            if (groupName.matches(OPTIONAL.pattern()))
                currentCommand = currentCommand.replaceFirst(OPTIONAL.pattern(), " ?(.*)? ?");
        }
        if (argType != null)
            groups.add(new Arg(groupName, argType, required));
        return currentCommand;
    }

    public ArgsMap getArgs(String[] args) {
        Map<String, Args> map = new LinkedHashMap<>();
        Matcher matcher = pattern.matcher(String.join(" ", args));
        if (!matcher.matches())
            return null;

        if (groupsName.isEmpty())
            return new ArgsMap(map);

        for (int i = 0; i < matcher.groupCount(); i++) {
            String groupName = groupsName.get(i);
            Arg group = groups.get(i);
            if (group.type == Arg.Type.RAW_PATTERN)
                continue;
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
        final boolean required;

        enum Type {
            STRING, ARRAY, RAW_PATTERN
        }
    }
}
