package com.wizardlybump17.wlib.command.completer;

import com.wizardlybump17.wlib.object.Registry;

public class ArgumentCompleterRegistry extends Registry<Class<?>, ArgumentCompleter> {

    public static final ArgumentCompleterRegistry INSTANCE = new ArgumentCompleterRegistry();

    private ArgumentCompleterRegistry() {
    }
}
