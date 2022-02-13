package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node of an args
 */
@Data
@AllArgsConstructor
public class ArgsNode {

    @NotNull
    private final String name;
    private boolean required;
    private final boolean userInput;
    @Nullable
    private final ArgsReader<?> reader;
    @Nullable
    private final String description;
}
