package main.java;

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
            default:
                throw new ParsingException("Sorry what does that mean ah? I never see " + keyword + " before...");
        }
    }
}
