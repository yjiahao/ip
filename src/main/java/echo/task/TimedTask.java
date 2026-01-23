package echo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import echo.exception.TaskException;

public abstract class TimedTask extends Task {
    // formatter for displaying the string representation only
    protected static final DateTimeFormatter TO_STRING_FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy HHmm");
    // formatter for parsing and saving the deadline
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    protected TimedTask(String description) throws TaskException {
        super(description);
    }

    /**
     * Helper method for child classes to parse all dates in the form of String
     * @param date date in String format
     * @return the parsed date as a LocalDateTime object
     * @throws TaskException
     */
    protected LocalDateTime parseDate(String date) throws TaskException {
        try {
            return LocalDateTime.parse(date, TimedTask.FORMATTER);
        } catch (DateTimeParseException e) {
            throw new TaskException("Date is in the wrong format! Must be in yyyy-mm-dd HHmm");
        }
    }
}
