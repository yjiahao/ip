package echo.task;

import java.time.LocalDateTime;

import echo.exception.TaskException;

public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Initializes a new Deadline object.
     * @param description description of the task.
     * @param by when to complete the task by (deadline).
     */
    public Deadline(String description, String by) throws TaskException {
        super(description);
        this.by = super.parseDate(by);
    }

    /**
     * Displays Deadline class in String form
     * @return String representation of Deadline when displayed on the terminal
     */
    @Override
    public String toString() {
        String dateString = this.by.format(Task.TO_STRING_FORMATTER);
        return "[D]" + super.toString() + " (by: " + dateString + ")";
    }

    /**
     * Create String representation of Deadline task for saving.
     * @return String representation of the Deadline task ready to be saved into a .txt file.
     * Return String is of the form: D | 0 | return book | June 6th
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;
        // format the date into a string format that the formatter expects
        String dateString = this.by.format(Task.FORMATTER);
        return "D | " + isDone + " | " + super.description + " | " + dateString;
    }
}
