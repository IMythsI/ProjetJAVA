package viewPackage.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateHelper {

    private static final Locale FRENCH_LOCALE = Locale.FRENCH;

    private static final DateTimeFormatter FULL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", FRENCH_LOCALE);

    private static final DateTimeFormatter SHORT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", FRENCH_LOCALE);

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm", FRENCH_LOCALE);

    private DateHelper() {
        // Utility class
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "-";
        }

        return date.format(FULL_DATE_FORMATTER);
    }

    public static String formatShortDate(LocalDate date) {
        if (date == null) {
            return "-";
        }

        return date.format(SHORT_DATE_FORMATTER);
    }

    public static String formatTime(LocalTime time) {
        if (time == null) {
            return "-";
        }

        return time.format(TIME_FORMATTER);
    }

    public static String formatDateTime(LocalDate date, LocalTime time) {
        return formatShortDate(date) + " à " + formatTime(time);
    }
}