package com.wizardlybump17.wlib.command.holder;

/**
 * Represents something that can holds commands
 * @param <H> The holder type
 */
public interface CommandHolder<H> {

    Command getCommand(String name);
    H getHandle();
}
