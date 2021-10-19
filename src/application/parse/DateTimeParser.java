package application.parse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateTimeParser {

    public static LocalDateTime getDateTimeFromString(String dateString, String timeString) {
        String string = dateString + ":" + timeString;
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("M/d/yyyy:H:mm:ss");
        return LocalDateTime.from(pattern.parse(string));
    }

    public static LocalDate getDateFromString(String dateString) {
        DateTimeFormatter patternA = DateTimeFormatter.ofPattern("M/d/yyyy");
        DateTimeFormatter patternB = DateTimeFormatter.ofPattern("MM/d/yy");
        try {
            return LocalDate.from(patternA.parse(dateString));
        } catch (Exception e) {
            return LocalDate.from(patternB.parse(dateString));
        }
    }
}
