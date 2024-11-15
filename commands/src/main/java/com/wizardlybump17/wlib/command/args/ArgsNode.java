package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.Argument;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node of a command argument
 */
@Data
@AllArgsConstructor
public class ArgsNode {

    public static final Object EMPTY = new Object();

    @NotNull
    private final String name;
    private final boolean userInput;
    @Nullable
    private final ArgsReader<?> reader;
    @Nullable
    private final String description;
    private final boolean isArgument;

    /**
     * Parses the given input
     * @param input the input
     * @return the parsed object
     * @throws ArgsReaderException if the input is invalid
     */
    public Object parse(String input) throws ArgsReaderException {
        if (reader == null)
            return EMPTY;

        Object object = reader.read(input);
        if (isArgument)
            return new Argument<>(name, object, input);

        return object;
    }

    public static @NotNull ArgsNode literal(@NotNull String string) {
        return new ArgsNode(string, false, null, null, false);
    }

    public static @NotNull ArgsNode userInput(@NotNull String name, @NotNull ArgsReader<?> reader) {
        return new ArgsNode(name, true, reader, null, false);
    }
}
