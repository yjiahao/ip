package main.java;

public class Event extends Task {
    protected String start;
    protected String end;

    /**
     * Initializes an Event object.
     * @param description description of the Event.
     * @param start Event start time.
     * @param end Event end time.
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
    }

    /**
     * Create String representation of Event task.
     * @return String representation of the Event task ready to be saved into a .txt file.
     * Return String is of the form: E | 1 | project meeting | Aug 6th 2pm | 4pm
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;
        return "D | " + isDone + " | " + super.description + " | " + this.start + " | " + this.end;
    }
}
