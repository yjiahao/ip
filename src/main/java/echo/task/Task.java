package echo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import echo.exception.TaskException;

public abstract class Task {
    // formatter for displaying the string representation only
    protected static final DateTimeFormatter TO_STRING_FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy HHmm");
    // formatter for parsing and saving the deadline
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     *
     * @param description the description of the task
     */
    public Task(String description) throws TaskException {
        if (description.equals("")) {
            throw new TaskException("Task description cannot be empty!");
        }

        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    private String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }

    protected LocalDateTime parseDate(String date) throws TaskException {
        try {
            return LocalDateTime.parse(date, Task.FORMATTER);
        } catch (DateTimeParseException e) {
            throw new TaskException("Date is in the wrong format! Must be in yyyy-mm-dd HHmm");
        }
    }

    public abstract String saveRepresentation();
}
