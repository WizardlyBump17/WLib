package com.wizardlybump17.wlib.test.util;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.util.CollectionUtil;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AssertionFailureBuilder;

import java.util.Collection;
import java.util.List;

public final class AssertionUtil {

    private AssertionUtil() {
    }

    public static void assertCommandsEqualsIgnoreExecutor(@Nullable List<Command> expected, @Nullable List<Command> actual) {
        if (expected == null) {
            if (actual != null) {
                AssertionFailureBuilder.assertionFailure()
                        .expected(null)
                        .actual(actual)
                        .buildAndThrow();
            }
            return;
        }

        if (expected != null) {
            if (actual == null) {
                AssertionFailureBuilder.assertionFailure()
                        .expected(expected)
                        .actual(null)
                        .buildAndThrow();
                return;
            }
        }

        if (expected.size() != actual.size()) {
            AssertionFailureBuilder.assertionFailure()
                    .expected(expected)
                    .actual(actual)
                    .message("Expected and actual lists have different sizes")
                    .buildAndThrow();
            return;
        }

        for (int i = 0; i < expected.size(); i++) {
            Command expectedCommand = expected.get(i);
            Command actualCommand = actual.get(i);

            if (!expectedCommand.equalsIgnoreExecutor(actualCommand)) {
                AssertionFailureBuilder.assertionFailure()
                        .expected(expectedCommand)
                        .actual(actualCommand)
                        .message("Commands at index " + i + " are not equal")
                        .buildAndThrow();
                return;
            }
        }
    }

    public static <T> void assertContentEquals(@Nullable Collection<T> expected, @Nullable Collection<T> actual) {
        if (expected == null) {
            if (actual != null) {
                AssertionFailureBuilder.assertionFailure()
                        .expected(null)
                        .actual(actual)
                        .buildAndThrow();
            }
            return;
        }

        if (expected != null) {
            if (actual == null) {
                AssertionFailureBuilder.assertionFailure()
                        .expected(expected)
                        .actual(null)
                        .buildAndThrow();
                return;
            }
        }

        if (expected.size() != actual.size()) {
            AssertionFailureBuilder.assertionFailure()
                    .expected(expected)
                    .actual(actual)
                    .message("Expected and actual collections have different sizes")
                    .buildAndThrow();
            return;
        }

        if (!CollectionUtil.contentEquals(expected, actual)) {
            AssertionFailureBuilder.assertionFailure()
                    .expected(expected)
                    .actual(actual)
                    .message("Expected and actual collections do not contain the same elements")
                    .buildAndThrow();
            return;
        }
    }
}
