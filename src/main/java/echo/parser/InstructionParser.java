package echo.parser;

import java.util.ArrayList;
import java.util.Arrays;

import echo.command.Command;
import echo.exception.ParsingException;

public class InstructionParser {
    public InstructionParser() {

    }

    /**
     * Parses the given command string and returns the corresponding Command enum.
     *
     * @param command the command string to parse
     * @return the Command enum corresponding to the command
     * @throws IllegalArgumentException if the command is unknown
     */
    public Command parseCommand(String command) throws ParsingException {
        // trim command
        String trimmedCommand = command.trim();
        // split into at most 2 elements
        String[] parts = trimmedCommand.split(" ", 2);
        // get the keyword from user which is the first element
        String keyword = parts[0].toLowerCase();
        switch (keyword) {
        case "todo":
            if (parts.length < 2) {
                throw new ParsingException("The todo description cannot be empty leh...");
            }
            return Command.TODO;
        case "deadline":
            if (parts.length < 2) {
                throw new ParsingException("got no deadline description and by when you should complete it...");
            }
            // catch no description of deadline
            if (parts[1].startsWith("/by")) {
                throw new ParsingException("got no deadline description...");
            }
            // catch no /by ... for deadline
            String[] deadlineParts = parts[1].split("/by ");
            if (deadlineParts.length < 2) {
                throw new ParsingException("need to indicate when you need to complete the deadline by!");
            }
            return Command.DEADLINE;
        case "list":
            return Command.LIST;
        case "event":
            if (parts.length < 2) {
                throw new ParsingException("why got no event description one...");
            }
            return Command.EVENT;
        case "mark":
            if (parts.length < 2) {
                throw new ParsingException("mark requires a task number!");
            }
            // catch cases like "mark string" instead of "mark 1"
            try {
                int num = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new ParsingException("mark needs a valid task number!");
            }
            return Command.MARK;
        case "unmark":
            if (parts.length < 2) {
                throw new ParsingException("unmark requires a task number!");
            }
            // catch cases like "unmark string" instead of "unmark 1"
            try {
                int num = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new ParsingException("unmark needs a valid task number!");
            }
            return Command.UNMARK;
        case "delete":
            if (parts.length < 2) {
                throw new ParsingException("delete requires a task number!");
            }
            // catch cases like "delete string" instead of "delete 1"
            try {
                int num = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                throw new ParsingException("delete needs a valid task number!");
            }
            return Command.DELETE;
        case "bye":
            return Command.BYE;
        case "find":
            return Command.FIND;
        default:
            throw new ParsingException("Sorry what does that mean ah? I never see " + keyword + " before...");
        }
    }

    /**
     * Parse the user arguments for marking or unmarking a Task
     * @param userMessage String of raw user message
     * @return int representing the Task number that user wants to mark or unmark
     */
    public int parseMarkUnmarkArgs(String userMessage) {
        String[] markParts = userMessage.split(" ");
        int markTaskNumber = Integer.parseInt(markParts[1]);
        return markTaskNumber;
    }

    /**
     * Parse user arguments for deleting a Task
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
     * @param userMessage String of raw user message
     * @return String representing the description of the ToDo Task user specified
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
     * @param userMessage String of raw user message
     * @return Currently returns an empty ArrayList because Todo has no arguments
     */
    public ArrayList<String> parseTodoArgs(String userMessage) {
        return new ArrayList<>();
    }

    /**
     * Parses the description of the Deadline Task.
     * @param userMessage String of raw user message
     * @return Description of Deadline Task
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
     * @param userMessage String of raw user message
     * @return Arguments of the Deadline Task as an ArrayList of String
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
     * @param userMessage String of raw user message
     * @return Description of Event Task
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
     * @param userMessage String of raw user message
     * @return Arguments of Event Task as an ArrayList of String
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
     * @param userMessage String of raw user message.
     * @return String of keyword to search for in the tasks.
     * @throws ParsingException
     */
    public String parseFindKeyword(String userMessage) throws ParsingException {
        String[] findParts = userMessage.split(" ", 2);
        if (findParts.length < 2) {
            throw new ParsingException("You did not specify a keyword to look for!");
        }
        String keyword = findParts[1];
        return keyword;
    }
}
// NOTE: Exceptions are checked twice, once in parseCommand and once in parse___Args()
