package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
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

    public static final Object EMPTY = new Object();

    @NotNull
    private final String name;
    private boolean required;
    private final boolean userInput;
    @Nullable
    private final ArgsReader<?> reader;
    @Nullable
    private final String description;

    public Object parse(String input) throws ArgsReaderException {
        if (reader == null)
            return EMPTY;

        return reader.read(input);
    }
}
