package echo.task;

import java.time.LocalDateTime;

import echo.exception.TaskException;

/**
 * Represents an event task with a start and end date/time.
 * An event is a task that occurs during a specific time period, with both
 * a starting time and an ending time.
 *
 * Events are validated to ensure the start time is before the end time.
 */
public class Event extends TimedTask {
    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Initializes an Event object.
     *
     * @param description description of the Event.
     * @param start Event start time.
     * @param end Event end time.
     * @throws TaskException if start date is later than end date.
     */
    public Event(String description, String start, String end) throws TaskException {
        super(description);

        LocalDateTime startDate = super.parseDate(start);
        LocalDateTime endDate = super.parseDate(end);
        // validity checks first
        this.checkStartBeforeEnd(startDate, endDate);

        this.start = startDate;
        this.end = endDate;
    }

    /**
     * Displays Event class in String form
     *
     * @return String representation of Event when displayed on the terminal
     */
    @Override
    public String toString() {
        String startString = this.start.format(TimedTask.FORMATTER_TO_STRING);
        String endString = this.end.format(TimedTask.FORMATTER_TO_STRING);
        return "[E]" + super.toString() + " (from: " + startString + " to: " + endString + ")";
    }

    /**
     * Creates String representation of Event task to be saved.
     * Return String is of the form: E | 1 | project meeting | Aug 6th 2pm | 4pm
     *
     * @return String representation of the Event task ready to be saved into a .txt file.
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;
        String startString = this.start.format(TimedTask.FORMATTER_TO_SAVE);
        String endString = this.end.format(TimedTask.FORMATTER_TO_SAVE);
        return "E | " + isDone + " | " + super.description + " | " + startString + " | " + endString;
    }

    /**
     * Helper method to check that Event start date is before Event end date
     *
     * @param start start date of type LocalDateTime
     * @param end end date of type LocalDateTime
     * @throws TaskException if start later than end
     */
    private void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) throws TaskException {
        if (start.isAfter(end)) {
            throw new TaskException("Start date cannot be later than end date!");
        }
    }
}
