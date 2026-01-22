package main.java.echo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import main.java.echo.command.Command;
import main.java.echo.exception.ParsingException;
import main.java.echo.exception.TaskManagerException;
import main.java.echo.parser.InstructionParser;

/**
 * Main entry point for the chatbot.
 * This class initializes and runs the Echo program which greets the user
 * and displays an exit message.
 */
public class Main {
    private static final String SEPARATOR = "____________________________________________________________";
    /**
     * Main method that starts the chatbot.
     * Creates an Echo instance and prints the greeting and exit messages.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Echo echo = new Echo();
        InstructionParser instructionParser = new InstructionParser();
        Scanner scanner = new Scanner(System.in);

        // initialize hello message by Echo
        String greeting = echo.greetUser();
        System.out.println(greeting);

        while (true) {
            String userMessage = scanner.nextLine();
            try {
                Command command = instructionParser.parseCommand(userMessage);
                String botMessage;

                switch (command) {
                case BYE:
                    System.out.println(echo.exitUser());
                    scanner.close();
                    return;
                case LIST:
                    botMessage = echo.getTasks();
                    break;
                case MARK:
                    String[] markParts = userMessage.split(" ");
                    int markTaskNumber = Integer.parseInt(markParts[1]);
                    botMessage = echo.markAsDone(markTaskNumber);
                    break;
                case UNMARK:
                    String[] unmarkParts = userMessage.split(" ");
                    int unmarkTaskNumber = Integer.parseInt(unmarkParts[1]);
                    botMessage = echo.markAsUndone(unmarkTaskNumber);
                    break;
                case DELETE:
                    String[] deleteParts = userMessage.split(" ");
                    int deleteTaskNumber = Integer.parseInt(deleteParts[1]);
                    botMessage = echo.removeTask(deleteTaskNumber);
                    break;
                case TODO:
                    String[] todoParts = userMessage.split(" ", 2);
                    botMessage = echo.addTask(todoParts[1], Command.TODO, new ArrayList<>());
                    break;
                case DEADLINE:
                    String deadlineDetails = userMessage.substring(9);
                    String[] deadlineParts = deadlineDetails.split("/by", 2);
                    ArrayList<String> deadlineArgs = new ArrayList<>(Arrays.asList(deadlineParts));
                    botMessage = echo.addTask(deadlineArgs.get(0).trim(), Command.DEADLINE,
                        new ArrayList<>(Arrays.asList(deadlineArgs.get(1).trim())));
                    break;
                case EVENT:
                    String eventDetails = userMessage.substring(6).trim();
                    String[] fromSplit = eventDetails.split("/from", 2);
                    String description = fromSplit[0].trim();
                    String[] toSplit = fromSplit[1].split("/to", 2);
                    String from = toSplit[0].trim();
                    String to = toSplit[1].trim();
                    ArrayList<String> eventArgs = new ArrayList<>(Arrays.asList(from, to));
                    botMessage = echo.addTask(description, Command.EVENT, eventArgs);
                    break;
                default:
                    botMessage = "Unknown command, please try again!";
                    break;
                }

                System.out.println(botMessage);
            } catch (ParsingException e) {
                System.out.println(Main.SEPARATOR + "\n" + e.getMessage() + "\n" + Main.SEPARATOR);
            } catch (TaskManagerException e) {
                // if number to mark or unmark more than length of current task list
                System.out.println(Main.SEPARATOR + "\n" + e.getMessage() + "\n" + Main.SEPARATOR);
            }
        }
    }
}
