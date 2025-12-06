package com.wizardlybump17.wlib.test.command.manager.listener;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.manager.listener.CommandManagerListener;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class CommandManagerListenerTests {

    @Test
    void testBeingCalledRegister() {
        AtomicInteger counter = new AtomicInteger();

        CommandManager manager = new CommandManager();
        manager.addListener(new CommandManagerListener() {
            @Override
            public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
                counter.incrementAndGet();
            }

            @Override
            public void onClear(@NotNull CommandManager manager) {
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test")));

        Assertions.assertEquals(3, counter.get());
    }

    @Test
    void testSeeingRightCommands() {
        List<String> seenCommands = new ArrayList<>();

        CommandManager manager = new CommandManager();
        manager.addListener(new CommandManagerListener() {
            @Override
            public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
                seenCommands.add(identifier + CommandManager.SEPARATOR + command.getRoot().getName());
            }

            @Override
            public void onClear(@NotNull CommandManager manager) {
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test0")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test1")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test2")));

        Assertions.assertEquals(
                List.of(
                        "test:test0",
                        "test:test1",
                        "test:test2"
                ),
                seenCommands
        );
    }

    @Test
    void testBeingCalledClear() {
        AtomicBoolean called = new AtomicBoolean();

        CommandManager manager = new CommandManager();

        manager.addListener(new CommandManagerListener() {
            @Override
            public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
            }

            @Override
            public void onClear(@NotNull CommandManager manager) {
                Assertions.assertTrue(manager.isEmpty());
                called.set(true);
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test0")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test1")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test2")));

        manager.clear();

        Assertions.assertTrue(called.get());
    }
}
