package com.wizardlybump17.wlib.util.test;

import com.wizardlybump17.wlib.util.StringUtil;
import com.wizardlybump17.wlib.util.exception.QuotedStringException;
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
    void testQuotedStringAfterNonQuotedStringException() {
        assertThrows(
                QuotedStringException.class,
                () -> StringUtil.parseQuotedStrings("Hello World\"Hi\"", QUOTE, ESCAPE, DELIMITER),
                QuotedStringException.QUOTED_WITHOUT_DELIMITER
        );
    }

    @Test
    void testNonQuotedStringAfterQuotedStringException() {
        assertThrows(
                QuotedStringException.class,
                () -> StringUtil.parseQuotedStrings("\"Hello\"World", QUOTE, ESCAPE, DELIMITER),
                QuotedStringException.NON_QUOTED_AFTER_QUOTED
        );
    }
}
