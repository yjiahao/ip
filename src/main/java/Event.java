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
}
