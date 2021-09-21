package com.wizardlybump17.wlib.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TimeUnit {

    MILLISECOND(1),
    SECOND(MILLISECOND.millis * 1000),
    MINUTE(SECOND.millis * 60),
    HOUR(MINUTE.millis * 60),
    DAY(HOUR.millis * 24),
    WEEK(DAY.millis * 7),
    MONTH(DAY.millis * 30),
    YEAR(DAY.millis * 365);

    private final long millis;
}
