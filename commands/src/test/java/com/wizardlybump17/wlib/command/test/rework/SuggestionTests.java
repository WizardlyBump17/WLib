package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import org.junit.jupiter.api.Test;

import java.util.List;

class SuggestionTests {

    @Test
    void test() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), context -> CommandResult.successful(context, "a1"))
                        )),
                        new LiteralCommandNode("world1", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), context -> CommandResult.successful(context, "a1"))
                        )),
                        new LiteralCommandNode("world2", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), context -> CommandResult.successful(context, "a1"))
                        )),
                        new LiteralCommandNode("world3", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), context -> CommandResult.successful(context, "a1"))
                        ))
                ))
        );

        List<String> suggestions = command.getSuggestions(null, List.of("hello", "world"));
        System.out.println(suggestions);
    }
}
