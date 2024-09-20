package com.wizardlybump17.wlib.command.args.reader;

import com.github.sisyphsu.dateparser.DateParserUtils;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateReader extends ArgsReader<Date> {

    @Override
    public @NonNull Class<Date> getType() {
        return Date.class;
    }

    @Override
    public @Nullable Date read(@NonNull String string) {
        try {
            return DateParserUtils.parseDate(string);
        } catch (DateTimeParseException e) {
            return null; //i need to rework the command system :C
        }
    }
}
