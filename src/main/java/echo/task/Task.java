package echo.task;

import java.time.LocalDateTime;

import echo.exception.TaskException;

/**
 * Represents an abstract task in the Echo application.
 * This is the base class for all task types (Todo, Deadline, Event).
 * Each task has a description and a completion status.
 *
 * Subclasses must implement the {@link #saveRepresentation()} method to define
 * how the task should be saved to persistent storage.
 */
public abstract class Task {

    protected static final String LOADING_SPLIT_PATTERN = " \\| ";
    protected static final String SEPARATOR = " | ";
    // marker in text file that are expected to identify task is done or not
    protected static final String MARKER_IS_DONE = "1";

    // index of task information after splitting by pattern
    protected static final int INDEX_TASK_TYPE = 0;
    protected static final int INDEX_IS_DONE = 1;
    protected static final int INDEX_DESCRIPTION = 2;

    private static final String ERROR_MESSAGE_TASK_NULL = "Task description cannot be null!";
    private static final String ERROR_MESSAGE_TASK_DESCRIPTION_EMPTY = "Task description cannot be empty!";

    protected String description;
    protected boolean isDone;

    /**
     * Initializes a Task object
     *
     * @param description the description of the Task
     * @throws TaskException if Task description is empty
     */
    public Task(String description) throws TaskException {
        assert description != null : Task.ERROR_MESSAGE_TASK_NULL;

        if (description.equals("")) {
            throw new TaskException(Task.ERROR_MESSAGE_TASK_DESCRIPTION_EMPTY);
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
     *
     * @return String of "X" if task is marked, else " " if unmarked
     */
    private String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns a String representation of the Task
     *
     * @return String of Task for display in the terminal
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }

    /**
     * Checks if Task description contains a keyword.
     *
     * @param keyword Keyword to search for in Task description.
     * @return true if Task description contains keyword, else false.
     */
    public boolean descriptionContains(String keyword) {
        return this.description.contains(keyword);
    }

    public static String getSplitPattern() {
        return Task.LOADING_SPLIT_PATTERN;
    }

    public static int getTaskTypeIndex() {
        return Task.INDEX_TASK_TYPE;
    }

    public abstract String saveRepresentation();

    /**
     * Checks if this Task has a scheduling conflict with another Task.
     *
     * @param task Task to be checked for any conflicts
     * @return true if there are scheduling conflicts, otherwise false
     */
    public abstract boolean hasSchedulingConflict(Task task);

    /**
     * Checks if a Task has a scheduling conflict with a Deadline.
     *
     * @param deadline Deadline to be checked for any scheduling conflicts
     * @return true if there are scheduling conflicts, otherwise false
     */
    protected abstract boolean hasSchedulingConflictWithDeadline(Deadline deadline);

    /**
     * Checks if a Task has a scheduling conflict with a Event.
     *
     * @param event Event to be checked for any scheduling conflicts
     * @return true if there are scheduling conflicts, otherwise false
     */
    protected abstract boolean hasSchedulingConflictWithEvent(Event event);

    /**
     * Checks if a Task has a scheduling conflict with a ToDo.
     *
     * @param todo ToDo to be checked for any scheduling conflicts
     * @return true if there are scheduling conflicts, otherwise false
     */
    protected boolean hasSchedulingConflictWithToDo(ToDo todo) {
        // always false since ToDo has no date associated with it
        return false;
    }
}
