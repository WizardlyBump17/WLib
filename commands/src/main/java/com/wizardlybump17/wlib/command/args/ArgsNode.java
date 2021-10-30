package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import lombok.Data;

/**
 * Represents a node of an args
 */
@Data
public class ArgsNode {

    private final String name;
    private final boolean required;
    private final boolean userInput;
    private final ArgsReader<?> reader;
}
