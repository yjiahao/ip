package echo.task;

import java.util.ArrayList;

/**
 * Represents a result after adding a Task.
 */
public class AddTaskResult {
    private final Task task;
    private final boolean hasConflict;
    private final ArrayList<Task> conflictingTasks;

    /**
     * Constructs a TaskResult with the given task and conflict status.
     *
     * @param task the task associated with this result
     * @param hasConflict whether this task result has a conflict
     */
    public AddTaskResult(Task task, boolean hasConflict, ArrayList<Task> conflictingTasks) {
        this.task = task;
        this.hasConflict = hasConflict;
        this.conflictingTasks = conflictingTasks;
    }

    public Task getTask() {
        return this.task;
    }

    public boolean hasConflict() {
        return this.hasConflict;
    }

    public ArrayList<Task> getConflictingTasks() {
        return this.conflictingTasks;
    }
}
