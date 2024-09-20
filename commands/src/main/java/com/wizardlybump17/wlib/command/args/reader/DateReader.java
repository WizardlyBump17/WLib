package com.wizardlybump17.wlib.command.args.reader;

import com.github.sisyphsu.dateparser.DateParserUtils;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DateReader extends ArgsReader<Date> {

    //i could do a dedicated system for this, but this one is simpler and works so idc
    public static final @NonNull Map<String, Supplier<Date>> PROVIDERS = new HashMap<>();

    static {
        PROVIDERS.put("now", Date::new);
        PROVIDERS.put("today", () -> getToday().getTime());
        PROVIDERS.put("yesterday", () -> {
            Calendar today = getToday();
            today.add(Calendar.DAY_OF_YEAR, -1);
            return today.getTime();
        });
        PROVIDERS.put("tomorrow", () -> {
            Calendar today = getToday();
            today.add(Calendar.DAY_OF_YEAR, 1);
            return today.getTime();
        });
    }

    @Override
    public @NonNull Class<Date> getType() {
        return Date.class;
    }

    @Override
    public @Nullable Date read(@NonNull String string) {
        Supplier<Date> provider = PROVIDERS.get(string.toLowerCase());
        if (provider != null)
            return provider.get();

        try {
            return DateParserUtils.parseDate(string);
        } catch (DateTimeParseException e) {
            return null; //i need to rework the command system :C
        }
    }

    public static @NonNull Calendar getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
