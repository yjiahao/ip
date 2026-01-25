package echo.task;

import echo.exception.TaskException;

public class ToDo extends Task {
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
     * Displays Todo class in String form
     *
     * @return String representation of Todo when displayed on the terminal
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Create String representation of ToDo task.
     * Return String is of the form: T | 1 | read book
     *
     * @return String representation of the ToDo task ready to be saved into a .txt file.
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;
        return "T | " + isDone + " | " + super.description;
    }
}
