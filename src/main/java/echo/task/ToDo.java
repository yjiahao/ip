package main.java.echo.task;

public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Create String representation of ToDo task.
     * @return String representation of the ToDo task ready to be saved into a .txt file.
     * Return String is of the form: T | 1 | read book
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;
        return "T | " + isDone + " | " + super.description;
    }
}
