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
     */
    public Event(String description, String start, String end) throws TaskException {
        super(description);
        this.start = super.parseDate(start);
        this.end = super.parseDate(end);
    }

    @Override
    public String toString() {
        String startString = this.start.format(Task.TO_STRING_FORMATTER);
        String endString = this.end.format(Task.TO_STRING_FORMATTER);
        return "[E]" + super.toString() + " (from: " + startString + " to: " + endString + ")";
    }

    /**
     * Create String representation of Event task.
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
}
