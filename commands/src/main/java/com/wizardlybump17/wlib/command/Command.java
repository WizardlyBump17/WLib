package com.wizardlybump17.wlib.command;

import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;

public class Command implements Comparable<Command> {

    public static final @NotNull Comparator<Command> COMPARATOR = Comparator.comparing(Command::getFullCommand);

    private final @NotNull LiteralCommandNode root;

    public Command(@NotNull LiteralCommandNode root) {
        this.root = root;
    }

    public @NotNull LiteralCommandNode getRoot() {
        return root;
    }

    public @NotNull Command merge(@NotNull Command other) {
        if (other.getClass() != getClass())
            return other.merge(this);
        return new Command(root.merge(other.getRoot()));
    }

    @Override
    public String toString() {
        return "Command{" +
                "root=" + root +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Command command = (Command) o;
        return Objects.equals(root, command.root);
    }

    public boolean equalsIgnoreExecutor(@Nullable Object other) {
        if (other == null || getClass() != other.getClass())
            return false;
        Command command = (Command) other;
        return root.equalsIgnoreExecutor(command.root);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(root);
    }

    @Override
    public int compareTo(@NotNull Command other) {
        return COMPARATOR.compare(this, other);
    }

    public @NotNull String getFullCommand() {
        return root.getFullCommand();
    }

    public @Nullable CommandNode<?> findNode(@NotNull String name) {
        return root.findChild(name);
    }

    public @NotNull String getName() {
        return root.getName();
    }

    public int getTotalNodes() {
        return root.getTotalNodes();
    }
}
