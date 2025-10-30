package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

class CommandManagerTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);
}
