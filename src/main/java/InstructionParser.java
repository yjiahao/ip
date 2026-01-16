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
    public Command parseCommand(String command) {
        // trim command
        String trimmedCommand = command.trim();
        // split into at most 2 elements
        String[] parts = trimmedCommand.split(" ", 2);
        // get the keyword from user which is the first element
        String keyword = parts[0].toLowerCase();
        
        switch (keyword) {
            case "todo":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Todo description cannot be empty");
                }
                return Command.TODO;
            case "deadline":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Deadline description cannot be empty");
                }
                return Command.DEADLINE;
            case "list":
                return Command.LIST;
            case "event":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Event description cannot be empty");
                }
                return Command.EVENT;
            case "mark":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Mark requires a task number");
                }
                return Command.MARK;
            case "unmark":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Unmark requires a task number");
                }
                return Command.UNMARK;
            case "bye":
                return Command.BYE;
            default:
                throw new IllegalArgumentException("Unknown command");
        }
    }
}
