package echo.parser;

import java.util.ArrayList;
import java.util.Arrays;

import echo.command.Command;
import echo.exception.ParsingException;

/**
 * Parses user input commands and extracts relevant information for task operations.
 * This class handles the interpretation of user messages, validating command syntax,
 * and extracting task descriptions and arguments for various task types (Todo, Deadline, Event).
 *
 * <p>The parser performs validation at two levels: first during command identification in
 * {@link #parseCommand(String)}, and again when extracting specific arguments in the
 * respective parse methods.</p>
 */
public class InstructionParser {

    private static final String ERROR_MESSAGE_TODO = "The todo description cannot be empty leh...";

    private static final String ERROR_MESSAGE_DEADLINE_MISSING_DEADLINE_AND_BY =
        "got no deadline description and by when you should complete it...";
    private static final String ERROR_MESSAGE_DEADLINE_MISSING_DEADLINE = "got no deadline description...";
    private static final String ERROR_MESSAGE_DEADLINE_MISSING_BY =
        "need to indicate when you need to complete the deadline by!";

    private static final String ERROR_MESSAGE_EVENT_MISSING_DESCRIPTION = "why got no event description one...";

    private static final String ERRROR_MESSAGE_MARK_NO_NUMBER = "mark requires a task number!";
    private static final String ERROR_MESSAGE_MARK_INVALID_NUMBER = "mark needs a valid task number!";

    private static final String ERROR_MESSAGE_UNMARK_NO_NUMBER = "unmark requires a task number!";
    private static final String ERROR_MESSAGE_UNMARK_INVALID_NUMBER = "unmark needs a valid task number!";

    private static final String ERROR_MESSAGE_DELETE_NO_NUMBER = "delete requires a task number!";
    private static final String ERROR_MESSAGE_DELETE_INVALID_NUMBER = "delete needs a valid task number!";

    private static final String ERROR_MESSAGE_FIND_NO_KEYWORD = "find requires a keyword!";

    private static final String ERROR_MESSAGE_UNKNOWN_COMMAND =
        "Sorry what does that mean ah? I never see %s before...";

    public InstructionParser() {

    }

