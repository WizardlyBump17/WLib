package com.wizardlybump17.wlib.util.test;

import com.wizardlybump17.wlib.util.StringUtil;
import com.wizardlybump17.wlib.util.exception.QuotedStringException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuotedStringsTests {

    static final char QUOTE = '"';
    static final char ESCAPE = '\\';
    static final char DELIMITER = ' ';

    @Test
    void testNoQuotes() {
        assertEquals(
                List.of("Hello", "World"),
                StringUtil.parseQuotedStrings("Hello World", QUOTE, ESCAPE, DELIMITER)
        );
        assertEquals(
                List.of("Hello", "Beautiful", "World"),
                StringUtil.parseQuotedStrings("Hello Beautiful World", QUOTE, ESCAPE, DELIMITER)
        );
        assertEquals(
                List.of("Hello", "World", "Hi"),
                StringUtil.parseQuotedStrings("Hello World   Hi", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testQuotesInTheBeginning() {
        assertEquals(
                List.of("Hello", "World"),
                StringUtil.parseQuotedStrings("\"Hello\" World", QUOTE, ESCAPE, DELIMITER)
        );
        assertEquals(
                List.of("Hello World", "Hi"),
                StringUtil.parseQuotedStrings("\"Hello World\" Hi", QUOTE, ESCAPE, DELIMITER)
        );
        assertEquals(
                List.of("Hello World", "Hi", "there"),
                StringUtil.parseQuotedStrings("\"Hello World\" Hi there", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testQuotesInTheEnd() {
        assertEquals(
                List.of("Hello", "World", "Hi"),
                StringUtil.parseQuotedStrings("Hello World \"Hi\"", QUOTE, ESCAPE, DELIMITER)
        );
        assertEquals(
                List.of("Hello", "World Hi"),
                StringUtil.parseQuotedStrings("Hello \"World Hi\"", QUOTE, ESCAPE, DELIMITER)
        );
        assertEquals(
                List.of("Hello", "World", "Hi there"),
                StringUtil.parseQuotedStrings("Hello World \"Hi there\"", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testQuotesInTheMiddle() {
        assertEquals(
                List.of("Hello", "World Hi", "there"),
                StringUtil.parseQuotedStrings("Hello \"World Hi\" there", QUOTE, ESCAPE, DELIMITER)
        );
        assertEquals(
                List.of("Hello", "World Hi there, nice", "string"),
                StringUtil.parseQuotedStrings("Hello \"World Hi there, nice\" string", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testNotEndedQuotes() {
        assertThrows(
                QuotedStringException.class,
                () -> StringUtil.parseQuotedStrings("Hello \"World", QUOTE, ESCAPE, DELIMITER),
                QuotedStringException.UNCLOSED_QUOTE
        );
        assertThrows(
                QuotedStringException.class,
                () -> StringUtil.parseQuotedStrings("Hello World \"Hi", QUOTE, ESCAPE, DELIMITER),
                QuotedStringException.UNCLOSED_QUOTE
        );
        assertThrows(
                QuotedStringException.class,
                () -> StringUtil.parseQuotedStrings("Hello World \"Hi there, nice\" \"string", QUOTE, ESCAPE, DELIMITER),
                QuotedStringException.UNCLOSED_QUOTE
        );
    }

    @Test
    void testEscapeInTheEndException() {
        assertThrows(
                QuotedStringException.class,
                () -> StringUtil.parseQuotedStrings("Hello World \\", QUOTE, ESCAPE, DELIMITER),
                QuotedStringException.INVALID_ESCAPE
        );
    }

    @Test
    void testQuotedStringAfterNonQuotedStringSuccess() {
        Assertions.assertEquals(
                List.of("Hello", "WorldHi"),
                StringUtil.parseQuotedStrings("Hello World\"Hi\"", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testNonQuotedStringAfterQuotedStringSuccess() {
        Assertions.assertEquals(
                List.of("Hello", "World"),
                StringUtil.parseQuotedStrings("\"Hello\"World", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testEmpty() {
        Assertions.assertEquals(
                List.of(),
                StringUtil.parseQuotedStrings("", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testEndingWithSpace0() {
        Assertions.assertEquals(
                List.of(),
                StringUtil.parseQuotedStrings(" ", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testEndingWithSpace1() {
        Assertions.assertEquals(
                List.of("Hello", "World"),
                StringUtil.parseQuotedStrings("Hello World ", QUOTE, ESCAPE, DELIMITER)
        );
    }

    @Test
    void testProperlyQuotedTrue() {
        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello World", QUOTE, ESCAPE));

        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello \"World\"", QUOTE, ESCAPE));
        Assertions.assertTrue(StringUtil.isProperlyQuoted("\"Hello World\"", QUOTE, ESCAPE));

        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello \"World    \"", QUOTE, ESCAPE));

        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello \"World\" Hi There", QUOTE, ESCAPE));

        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello \"World\"Hi There", QUOTE, ESCAPE));
        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello\"World\" Hi There", QUOTE, ESCAPE));
        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello\"World\"Hi There", QUOTE, ESCAPE));

        Assertions.assertTrue(StringUtil.isProperlyQuoted("", QUOTE, ESCAPE));
        Assertions.assertTrue(StringUtil.isProperlyQuoted("\"\"", QUOTE, ESCAPE));

        Assertions.assertTrue(StringUtil.isProperlyQuoted("Hello\\ World", QUOTE, ESCAPE));
    }

    @Test
    void testProperlyQuotedFalse() {
        Assertions.assertFalse(StringUtil.isProperlyQuoted("Hello \"World", QUOTE, ESCAPE));
        Assertions.assertFalse(StringUtil.isProperlyQuoted("Hello \"World\" Hi \"There", QUOTE, ESCAPE));

        Assertions.assertFalse(StringUtil.isProperlyQuoted("\"", QUOTE, ESCAPE));

        Assertions.assertFalse(StringUtil.isProperlyQuoted("Hello \"World\" \\", QUOTE, ESCAPE));
    }
}
