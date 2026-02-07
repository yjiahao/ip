package echo.task;

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

    protected String description;
    protected boolean isDone;

    /**
     * Initializes a Task object
     *
     * @param description the description of the Task
     * @throws TaskException if Task description is empty
     */
    public Task(String description) throws TaskException {
        assert description != null : "Task description cannot be null!";

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

    public abstract String saveRepresentation();
}