    /**
     * Parses the given command string and returns the corresponding Command enum.
     *
     * @param command the command string to parse
     * @return the Command enum corresponding to the command
     * @throws IllegalArgumentException if the command is unknown or unsupported
     */
    public Command parseCommand(String command) throws ParsingException {
        // trim command
        String trimmedCommand = command.trim();
        // split into at most 2 elements
        String[] parts = trimmedCommand.split(" ", 2);
        // get the keyword from user which is the first element
        String keyword = parts[0].toLowerCase();

        // TODO: abstract each case out even more into its own validation checks
        switch (keyword) {
        case "todo":
            checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_TODO);
            return Command.TODO;
        case "deadline":
            checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_DEADLINE_AND_BY);
            // catch no description of deadline
            if (parts[1].startsWith("/by")) {
                throw new ParsingException(InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_DEADLINE);
            }
            // catch no /by ... for deadline
            String[] deadlineParts = parts[1].split("/by ");
            checkLengthMoreThanEqualTwo(deadlineParts, InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_BY);
            return Command.DEADLINE;
        case "list":
            return Command.LIST;
        case "event":
            checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_EVENT_MISSING_DESCRIPTION);
            return Command.EVENT;
        case "mark":
            checkLengthMoreThanEqualTwo(parts, InstructionParser.ERRROR_MESSAGE_MARK_NO_NUMBER);
            // catch cases like "mark string" instead of "mark 1"
            checkValidNumber(parts, InstructionParser.ERROR_MESSAGE_MARK_INVALID_NUMBER);
            return Command.MARK;
        case "unmark":
            checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_UNMARK_NO_NUMBER);
            // catch cases like "unmark string" instead of "unmark 1"
            checkValidNumber(parts, InstructionParser.ERROR_MESSAGE_UNMARK_INVALID_NUMBER);
            return Command.UNMARK;
        case "delete":
            checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_DELETE_NO_NUMBER);
            // catch cases like "delete string" instead of "delete 1"
            checkValidNumber(parts, InstructionParser.ERROR_MESSAGE_DELETE_INVALID_NUMBER);
            return Command.DELETE;
        case "bye":
            return Command.BYE;
        case "find":
            checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_FIND_NO_KEYWORD);
            return Command.FIND;
        default:
            // unknown command expected here
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_UNKNOWN_COMMAND.formatted(keyword));
        }
    }

    /**
     * Parses the user arguments for marking or unmarking a Task
     *
     * @param userMessage String of raw user message
     * @return int representing the Task number that user wants to mark or unmark
     */
    public int parseMarkUnmarkArgs(String userMessage) {
        String[] markParts = userMessage.split(" ");
        int markTaskNumber = Integer.parseInt(markParts[1]);
        return markTaskNumber;
    }

    /**
     * Parses user arguments for deleting a Task
     *
     * @param userMessage String of raw user message
     * @return int representing Task number that user wants to delete
     */
    public int parseDeleteArgs(String userMessage) {
        String[] deleteParts = userMessage.split(" ");
        int deleteTaskNumber = Integer.parseInt(deleteParts[1]);
        return deleteTaskNumber;
    }

    /**
     * Parses the description part of the Todo Task.
     *
     * @param userMessage String of raw user message
     * @return String representing the description of the Todo Task user specified
     * @throws ParsingException If user message is an empty String or user did not specify description
     */
    public String parseTodoDescription(String userMessage) throws ParsingException {
        if (userMessage.length() == 0) {
            throw new ParsingException("User message cannot be empty!");
        }

        String[] todoParts = userMessage.split(" ", 2);
        String description = todoParts[1];

        if (description.length() == 0) {
            throw new ParsingException("No todo description!");
        }

        return description;
    }

    /**
     * Parses the arguments of the Todo Task.
     *
     * @param userMessage String of raw user message
     * @return Currently returns an empty ArrayList because Todo has no arguments
     */
    public ArrayList<String> parseTodoArgs(String userMessage) {
        return new ArrayList<>();
    }

    /**
     * Parses the description of the Deadline Task.
     *
     * @param userMessage String of raw user message
     * @return Description of Deadline Task
     * @throws ParsingException If userMessage is an empty String, or there is no description for deadline
     */
    public String parseDeadlineDescription(String userMessage) throws ParsingException {
        if (userMessage.length() == 0) {
            throw new ParsingException("User message cannot be empty!");
        }

        String deadlineDetails = userMessage.substring(9);
        String[] deadlineParts = deadlineDetails.split("/by", 2);
        String deadlineDescription = deadlineParts[0].trim();

        if (deadlineDescription.length() == 0) {
            throw new ParsingException("No deadline description!");
        }

        return deadlineDescription;
    }

    /**
     * Parses the arguments of the Deadline Task provided by user.
     *
     * @param userMessage String of raw user message
     * @return Arguments of the Deadline Task as an ArrayList of String
     * @throws ParsingException If userMessage is an empty String or there is no
     *     /by in the userMessage, or more than one /by in the userMessage
     */
    public ArrayList<String> parseDeadlineArgs(String userMessage) throws ParsingException {
        if (userMessage.length() == 0) {
            throw new ParsingException("User message cannot be empty!");
        }

        String deadlineDetails = userMessage.substring(9);
        String[] deadlineParts = deadlineDetails.split("/by", 2);

        if (deadlineParts.length < 2) {
            throw new ParsingException("Did you forget to specify /by for the deadline?");
        } else if (deadlineParts[1].trim().contains("/by ")) {
            throw new ParsingException("Did you specify more than one /by for the deadline?");
        }

        return new ArrayList<>(Arrays.asList(deadlineParts[1].trim()));
    }

    /**
     * Parses the description of the Event Task.
     *
     * @param userMessage String of raw user message
     * @return Description of Event Task
     * @throws ParsingException If userMessage is an empty String, or there is no Event description
     */
    public String parseEventDescription(String userMessage) throws ParsingException {
        if (userMessage.length() == 0) {
            throw new ParsingException("User message cannot be empty!");
        }

        String eventDetails = userMessage.substring(6).trim();
        String[] fromSplit = eventDetails.split("/from", 2);
        String eventDescription = fromSplit[0].trim();

        if (eventDescription.length() == 0) {
            throw new ParsingException("No event description!");
        }

        return eventDescription;
    }

    /**
     * Parses the arguments of the Event Task.
     *
     * @param userMessage String of raw user message
     * @return Arguments of Event Task as an ArrayList of String
     * @throws ParsingException If userMessage is an empty String, or does not contain /from or does not contain /to
     */
    public ArrayList<String> parseEventArgs(String userMessage) throws ParsingException {
        if (userMessage.length() == 0) {
            throw new ParsingException("User message cannot be empty!");
        }

        String eventDetails = userMessage.substring(6).trim();

        if (!userMessage.contains("/from ")) {
            throw new ParsingException("Did you forget to specify /from for the event?");
        }
        if (!userMessage.contains("/to")) {
            throw new ParsingException("Did you forget to specify /to for the event?");
        }

        String[] fromSplit = eventDetails.split("/from", 2);
        String[] toSplit = fromSplit[1].split("/to", 2);
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();
        ArrayList<String> eventArgs = new ArrayList<>(Arrays.asList(from, to));
        return eventArgs;
    }

    /**
     * Parses the keyword to search for in the user's message when user requests to find a keyword.
     *
     * @param userMessage String of raw user message.
     * @return String of keyword to search for in the tasks.
     * @throws ParsingException If userMessage did not specify a keyword
     */
    public String parseFindKeyword(String userMessage) throws ParsingException {
        String[] findParts = userMessage.split(" ", 2);
        if (findParts.length < 2) {
            throw new ParsingException("You did not specify a keyword to look for!");
        }
        String keyword = findParts[1];
        return keyword;
    }

    private void checkValidNumber(String[] parts, String errorMessage) throws ParsingException {
        try {
            Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new ParsingException(errorMessage);
        }
    }

    private void checkLengthMoreThanEqualTwo(String[] parts, String errorMessage) throws ParsingException {
        if (parts.length < 2) {
            throw new ParsingException(errorMessage);
        }
    }

}
// NOTE: Exceptions are checked twice, once in parseCommand and once in parse___Args()
