package com.wizardlybump17.wlib.test.command.manager.listener;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.manager.listener.CommandManagerListener;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.test.util.AssertionUtil;
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

            @Override
            public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
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

            @Override
            public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
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

            @Override
            public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test0")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test1")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test2")));

        manager.clear();

        Assertions.assertTrue(called.get());
    }

    @Test
    void testBeingCalledUnregisterByIdentifierAndName() {
        List<String> seenCommands = new ArrayList<>();

        CommandManager manager = new CommandManager();
        manager.addListener(new CommandManagerListener() {
            @Override
            public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
            }

            @Override
            public void onClear(@NotNull CommandManager manager) {
            }

            @Override
            public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
                seenCommands.add(identifier + CommandManager.SEPARATOR + command.getName());
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test0")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test1")));
        manager.registerCommand("test", new Command(new LiteralCommandNode("test2")));

        manager.unregister("test", "test0");
        manager.unregister("test", "test1");
        manager.unregister("test", "test2");

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
    void testBeingCalledUnregisterByIdentifierAndHolder0() {
        List<String> seenCommands = new ArrayList<>();

        CommandManager manager = new CommandManager();
        manager.addListener(new CommandManagerListener() {
            @Override
            public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
            }

            @Override
            public void onClear(@NotNull CommandManager manager) {
            }

            @Override
            public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
                seenCommands.add(identifier + CommandManager.SEPARATOR + command.getName());
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test0")), "test");
        manager.registerCommand("test", new Command(new LiteralCommandNode("test1")), "test");
        manager.registerCommand("test", new Command(new LiteralCommandNode("test2")), "test");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test10")), "test1");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test11")), "test1");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test12")), "test1");

        manager.unregisterByHolder("test", "test");

        AssertionUtil.assertContentEquals(
                List.of(
                        "test:test0",
                        "test:test1",
                        "test:test2"
                ),
                seenCommands
        );
    }

    @Test
    void testBeingCalledUnregisterByIdentifierAndHolder1() {
        List<String> seenCommands = new ArrayList<>();

        CommandManager manager = new CommandManager();
        manager.addListener(new CommandManagerListener() {
            @Override
            public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
            }

            @Override
            public void onClear(@NotNull CommandManager manager) {
            }

            @Override
            public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
                seenCommands.add(identifier + CommandManager.SEPARATOR + command.getName());
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test0")), "test");
        manager.registerCommand("test", new Command(new LiteralCommandNode("test1")), "test");
        manager.registerCommand("test", new Command(new LiteralCommandNode("test2")), "test");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test10")), "test1");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test11")), "test1");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test12")), "test1");

        manager.unregisterByHolder("test1", "test1");

        AssertionUtil.assertContentEquals(
                List.of(
                        "test1:test10",
                        "test1:test11",
                        "test1:test12"
                ),
                seenCommands
        );
    }

    @Test
    void testBeingCalledUnregisterByIdentifierAndHolder2() {
        List<String> seenCommands = new ArrayList<>();

        CommandManager manager = new CommandManager();
        manager.addListener(new CommandManagerListener() {
            @Override
            public void onRegister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
            }

            @Override
            public void onClear(@NotNull CommandManager manager) {
            }

            @Override
            public void onUnregister(@NotNull String identifier, @NotNull Command command, @Nullable Object holder, @NotNull CommandManager manager) {
                seenCommands.add(identifier + CommandManager.SEPARATOR + command.getName());
            }
        });

        manager.registerCommand("test", new Command(new LiteralCommandNode("test0")), "test");
        manager.registerCommand("test", new Command(new LiteralCommandNode("test1")), "test");
        manager.registerCommand("test", new Command(new LiteralCommandNode("test2")), "test");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test10")), "test1");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test11")), "test1");
        manager.registerCommand("test1", new Command(new LiteralCommandNode("test12")), "test1");

        manager.unregisterByHolder("test", "test");
        manager.unregisterByHolder("test1", "test1");

        AssertionUtil.assertContentEquals(
                List.of(
                        "test:test0",
                        "test:test1",
                        "test:test2",
                        "test1:test10",
                        "test1:test11",
                        "test1:test12"
                ),
                seenCommands
        );
    }
}
