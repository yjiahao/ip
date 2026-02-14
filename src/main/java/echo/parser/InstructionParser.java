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
    private static final String STRING_EMPTY = "";
    private static final String STRING_SPACE = " ";
    private static final String STRING_DEADLINE = "deadline";
    private static final String STRING_TODO = "todo";
    private static final String STRING_EVENT = "event";
    private static final String STRING_UNMARK = "unmark";
    private static final String STRING_MARK = "mark";
    private static final String STRING_DELETE = "delete";
    private static final String STRING_BYE = "bye";
    private static final String STRING_LIST = "list";
    private static final String STRING_FIND = "find";

    private static final String INPUT_DELIMITER = " ";
    private static final String INPUT_DEADLINE_BY = "/by";
    private static final String INPUT_EVENT_FROM = "/from";
    private static final String INPUT_EVENT_TO = "/to";

    private static final String ERROR_MESSAGE_TODO_MISSING_DESCRIPTION =
        "The todo description cannot be empty leh...";

    private static final String ERROR_MESSAGE_DEADLINE_MISSING_DESCRIPTION_AND_BY =
        "got no deadline description and by when you should complete it...";
    private static final String ERROR_MESSAGE_DEADLINE_MISSING_DESCRIPTION = "got no deadline description...";
    private static final String ERROR_MESSAGE_DEADLINE_MISSING_BY =
        "need to indicate when you need to complete the deadline by!";
    private static final String ERROR_MESSAGE_DEADLINE_MORE_THAN_ONE_BY =
        "Did you specify more than one /by for the deadline?";

    private static final String ERROR_MESSAGE_EVENT_MISSING_DESCRIPTION = "why got no event description one...";
    private static final String ERROR_MESSAGE_EVENT_MISSING_FROM = "Did you forget to specify /from for the event?";
    private static final String ERROR_MESSAGE_EVENT_MISSING_TO = "Did you forget to specify /to for the event?";

    private static final String ERRROR_MESSAGE_MARK_NO_NUMBER = "mark requires a task number!";
    private static final String ERROR_MESSAGE_MARK_INVALID_NUMBER = "mark needs a valid task number!";

    private static final String ERROR_MESSAGE_UNMARK_NO_NUMBER = "unmark requires a task number!";
    private static final String ERROR_MESSAGE_UNMARK_INVALID_NUMBER = "unmark needs a valid task number!";

    private static final String ERROR_MESSAGE_DELETE_NO_NUMBER = "delete requires a task number!";
    private static final String ERROR_MESSAGE_DELETE_INVALID_NUMBER = "delete needs a valid task number!";

    private static final String ERROR_MESSAGE_FIND_NO_KEYWORD = "find requires a keyword!";

    private static final String ERROR_MESSAGE_UNKNOWN_COMMAND =
        "Sorry what does that mean ah? I never see %s before...";

    private static final String ERROR_MESSAGE_EMPTY_USER_MESSAGE = "User message cannot be empty!";

    private static final int INDEX_COMMAND_KEYWORD = 0;

    private static final int INDEX_TASK_NUMBER = 1;
    private static final int INDEX_TODO_DESCRIPTION = 1;

    private static final int INDEX_DEADLINE_DESCRIPTION = 0;
    // to remove "deadline "
    private static final int SUBSTRING_DEADLINE_MESSAGE_BY = 9;
    private static final int INDEX_DEADLINE_BY = 1;

    private static final int INDEX_EVENT_DESCRIPTION = 0;
    private static final int SUBSTRING_EVENT_MESSAGE_BY = 6;
    private static final int INDEX_EVENT_FROM = 0;
    private static final int INDEX_EVENT_TO = 1;
    private static final int INDEX_EVENT_TEMP_SPLIT_TO = 1;

    private static final int INDEX_FIND_KEYWORD = 1;

    private static final int INDEX_MARK_UNMARK_NUMBER = 1;

    private static final int ZERO = 0;
    private static final int TWO = 2;

    /**
     * Parses the given command string and returns the corresponding Command enum.
     *
     * @param command the command string to parse
     * @return the Command enum corresponding to the command
     * @throws IllegalArgumentException if the command is unknown or unsupported
     */
    public Command parseCommand(String command) throws ParsingException {
        // trim any trailing or leading spaces in user's command
        String trimmedCommand = command.trim();

        assert !(trimmedCommand.equals(InstructionParser.STRING_EMPTY));

        // split into at most 2 elements
        String[] parts = trimmedCommand.split(InstructionParser.STRING_SPACE, InstructionParser.TWO);
        String keyword = this.getKeyword(parts);

        switch (keyword) {
        case InstructionParser.STRING_TODO:
            validateTodo(parts);
            return Command.TODO;
        case InstructionParser.STRING_DEADLINE:
            validateDeadline(parts);
            return Command.DEADLINE;
        case InstructionParser.STRING_LIST:
            return Command.LIST;
        case InstructionParser.STRING_EVENT:
            validateEvent(parts);
            return Command.EVENT;
        case InstructionParser.STRING_MARK:
            validateMark(parts);
            return Command.MARK;
        case InstructionParser.STRING_UNMARK:
            validateUnmark(parts);
            return Command.UNMARK;
        case InstructionParser.STRING_DELETE:
            validateDelete(parts);
            return Command.DELETE;
        case InstructionParser.STRING_BYE:
            return Command.BYE;
        case InstructionParser.STRING_FIND:
            validateFind(parts);
            return Command.FIND;
        default:
            // unknown command expected here
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_UNKNOWN_COMMAND.formatted(keyword));
        }
    }

    private String getKeyword(String[] parts) {
        String keyword = parts[InstructionParser.INDEX_COMMAND_KEYWORD].toLowerCase();
        return keyword;
    }

    /**
     * Parses the user arguments for unmarking or marking a Task
     *
     * @param userMessage String of raw user message
     * @return int representing the Task number that user wants to unmark or mark
     */
    public int parseMarkUnmarkArgs(String userMessage) {
        String[] parts = userMessage.split(InstructionParser.INPUT_DELIMITER);
        int taskNumber = Integer.parseInt(parts[InstructionParser.INDEX_TASK_NUMBER]);
        return taskNumber;
    }

    /**
     * Parses user arguments for deleting a Task
     *
     * @param userMessage String of raw user message
     * @return int representing Task number that user wants to delete
     */
    public int parseDeleteArgs(String userMessage) {
        assert userMessage.contains(InstructionParser.STRING_DELETE);

        String[] deleteParts = userMessage.split(InstructionParser.INPUT_DELIMITER);
        int deleteTaskNumber = Integer.parseInt(deleteParts[InstructionParser.INDEX_TASK_NUMBER]);
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
        assert userMessage.contains(InstructionParser.STRING_TODO);

        if (userMessage.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EMPTY_USER_MESSAGE);
        }

        String[] todoParts = userMessage.split(InstructionParser.INPUT_DELIMITER, InstructionParser.TWO);
        String description = todoParts[InstructionParser.INDEX_TODO_DESCRIPTION];

        if (description.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_TODO_MISSING_DESCRIPTION);
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
        // assume we are calling this method exclusively for todo only
        assert userMessage.contains(InstructionParser.STRING_TODO);

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
        assert userMessage.contains(InstructionParser.STRING_DEADLINE);

        if (userMessage.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EMPTY_USER_MESSAGE);
        }

        String deadlineDetails = userMessage.substring(InstructionParser.SUBSTRING_DEADLINE_MESSAGE_BY);
        String[] deadlineParts = deadlineDetails.split(InstructionParser.INPUT_DEADLINE_BY, InstructionParser.TWO);
        String deadlineDescription = deadlineParts[InstructionParser.INDEX_DEADLINE_DESCRIPTION].trim();

        if (deadlineDescription.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_DESCRIPTION);
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
        assert userMessage.contains(InstructionParser.STRING_DEADLINE);

        if (userMessage.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EMPTY_USER_MESSAGE);
        }

        String deadlineDetails = userMessage.substring(InstructionParser.SUBSTRING_DEADLINE_MESSAGE_BY);
        String[] deadlineParts = deadlineDetails.split(InstructionParser.INPUT_DEADLINE_BY, InstructionParser.TWO);

        if (deadlineParts.length < InstructionParser.TWO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_BY);
        }

        if (deadlineParts[InstructionParser.INDEX_DEADLINE_BY].trim().contains(InstructionParser.INPUT_DEADLINE_BY)) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_DEADLINE_MORE_THAN_ONE_BY);
        }

        return new ArrayList<>(Arrays.asList(deadlineParts[InstructionParser.INDEX_DEADLINE_BY].trim()));
    }

    /**
     * Parses the description of the Event Task.
     *
     * @param userMessage String of raw user message
     * @return Description of Event Task
     * @throws ParsingException If userMessage is an empty String, or there is no Event description
     */
    public String parseEventDescription(String userMessage) throws ParsingException {
        assert userMessage.contains(InstructionParser.STRING_EVENT);

        if (userMessage.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EMPTY_USER_MESSAGE);
        }

        String eventDetails = userMessage.substring(InstructionParser.SUBSTRING_EVENT_MESSAGE_BY).trim();
        String[] fromSplit = eventDetails.split(InstructionParser.INPUT_EVENT_FROM, InstructionParser.TWO);
        String eventDescription = fromSplit[InstructionParser.INDEX_EVENT_DESCRIPTION].trim();

        if (eventDescription.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EVENT_MISSING_DESCRIPTION);
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
        assert userMessage.contains(InstructionParser.STRING_EVENT);

        if (userMessage.length() == InstructionParser.ZERO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EMPTY_USER_MESSAGE);
        }
        if (!userMessage.contains(InstructionParser.INPUT_EVENT_FROM)) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EVENT_MISSING_FROM);
        }
        if (!userMessage.contains(InstructionParser.INPUT_EVENT_TO)) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_EVENT_MISSING_TO);
        }

        String eventDetails = userMessage.substring(InstructionParser.SUBSTRING_EVENT_MESSAGE_BY).trim();

        String[] fromSplit = eventDetails.split(InstructionParser.INPUT_EVENT_FROM, InstructionParser.TWO);
        String[] toSplit = fromSplit[InstructionParser.INDEX_EVENT_TEMP_SPLIT_TO]
            .split(InstructionParser.INPUT_EVENT_TO, InstructionParser.TWO);

        String from = toSplit[InstructionParser.INDEX_EVENT_FROM].trim();
        String to = toSplit[InstructionParser.INDEX_EVENT_TO].trim();

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
        assert userMessage.contains(InstructionParser.STRING_FIND);

        String[] findParts = userMessage.split(InstructionParser.INPUT_DELIMITER, InstructionParser.TWO);

        if (findParts.length < InstructionParser.TWO) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_FIND_NO_KEYWORD);
        }

        String keyword = findParts[InstructionParser.INDEX_FIND_KEYWORD];
        return keyword;
    }

    private void checkValidNumber(String[] parts, String errorMessage) throws ParsingException {
        try {
            Integer.parseInt(parts[InstructionParser.INDEX_MARK_UNMARK_NUMBER]);
        } catch (NumberFormatException e) {
            throw new ParsingException(errorMessage);
        }
    }

    private void checkLengthMoreThanEqualTwo(String[] parts, String errorMessage) throws ParsingException {
        if (parts.length < InstructionParser.TWO) {
            throw new ParsingException(errorMessage);
        }
    }

    private void validateFind(String[] parts) throws ParsingException {
        checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_FIND_NO_KEYWORD);
    }

    private void validateDelete(String[] parts) throws ParsingException {
        checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_DELETE_NO_NUMBER);
        // catch cases like "delete string" instead of "delete 1"
        checkValidNumber(parts, InstructionParser.ERROR_MESSAGE_DELETE_INVALID_NUMBER);
    }

    private void validateUnmark(String[] parts) throws ParsingException {
        checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_UNMARK_NO_NUMBER);
        // catch cases like "unmark string" instead of "unmark 1"
        checkValidNumber(parts, InstructionParser.ERROR_MESSAGE_UNMARK_INVALID_NUMBER);
    }

    private void validateMark(String[] parts) throws ParsingException {
        checkLengthMoreThanEqualTwo(parts, InstructionParser.ERRROR_MESSAGE_MARK_NO_NUMBER);
        // catch cases like "mark string" instead of "mark 1"
        checkValidNumber(parts, InstructionParser.ERROR_MESSAGE_MARK_INVALID_NUMBER);
    }

    private void validateEvent(String[] parts) throws ParsingException {
        checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_EVENT_MISSING_DESCRIPTION);
    }

    private void validateDeadline(String[] parts) throws ParsingException {
        checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_DESCRIPTION_AND_BY);
        // catch no description of deadline
        if (parts[InstructionParser.INDEX_DEADLINE_BY].startsWith(InstructionParser.INPUT_DEADLINE_BY)) {
            throw new ParsingException(InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_DESCRIPTION);
        }
        // catch no /by ... for deadline
        String[] deadlineParts = parts[InstructionParser.INDEX_DEADLINE_BY].split(InstructionParser.INPUT_DEADLINE_BY);
        checkLengthMoreThanEqualTwo(deadlineParts, InstructionParser.ERROR_MESSAGE_DEADLINE_MISSING_BY);
    }

    private void validateTodo(String[] parts) throws ParsingException {
        checkLengthMoreThanEqualTwo(parts, InstructionParser.ERROR_MESSAGE_TODO_MISSING_DESCRIPTION);
    }
}
// NOTE: Exceptions are checked twice, once in parseCommand and once in parse___Args()
