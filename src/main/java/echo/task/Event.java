package echo.task;

import java.time.LocalDateTime;

import echo.exception.TaskException;

/**
 * Represents an event task with a start and end date/time.
 * An event is a task that occurs during a specific time period, with both
 * a starting time and an ending time.
 *
 * Events are validated to ensure the start time is before the end time.
 */
public class Event extends TimedTask {

    // markers in the text file that are expected to identify each task type
    private static final String MARKER_EVENT = "E";

    // index of task information after splitting by pattern
    private static final int INDEX_EVENT_FROM = 3;
    private static final int INDEX_EVENT_TO = 4;

    private static final String ERROR_MESSAGE_INCORRECT_FORMAT =
        "Failed to parse Event! Contains lesser arguments than expected!";
    private static final String ERROR_MESSAGE_START_LATER_THAN_END = 
        "Start date cannot be later than end date!";
    private static final String ERROR_MESSAGE_START_NULL = "Event start is null";
    private static final String ERROR_MESSAGE_END_NULL = "Event end is null";


    protected LocalDateTime start;
    protected LocalDateTime end;

    /**
     * Initializes an Event object.
     *
     * @param description description of the Event.
     * @param start Event start time.
     * @param end Event end time.
     * @throws TaskException if start date is later than end date.
     */
    public Event(String description, String start, String end) throws TaskException {
        super(description);

        assert start != null : Event.ERROR_MESSAGE_START_NULL;
        assert end != null : Event.ERROR_MESSAGE_END_NULL;

        LocalDateTime startDate = super.parseDate(start);
        LocalDateTime endDate = super.parseDate(end);
        // validity checks first
        this.checkStartBeforeEnd(startDate, endDate);

        this.start = startDate;
        this.end = endDate;
    }

    /**
     * Displays Event class in String form
     *
     * @return String representation of Event when displayed on the terminal
     */
    @Override
    public String toString() {
        String startString = this.start.format(TimedTask.FORMATTER_TO_STRING);
        String endString = this.end.format(TimedTask.FORMATTER_TO_STRING);
        return "[" + Event.MARKER_EVENT + "]" + super.toString() + " (from: " + startString + " to: " + endString + ")";
    }

    /**
     * Creates String representation of Event task to be saved.
     * Return String is of the form: E | 1 | project meeting | Aug 6th 2pm | 4pm
     *
     * @return String representation of the Event task ready to be saved into a .txt file.
     */
    @Override
    public String saveRepresentation() {
        int isDone = super.isDone ? 1 : 0;

        String startString = this.start.format(TimedTask.FORMATTER_TO_SAVE);
        String endString = this.end.format(TimedTask.FORMATTER_TO_SAVE);

        return Event.MARKER_EVENT + Task.SEPARATOR + isDone + Task.SEPARATOR
            + super.description + Task.SEPARATOR + startString + Task.SEPARATOR + endString;
    }

    /**
     * Helper method to check that Event start date is before Event end date
     *
     * @param start start date of type LocalDateTime
     * @param end end date of type LocalDateTime
     * @throws TaskException if start later than end
     */
    private void checkStartBeforeEnd(LocalDateTime start, LocalDateTime end) throws TaskException {
        if (start.isAfter(end)) {
            throw new TaskException(Event.ERROR_MESSAGE_START_LATER_THAN_END);
        }
    }

    /**
     * Creates a new Event object from the string representation
     * Factory method for creating the Event object
     *
     * A line for Event is expected to be of the form: 
     * Event: E | 1 | project meeting | 2026-01-27 1200 | 2026-01-27 1500
     * 
     * @param line A line from the file where the Tasks are getting loaded from
     * @return A Event object after parsing the String
     * @throws TaskException If the line is wrongly formatted to what Event expects
     */
    public static Event fromSaveFormat(String line) throws TaskException {
        // get arguments to parse
        String[] args = line.split(Task.LOADING_SPLIT_PATTERN);
        
        Event.checkEventValid(args);

        // create new Event
        Event event = new Event(args[Task.INDEX_DESCRIPTION],
            args[Event.INDEX_EVENT_FROM], args[Event.INDEX_EVENT_TO]);
        if (args[Task.INDEX_IS_DONE].equals(Task.MARKER_IS_DONE)) {
            event.markAsDone();
        }
        return event;
    }

    public static String getMarker() {
        return Event.MARKER_EVENT;
    }

    private static void checkEventValid(String[] args) throws TaskException {
        if (args.length < 5) {
            throw new TaskException(
                Event.ERROR_MESSAGE_INCORRECT_FORMAT);
        }
    }
}
