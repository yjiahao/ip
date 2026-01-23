package echo.task;

import java.time.LocalDateTime;

import echo.exception.TaskException;

public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Initializes an Event object.
     * @param description description of the Event.
     * @param start Event start time.
     * @param end Event end time.
     * @throws TaskException
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
     * @return String representation of Event when displayed on the terminal
     */
    @Override
    public String toString() {
        String startString = this.start.format(Task.TO_STRING_FORMATTER);
        String endString = this.end.format(Task.TO_STRING_FORMATTER);
        return "[E]" + super.toString() + " (from: " + startString + " to: " + endString + ")";
    }

    /**
     * Create String representation of Event task to be saved.
     * @return String representation of the Event task ready to be saved into a .txt file.
     * Return String is of the form: E | 1 | project meeting | Aug 6th 2pm | 4pm
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;
        String startString = this.start.format(Task.FORMATTER);
        String endString = this.end.format(Task.FORMATTER);
        return "E | " + isDone + " | " + super.description + " | " + startString + " | " + endString;
    }

    /**
     * Helper method to check that Event start date is before Event end date
     * @param start start date of type LocalDateTime
     * @param end end date of type LocalDateTime
     * @throws TaskException
     */
    private void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) throws TaskException {
        if (start.isAfter(end)) {
            throw new TaskException("Start date cannot be later than end date!");
        }
    }
}
