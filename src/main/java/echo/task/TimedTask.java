package echo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import echo.exception.TaskException;

/**
 * Represents an abstract class in the Echo application.
 * This is the base class for all subclasses that are required to store some form of date information.
 *
 * Provides static variables for its children to format and parse the date time information.
 */
public abstract class TimedTask extends Task {
    // formatter for displaying the string representation only
    protected static final DateTimeFormatter FORMATTER_TO_STRING = DateTimeFormatter.ofPattern("d MMM yyyy HHmm");
    // formatter for parsing and saving the deadline
    protected static final DateTimeFormatter FORMATTER_TO_SAVE = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private static final String ERROR_MESSAGE_DATE_WRONG_FORMAT =
        "Date is in the wrong format! Must be in yyyy-mm-dd HHmm";

    protected TimedTask(String description) throws TaskException {
        super(description);
    }

    /**
     * Parses all dates in the form of String
     * Intended as a helper method for child classes
     *
     * @param date date in String format
     * @return the parsed date as a LocalDateTime object
     * @throws TaskException if date is in the incorrect format
     */
    protected LocalDateTime parseDate(String date) throws TaskException {
        try {
            return LocalDateTime.parse(date, TimedTask.FORMATTER_TO_SAVE);
        } catch (DateTimeParseException e) {
            throw new TaskException(TimedTask.ERROR_MESSAGE_DATE_WRONG_FORMAT);
        }
    }
}
