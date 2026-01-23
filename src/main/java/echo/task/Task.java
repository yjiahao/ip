package echo.task;

import echo.exception.TaskException;

public abstract class Task {

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
