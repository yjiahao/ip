package echo.task;

import echo.exception.TaskException;

/**
 * Represents a Todo task without any date/time attached.
 * A Todo is a simple task that only has a description and completion status,
 * with no specific deadline or time period.
 *
 * Todos are the simplest form of tasks in the Echo application.
 */
public class ToDo extends Task {
    // markers in the text file that are expected to identify each task type
    private static final String MARKER_TODO = "T";

    private static final String ERROR_MESSAGE_INCORRECT_FORMAT =
        "Failed to parse ToDo! Contains lesser arguments than expected!";

    private static final int EXPECTED_ARGS_LENGTH = 3;

    /**
     * Constructor for Todo.
     *
     * @param description Description of Todo
     * @throws TaskException If task description is empty
     */
    public ToDo(String description) throws TaskException {
        super(description);
    }

    /**
     * Creates a new ToDo object from the string representation
     * Factory method for creating the ToDo object
     *
     * A line for ToDo is expected to be of the form:
     * ToDo: T | 1 | read book
     *
     * @param line A line from the file where the Tasks are getting loaded from
     * @return A ToDo object after parsing the String
     * @throws TaskException If the line is wrongly formatted to what ToDo expects
     */
    public static ToDo fromSaveFormat(String line) throws TaskException {
        // get arguments to parse
        String[] args = line.split(Task.LOADING_SPLIT_PATTERN);
        // check valid arguments for saving format
        ToDo.checkTodoValid(args);
        // create new ToDo
        ToDo todo = new ToDo(args[Task.INDEX_DESCRIPTION]);
        if (args[Task.INDEX_IS_DONE].equals(Task.MARKER_IS_DONE)) {
            todo.markAsDone();
        }
        return todo;
    }

    /**
     * Displays Todo class in String form
     *
     * @return String representation of Todo when displayed on the terminal
     */
    @Override
    public String toString() {
        return "[" + ToDo.MARKER_TODO + "]" + super.toString();
    }

    /**
     * Gets the marker with the saved representation for the ToDo task
     *
     * @return marker representing the ToDo task in the saved state
     */
    public static String getMarker() {
        return ToDo.MARKER_TODO;
    }

    /**
     * Create String representation of ToDo task.
     * Return String is of the form: T | 1 | read book
     *
     * @return String representation of the ToDo task ready to be saved into a .txt file.
     */
    @Override
    public String saveRepresentation() {
        String isDone = super.isDone ? Task.MARKER_IS_DONE : Task.MARKER_IS_NOT_DONE;
        return ToDo.MARKER_TODO + Task.SEPARATOR + isDone + Task.SEPARATOR + super.description;
    }

    @Override
    public boolean hasSchedulingConflict(Task task) {
        return task.hasSchedulingConflictWithToDo(this);
    }

    @Override
    protected boolean hasSchedulingConflictWithDeadline(Deadline deadline) {
        return false;
    }

    @Override
    protected boolean hasSchedulingConflictWithEvent(Event event) {
        return false;
    }

    private static void checkTodoValid(String[] args) throws TaskException {
        if (args.length < ToDo.EXPECTED_ARGS_LENGTH) {
            throw new TaskException(
                ToDo.ERROR_MESSAGE_INCORRECT_FORMAT);
        }
    }
}
