package com.wizardlybump17.wlib.command.registered;

import com.wizardlybump17.wlib.command.CommandManager;
import com.wizardlybump17.wlib.command.CommandResult;
import com.wizardlybump17.wlib.command.CommandSender;
import com.wizardlybump17.wlib.command.args.ArgsNode;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import com.wizardlybump17.wlib.command.data.CommandData;
import com.wizardlybump17.wlib.command.exception.CommandException;
import com.wizardlybump17.wlib.command.executor.CommandExecutor;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class RegisteredCommand implements Comparable<RegisteredCommand> {

    private final @NotNull CommandData command;
    private final @NotNull List<ArgsNode> nodes;
    private final @NotNull CommandExecutor executor;

    public RegisteredCommand(@NotNull CommandData command, @NotNull List<ArgsNode> nodes, @NotNull CommandExecutor executor) {
        this.command = command;
        this.nodes = nodes;
        this.executor = executor;
    }

    protected RegisteredCommand(@NotNull CommandData command, @NotNull List<ArgsNode> nodes) {
        this.command = command;
        this.nodes = nodes;
        executor = createExecutor();
    }

    protected CommandExecutor createExecutor() {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return command.getExecution().split(" ")[0];
    }

    @Override
    public int compareTo(@NotNull RegisteredCommand o) {
        return Integer.compare(o.command.getPriority(), command.getPriority());
    }

    public Optional<LinkedHashMap<String, Object>> parse(String input) throws ArgsReaderException {
        List<String> toParse = StringUtil.parseQuotedStrings(input);

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        if (checkNodes(result, toParse))
            return Optional.of(result);

        return Optional.empty();
    }

    protected boolean checkNodes(LinkedHashMap<String, Object> target, List<String> strings) throws ArgsReaderException {
        if (nodes.size() > strings.size())
            return false;

        for (int i = 0; i < nodes.size() && i < strings.size(); i++) {
            ArgsNode node = nodes.get(i);
            if (!node.isUserInput()) {
                if (node.getName().equalsIgnoreCase(strings.get(i)))
                    continue;
                return false;
            }

            Object parse = node.parse(strings.get(i));
            if (parse != ArgsNode.EMPTY)
                target.put(node.getName(), parse);
        }

        return true;
    }

    public @NotNull CommandResult execute(@NotNull CommandSender<?> sender, @NotNull String string) throws CommandException {
        if (!canExecute(sender))
            return CommandResult.INVALID_SENDER;

        String permission = command.getPermission();
        if (permission != null && !sender.hasPermission(permission))
            return CommandResult.PERMISSION_FAIL;

        try {
            Optional<LinkedHashMap<String, Object>> parse = parse(string);
            if (parse.isEmpty())
                return CommandResult.ARGS_FAIL;

            LinkedHashMap<String, Object> args = parse.get();
            if (!isValidArgs(args))
                return CommandResult.ARGS_FAIL;

            executor.execute(sender, args);
            return CommandResult.SUCCESS;
        } catch (ArgsReaderException e) {
            return CommandResult.EXCEPTION;
        }
    }

    protected boolean isValidArgs(@NotNull LinkedHashMap<String, Object> args) {
        return true;
    }

    public boolean canExecute(@NotNull CommandSender<?> sender) {
        Class<?> type = command.getSenderHandleType();
        return type == null || type.isInstance(sender);
    }

    @Override
    public String toString() {
        return "RegisteredCommand{" + command.getExecution() + "}";
    }

    public @NotNull CommandData getCommand() {
        return command;
    }

    public @NotNull CommandExecutor getExecutor() {
        return executor;
    }

    public @NotNull List<ArgsNode> getNodes() {
        return nodes;
    }

    public boolean isOwnedBy(@NotNull Object object) {
        return false;
    }

    public void onRegister(@NotNull CommandManager manager) {
    }

    public void onUnregister(@NotNull CommandManager manager) {
    }
}