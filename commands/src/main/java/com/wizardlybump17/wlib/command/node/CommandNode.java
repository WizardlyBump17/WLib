package com.wizardlybump17.wlib.command.node;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.exception.InvalidInputException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.CollectionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public abstract class CommandNode<T> {

    private final @NotNull String name;
    private final @NotNull @Unmodifiable List<CommandNode<?>> children;
    private final @NotNull AllowedInputs<T> allowedInputs;
    private final @Nullable CommandNodeExecutor<?> executor;
    private final @Nullable String permission;

    public CommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<T> allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this.name = name.toLowerCase();
        this.children = List.copyOf(children);
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

    public @NotNull @Unmodifiable List<CommandNode<?>> getChildren() {
        return children;
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
        return allowedInputs.getSuggestions(sender, args, currentInput);
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
                && CollectionUtil.contentEquals(children, that.children)
                && Objects.equals(allowedInputs, that.allowedInputs)
                && Objects.equals(executor, that.executor)
                && Objects.equals(permission, that.permission);
    }

    public boolean equalsIgnoreExecutor(@Nullable Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        CommandNode<?> that = (CommandNode<?>) other;
        return Objects.equals(name, that.name)
                && equalsIgnoreExecutor(children, that.children)
                && Objects.equals(allowedInputs, that.allowedInputs)
                && Objects.equals(permission, that.permission);
    }

    public static boolean equalsIgnoreExecutor(@NotNull Collection<CommandNode<?>> a, @NotNull Collection<CommandNode<?>> b) {
        if (a.size() != b.size())
            return false;

        Iterator<CommandNode<?>> aIterator = a.iterator();
        Iterator<CommandNode<?>> bIterator = b.iterator();

        while (aIterator.hasNext() && bIterator.hasNext()) {
            CommandNode<?> aNode = aIterator.next();
            CommandNode<?> bNode = bIterator.next();
            if (!aNode.equalsIgnoreExecutor(bNode))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, children, allowedInputs, executor, permission);
    }

    public @NotNull String getFullCommand() {
        StringBuilder builder = new StringBuilder();
        if (this instanceof LiteralCommandNode)
            builder.append(name);
        else
            builder.append('<').append(name).append('>');

        for (CommandNode<?> child : children) {
            builder.append(' ');
            builder.append(child.getFullCommand());
        }

        return builder.toString();
    }

    public abstract @NotNull CommandNode<T> withChildren(@NotNull List<CommandNode<?>> children);

    public abstract @NotNull CommandNode<T> withExecutor(@Nullable CommandNodeExecutor<?> executor);

    public abstract @NotNull CommandNode<T> withPermission(@Nullable String permission);

    public @NotNull Optional<CommandNode<?>> getChild(@NotNull String name) {
        for (CommandNode<?> child : children)
            if (child.getName().equals(name))
                return Optional.of(child);
        return Optional.empty();
    }

    public @NotNull CommandNode<T> merge(@NotNull CommandNode<?> right) {
        List<CommandNode<?>> newChildrenLeft = getNewChildren(right, this);
        List<CommandNode<?>> newChildrenRight = getNewChildren(this, right);

        newChildrenLeft.removeIf(newChildLeft -> {
            for (CommandNode<?> newChildRight : newChildrenRight)
                if (newChildRight.getName().equals(newChildLeft.getName()))
                    return true;
            return false;
        });
        newChildrenRight.removeIf(newChildRight -> {
            for (CommandNode<?> newChildLeft : newChildrenLeft)
                if (newChildLeft.getName().equals(newChildRight.getName()))
                    return true;
            return false;
        });

        newChildrenLeft.addAll(newChildrenRight);

        CommandNode<T> newNode = withChildren(newChildrenLeft);

        if (executor == null && right.getExecutor() != null)
            newNode = newNode.withExecutor(right.getExecutor());
        if (permission == null && right.getPermission() != null)
            newNode = newNode.withPermission(right.getPermission());

        return newNode;
    }

    private static @Nullable CommandNodeExecutor<?> getNewExecutor(@NotNull CommandNode<?> left, @NotNull CommandNode<?> right) {
        CommandNodeExecutor<?> leftExecutor = left.getExecutor();
        CommandNodeExecutor<?> rightExecutor = right.getExecutor();
        CommandNodeExecutor<?> newLeftExecutor;

        if (leftExecutor == null && rightExecutor == null) {
            newLeftExecutor = null;
        } else if (leftExecutor == null && rightExecutor != null) {
            newLeftExecutor = rightExecutor;
        } else if (leftExecutor != null && rightExecutor == null) {
            newLeftExecutor = leftExecutor;
        } else if (Objects.equals(leftExecutor, rightExecutor)) {
            newLeftExecutor = leftExecutor;
        } else {
            throw new IllegalStateException("Could not resolve an executor for the merged node.");
        }

        return newLeftExecutor;
    }

    private static @NotNull List<CommandNode<?>> getNewChildren(@NotNull CommandNode<?> left, @NotNull CommandNode<?> right) {
        List<CommandNode<?>> newChildren = new ArrayList<>();

        for (CommandNode<?> rightChild : right.children) {
            Optional<CommandNode<?>> leftChildOptional = left.getChild(rightChild.name);

            if (leftChildOptional.isEmpty()) {
                newChildren.add(rightChild);
                continue;
            }

            CommandNode<?> leftChild = leftChildOptional.get();

            CommandNodeExecutor<?> newLeftExecutor = getNewExecutor(leftChild, rightChild);
            leftChild = leftChild.withExecutor(newLeftExecutor);

            newChildren.add(leftChild.merge(rightChild));
        }

        return newChildren;
    }

    public boolean canExecute(@NotNull CommandSender<?> sender) {
        return permission == null || sender.hasPermission(permission);
    }

    public @Nullable CommandNode<?> findChild(@NotNull String name) {
        for (CommandNode<?> child : children)
            if (child.getName().equals(name))
                return child;

        for (CommandNode<?> child : children) {
            CommandNode<?> found = child.findChild(name);
            if (found != null)
                return found;
        }

        return null;
    }

    public int getTotalNodes() {
        int total = 1;
        for (CommandNode<?> child : children)
            total += child.getTotalNodes();
        return total;
    }
}
