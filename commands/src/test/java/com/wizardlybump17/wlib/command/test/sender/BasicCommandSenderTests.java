package com.wizardlybump17.wlib.command.test.sender;

import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class BasicCommandSenderTests {

    @Test
    void testPermissionsAllTrue() {
        String name = "Test Sender";
        BasicCommandSender<Object> sender = BasicCommandSender.builder()
                .handle(new Object())
                .name(name)
                .id(UUID.nameUUIDFromBytes(name.getBytes()))
                .permissionTest(permission -> true)
                .build();
        Assertions.assertTrue(sender.hasPermission("test0.permission0"));
        Assertions.assertTrue(sender.hasPermission("test0.permission1"));
        Assertions.assertTrue(sender.hasPermission("test0.permission2"));
        Assertions.assertTrue(sender.hasPermission("test1.permission0"));
        Assertions.assertTrue(sender.hasPermission("test1.permission1"));
        Assertions.assertTrue(sender.hasPermission("test1.permission2"));
    }

    @Test
    void testPermissionsAllFalse() {
        String name = "Test Sender";
        BasicCommandSender<Object> sender = BasicCommandSender.builder()
                .handle(new Object())
                .name(name)
                .id(UUID.nameUUIDFromBytes(name.getBytes()))
                .permissionTest(permission -> false)
                .build();
        Assertions.assertFalse(sender.hasPermission("test0.permission0"));
        Assertions.assertFalse(sender.hasPermission("test0.permission1"));
        Assertions.assertFalse(sender.hasPermission("test0.permission2"));
        Assertions.assertFalse(sender.hasPermission("test1.permission0"));
        Assertions.assertFalse(sender.hasPermission("test1.permission1"));
        Assertions.assertFalse(sender.hasPermission("test1.permission2"));
    }

    @Test
    void testSpecificPermissions() {
        String name = "Test Sender";
        BasicCommandSender<Object> sender = BasicCommandSender.builder()
                .handle(new Object())
                .name(name)
                .id(UUID.nameUUIDFromBytes(name.getBytes()))
                .permissionTest(permission -> permission.startsWith("test0."))
                .build();
        Assertions.assertTrue(sender.hasPermission("test0.permission0"));
        Assertions.assertTrue(sender.hasPermission("test0.permission1"));
        Assertions.assertTrue(sender.hasPermission("test0.permission2"));
        Assertions.assertFalse(sender.hasPermission("test1.permission0"));
        Assertions.assertFalse(sender.hasPermission("test1.permission1"));
        Assertions.assertFalse(sender.hasPermission("test1.permission2"));
        Assertions.assertFalse(sender.hasPermission("test2.permission0"));
        Assertions.assertFalse(sender.hasPermission("test2.permission1"));
        Assertions.assertFalse(sender.hasPermission("test2.permission2"));
    }
}
