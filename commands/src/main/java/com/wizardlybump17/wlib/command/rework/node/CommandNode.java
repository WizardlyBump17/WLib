package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.exception.InvalidInputException;
import com.wizardlybump17.wlib.command.rework.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.rework.input.AllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public abstract class CommandNode<T> {

    private final @NotNull String name;
    private final @NotNull LinkedHashMap<String, CommandNode<?>> children;
    private final @NotNull AllowedInputs<T> allowedInputs;
    private final @Nullable CommandNodeExecutor<?> executor;
    private final @Nullable String permission;

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this.name = name;
        this.children = (LinkedHashMap<String, CommandNode<?>>) MapUtils.collectionToMap(LinkedHashMap::new, children, CommandNode::getName);
        this.allowedInputs = allowedInputs;
        this.executor = executor;
        this.permission = permission;
    }

    public CommandNode(@NotNull String name, @NotNull AllowedInputs<T> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), allowedInputs, executor, permission);
    }

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs, @Nullable String permission) {
        this(name, children, allowedInputs, null, permission);
    }

    public CommandNode(@NotNull String name, @NotNull AllowedInputs<T> allowedInputs, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, permission);
    }

    public CommandNode(@NotNull String name, @NotNull AllowedInputs<T> allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), allowedInputs, executor, null);
    }

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs) {
        this(name, children, allowedInputs, null, null);
    }

    public CommandNode(@NotNull String name, @NotNull AllowedInputs<T> allowedInputs) {
        this(name, List.of(), allowedInputs, null, null);
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull @Unmodifiable SequencedCollection<CommandNode<?>> getChildren() {
        return Collections.unmodifiableSequencedCollection(children.sequencedValues());
    }

    public @NotNull AllowedInputs<T> getAllowedInputs() {
        return allowedInputs;
    }

    public abstract @Nullable T parse(@NotNull String input) throws InputParsingException;

    public final boolean isValidInput(@Nullable T input) {
        return allowedInputs.isAllowed(input);
    }

    public final @Nullable T parseOrInvalid(@NotNull String input) throws InputParsingException, InvalidInputException {
        T parse = parse(input);
        if (!isValidInput(parse))
            throw new InvalidInputException("Invalid input " + input);
        return parse;
    }

    public @NotNull List<T> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> args, @NotNull String currentInput) {
        return List.of();
    }

    public @Nullable CommandNodeExecutor<?> getExecutor() {
        return executor;
    }

    public @Nullable String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "CommandNode{" +
                "name='" + name + '\'' +
                ", children=" + children +
                ", allowedInputs=" + allowedInputs +
                ", executor=" + executor +
                ", permission='" + permission + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        CommandNode<?> that = (CommandNode<?>) o;
        return Objects.equals(name, that.name)
                && Objects.equals(children, that.children)
                && Objects.equals(allowedInputs, that.allowedInputs)
                && Objects.equals(executor, that.executor)
                && Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, children, allowedInputs, executor, permission);
    }

    public abstract @NotNull CommandNode<T> withChildren(@NotNull List<CommandNode<?>> children);

    public @NotNull CommandNode<T> merge(@NotNull CommandNode<?> other) {
        LinkedHashMap<String, CommandNode<?>> newChildren = new LinkedHashMap<>();

        children.forEach((leftKey, leftChild) -> {
            if (!other.children.containsKey(leftKey)) {
                newChildren.put(leftKey, leftChild);
                return;
            }

            newChildren.put(leftKey, leftChild.merge(other.children.get(leftKey)));
        });

        return withChildren(new ArrayList<>(newChildren.values()));
    }
}
