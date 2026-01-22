package main.java;

public class Deadline extends Task {
    protected String by;

    /**
     * Initializes a new Deadline object.
     * @param description description of the task.
     * @param by when to complete the task by (deadline).
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    /**
     * Create String representation of Deadline task.
     * @return String representation of the Deadline task ready to be saved into a .txt file.
     * Return String is of the form: D | 0 | return book | June 6th
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;
        return "D | " + isDone + " | " + super.description + " | " + this.by;
    }
}
