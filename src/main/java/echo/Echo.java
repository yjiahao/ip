package echo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import echo.command.Command;
import echo.exception.ParsingException;
import echo.exception.StorageException;
import echo.exception.TaskException;
import echo.exception.TaskManagerException;
import echo.parser.InstructionParser;
import echo.storage.Storage;
import echo.task.Task;
import echo.task.TaskManager;
import echo.ui.Ui;

public class Echo {

    private static final String FILE_PATH = "data/echo.txt";
    private TaskManager taskManager;
    private Ui ui;
    private Storage storage;

    /**
     * Initalizes a new instance of Echo
     */
    public Echo() {
        this.storage = new Storage(Echo.FILE_PATH);
        ArrayList<Task> tasks = this.loadTasksFromFile();
        this.taskManager = new TaskManager(tasks);
        this.ui = new Ui();
    }

    /**
     * Loads tasks from file.
     * Intended as a helper method to use the Storage class to load tasks.
     *
     * @return ArrayList of Task after successfully loading, or empty ArrayList if there was an error.
     */
    private ArrayList<Task> loadTasksFromFile() {
        try {
            ArrayList<Task> tasks = this.storage.loadTasks();
            return tasks;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "\nStarting from empty history...");
            return new ArrayList<>();
        } catch (StorageException e) {
            System.out.println(e.getMessage() + "\nStarting from empty history...");
            return new ArrayList<>();
        } catch (TaskException e) {
            System.out.println(e.getMessage() + "\nStarting from empty history...");
            return new ArrayList<>();
        }
    }

    /**
     * Greets user with a greeting.
     *
     * @return a greeting to the user of type string.
     */
    public String greetUser() {
        return this.ui.greetUser();
    }

    /**
     * Says bye to the user.
     *
     * @return an ending message for exiting the chatbot.
     */
    public String exitUser() {
        return this.ui.exitUser();
    }

    /**
     * Adds a task to the task list.
     *
     * @param description the description of the task to be added.
     * @param type Type of command that the user is trying to perform.
     * @param commandArgs arguments for the particular command the user is trying to perform.
     * @return description to inform user the addition of a new task.
     * @throws TaskException if creation of task was unsuccessful.
     * @throws TaskManagerException if type is not of Command.TODO, Command.Event or Command.Description.
     */
    public String addTask(String description, Command type, ArrayList<String> commandArgs) throws TaskException {
        Task task = this.taskManager.addTask(description, type, commandArgs);

        this.saveTasksToFile();

        int numTasks = this.taskManager.getNumTasks();
        return this.ui.createAddTaskMessage(task, numTasks);
    }

    /**
     * Get tasks that have been stored, and format it for the user.
     *
     * @return formatted tasks in the form of String.
     */
    public String getTasks() {
        ArrayList<Task> tasks = this.taskManager.getTasks();
        return this.ui.createListTaskMessage(tasks);
    }

    /**
     * Marks a task in the task manager as done.
     *
     * @param taskNumber 1-indexed task number.
     * @return String of message telling user a task has been marked as done.
     * @throws TaskManagerException if negative taskNumber or taskNumber more than number of tasks present
     */
    public String markAsDone(int taskNumber) throws TaskManagerException {
        Task task = this.taskManager.markAsDone(taskNumber);

        this.saveTasksToFile();

        return this.ui.createMarkAsDoneMessage(task);
    }

    /**
     * Marks a task as undone in the task manager.
     *
     * @param taskNumber 1-indexed task number.
     * @return String of message telling user a task has been marked as undone.
     * @throws TaskManagerException If negative taskNumber or taskNumber more than number of tasks present
     */
    public String markAsUndone(int taskNumber) throws TaskManagerException {
        Task task = this.taskManager.markAsUndone(taskNumber);

        this.saveTasksToFile();

        return this.ui.createMarkAsUndoneMessage(task);
    }

    /**
     * Removes a task from the task manager.
     *
     * @param taskNumber 1-indexed task number to be removed.
     * @return String of message informing user the task has been removed.
     * @throws TaskManagerException If negative taskNumber or taskNumber more than number of tasks present
     */
    public String removeTask(int taskNumber) throws TaskManagerException {
        Task task = this.taskManager.removeTask(taskNumber);
        int numTasks = this.taskManager.getNumTasks();

        this.saveTasksToFile();

        return this.ui.createRemoveTaskMessage(task, numTasks);
    }

    /**
     * Saves the tasks in the TaskManager to a file.
     * Informs user if saving of current tasks failed.
     */
    private void saveTasksToFile() {
        try {
            ArrayList<Task> tasks = this.taskManager.getTasks();
            this.storage.saveTasks(tasks);
        } catch (IOException e) {
            System.out.println("Saving failed due to: " + e.getMessage());
        }
    }

    /**
     * Searches TaskManager for Tasks whose descriptions have keyword
     *
     * @param keyword keyword to look for in the Task description
     * @return Formatted String that contains the Tasks that have keyword in their description
     */
    public String findTasks(String keyword) {
        ArrayList<Task> filteredTasks = this.taskManager.findTasks(keyword);
        String res = this.ui.createFilteredListTaskMessage(filteredTasks);
        return res;
    }

    /**
     * Main function to run the bot.
     * @param args
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
                    int markTaskNumber = instructionParser.parseMarkUnmarkArgs(userMessage);
                    botMessage = echo.markAsDone(markTaskNumber);
                    break;
                case UNMARK:
                    int unmarkTaskNumber = instructionParser.parseMarkUnmarkArgs(userMessage);
                    botMessage = echo.markAsUndone(unmarkTaskNumber);
                    break;
                case DELETE:
                    int deleteTaskNumber = instructionParser.parseDeleteArgs(userMessage);
                    botMessage = echo.removeTask(deleteTaskNumber);
                    break;
                case TODO:
                    String todoDescription = instructionParser.parseTodoDescription(userMessage);
                    ArrayList<String> todoCommandArgs = instructionParser.parseTodoArgs(userMessage);
                    botMessage = echo.addTask(todoDescription, Command.TODO, todoCommandArgs);
                    break;
                case DEADLINE:
                    String deadlineDescription = instructionParser.parseDeadlineDescription(userMessage);
                    ArrayList<String> deadlineArgs = instructionParser.parseDeadlineArgs(userMessage);
                    botMessage = echo.addTask(deadlineDescription, Command.DEADLINE, deadlineArgs);
                    break;
                case EVENT:
                    String eventDescription = instructionParser.parseEventDescription(userMessage);
                    ArrayList<String> eventArgs = instructionParser.parseEventArgs(userMessage);
                    botMessage = echo.addTask(eventDescription, Command.EVENT, eventArgs);
                    break;
                case FIND:
                    String keyword = instructionParser.parseFindKeyword(userMessage);
                    botMessage = echo.findTasks(keyword);
                    break;
                default:
                    botMessage = "Unknown command, please try again!";
                    break;
                }

                System.out.println(botMessage);
            } catch (ParsingException e) {
                System.out.println(echo.ui.createErrorMessage(e));
            } catch (TaskManagerException e) {
                // if number to mark or unmark more than length of current task list
                System.out.println(echo.ui.createErrorMessage(e));
            } catch (TaskException e) {
                System.out.println(echo.ui.createErrorMessage(e));
            }
        }
    }
}
