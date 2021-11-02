package com.example.ptvimproved24.datastructures;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class Time {

    private static Time instance;

    private Time() {

    }

    //use double lock method
    public static Time getInstance() {
        if (instance == null) {
            synchronized (Time.class) {
                if (instance == null) {
                    instance = new Time();
                }
            }
        }
        return instance;
    }

    public long timeGap(String timeStr) {
        ZonedDateTime a = Instant.now().atZone(ZoneId.of("Australia/Melbourne"));
        TemporalAccessor time = DateTimeFormatter
                .ofLocalizedDateTime (FormatStyle.SHORT)
                .withLocale (Locale.UK)
                .withZone(ZoneId.of("Australia/Melbourne"))
                .parse(timeStr);
        ZonedDateTime b = ZonedDateTime.from(time);
        long diffInMinutes = ChronoUnit.MINUTES.between(a, b);
        return diffInMinutes;
    }

    public String UTCToAEST(String utc) {
        String result = Instant.parse ( utc )
                .atZone ( ZoneId.of ( "Australia/Sydney" ) )
                .format (
                        DateTimeFormatter.ofLocalizedDateTime ( FormatStyle.SHORT )
                                .withLocale ( Locale.UK ));
        return result;
    }

    public String gapInString(String timeStr) {
        long gap = timeGap(timeStr);
        String result = "";
        String appendStr = "";
        if (gap <= 1) {
            appendStr = " < 1 min";
            result = appendStr;
        } else if (gap > 60) {
            long hours = gap / 60;
            if (hours < 24) {
                appendStr = hours > 1 ? " hours" : " hour";
                result = hours + appendStr;
            } else {
                result = " > 1 day";
            }
        } else {
            result = gap + " mins";
        }
        return result;
    }



}
