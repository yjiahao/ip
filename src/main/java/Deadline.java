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
}
