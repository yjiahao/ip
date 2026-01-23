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
     * Constructor for Task
     * @param description the description of the task
     * @throws TaskException
     */
    public Task(String description) throws TaskException {
        if (description.equals("")) {
            throw new TaskException("Task description cannot be empty!");
        }

        this.description = description;
        this.isDone = false;
    }

    /**
     * Mark a task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Mark a task as undone.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Helper method to get status on whether Task is marked or unmarked
     * @return String of "X" if task is marked, else " " if unmarked
     */
    private String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns a String representation of the Task
     * @return String of Task for display in the terminal
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }

    /**
     * Helper method for child classes to parse all dates in the form of String
     * @param date date in String format
     * @return the parsed date as a LocalDateTime object
     * @throws TaskException
     */
    protected LocalDateTime parseDate(String date) throws TaskException {
        try {
            return LocalDateTime.parse(date, Task.FORMATTER);
        } catch (DateTimeParseException e) {
            throw new TaskException("Date is in the wrong format! Must be in yyyy-mm-dd HHmm");
        }
    }

    /**
     * Check if Task description contains a keyword.
     * @param keyword Keyword to search for in Task description.
     * @return true if Task description contains keyword, else false.
     */
    public boolean descriptionContains(String keyword) {
        return this.description.contains(keyword);
    }

    public abstract String saveRepresentation();
}
