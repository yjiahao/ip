package echo.task;

import java.time.LocalDateTime;

import echo.exception.TaskException;

/**
 * Represents a deadline task with a specific due date/time.
 * A deadline is a task that needs to be completed by a certain date and time.
 *
 * Deadlines extend TimedTask and use date/time formatting for display and storage.
 */
public class Deadline extends TimedTask {
    // markers in the text file that are expected to identify each task type
    private static final String MARKER_DEADLINE = "D";

    // index of task information after splitting by pattern
    private static final int INDEX_DEADLINE_BY = 3;

    private static final String ERROR_MESSAGE_INCORRECT_FORMAT =
        "Failed to parse Deadline! Contains lesser arguments than expected!";
    private static final String ERROR_MESSAGE_BY_NULL = "/by date is null";

    private static final int EXPECTED_ARGS_LENGTH = 4;

    private LocalDateTime by;

    /**
     * Initializes a new Deadline object.
     *
     * @param description description of the task.
     * @param by when to complete the task by (deadline).
     */
    public Deadline(String description, String by) throws TaskException {
        super(description);

        assert by != null : Deadline.ERROR_MESSAGE_BY_NULL;

        this.by = super.parseDate(by);
    }

    /**
     * Displays Deadline class in String form
     *
     * @return String representation of Deadline when displayed on the terminal
     */
    @Override
    public String toString() {
        String dateString = this.by.format(TimedTask.FORMATTER_TO_STRING);
        return "[" + Deadline.MARKER_DEADLINE + "]" + super.toString() + " (by: " + dateString + ")";
    }

    /**
     * Create String representation of Deadline task for saving.
     * Return String is of the form: D | 0 | return book | June 6th
     *
     * @return String representation of the Deadline task ready to be saved into a .txt file.
     */
    @Override
    public String saveRepresentation() {
        String isDone = super.isDone ? Task.MARKER_IS_DONE : Task.MARKER_IS_NOT_DONE;
        // format the date into a string format that the formatter expects
        String dateString = this.by.format(TimedTask.FORMATTER_TO_SAVE);
        return Deadline.MARKER_DEADLINE + Task.SEPARATOR + isDone
            + Task.SEPARATOR + super.description + Task.SEPARATOR + dateString;
    }


    /**
     * Creates a new Deadline object from the string representation
     * Factory method for creating the Deadline object
     *
     * A line for Deadline is expected to be of the form:
     * Deadline: D | 0 | return book | 2026-01-25 1000
     *
     * @param line A line from the file where the Tasks are getting loaded from
     * @return A Deadline object after parsing the String
     * @throws TaskException If the line is wrongly formatted to what Deadline expects
     */
    public static Deadline fromSaveFormat(String line) throws TaskException {
        String[] args = line.split(Task.LOADING_SPLIT_PATTERN);
        Deadline.checkDeadlineValid(args);
        // create new Deadline
        Deadline deadline = new Deadline(args[Task.INDEX_DESCRIPTION], args[Deadline.INDEX_DEADLINE_BY]);
        if (args[Task.INDEX_IS_DONE].equals(Task.MARKER_IS_DONE)) {
            deadline.markAsDone();
        }
        return deadline;
    }

    /**
     * Gets the marker with the saved representation for the Deadline task
     *
     * @return marker representing the Deadline task in the saved state
     */
    public static String getMarker() {
        return Deadline.MARKER_DEADLINE;
    }

    @Override
    public boolean hasSchedulingConflict(Task other) {
        return other.hasSchedulingConflictWithDeadline(this);
    }

    @Override
    protected boolean hasSchedulingConflictWithDeadline(Deadline deadline) {
        return this.by.isEqual(deadline.by);
    }

    @Override
    protected boolean hasSchedulingConflictWithEvent(Event event) {
        return event.isWithinEventInterval(this.by);
    }

    private static void checkDeadlineValid(String[] args) throws TaskException {
        if (args.length < Deadline.EXPECTED_ARGS_LENGTH) {
            throw new TaskException(
                Deadline.ERROR_MESSAGE_INCORRECT_FORMAT);
        }
    }
}
