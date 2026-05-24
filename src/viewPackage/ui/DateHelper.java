package viewPackage.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy");

        return date.format(formatter);
    }

    public static String formatTime(LocalTime time) {
        if (time == null) {
            return "";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm");

        return time.format(formatter);
    }
}