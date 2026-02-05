package echo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import echo.command.Command;
import echo.exception.ParsingException;
import echo.exception.StorageException;
import echo.exception.TaskException;
import echo.exception.TaskManagerException;
import echo.parser.InstructionParser;
import echo.storage.Storage;
import echo.task.Task;
import echo.task.TaskManager;
import echo.ui.MessageFormatter;

/**
 * Represents the main application class for the Echo chatbot.
 * Echo is a task management chatbot that allows users to create, manage, and track
 * various types of tasks including todos, deadlines, and events.
 *
 * This class handles the initialization of the application components (UI, storage, task manager),
 * user interaction through command parsing, and coordination between different modules.
 */
public class Echo {

    private static final String FILE_PATH = "data/echo.txt";
    private TaskManager taskManager;
    private MessageFormatter messageFormatter;
    private Storage storage;
    private InstructionParser instructionParser;
    private Optional<String> loadingErrorMessage;

    /**
     * Initalizes a new instance of Echo
     */
    public Echo() {
        this.storage = new Storage(Echo.FILE_PATH);
        this.messageFormatter = new MessageFormatter();
        this.loadingErrorMessage = Optional.empty();
        this.instructionParser = new InstructionParser();

        // load tasks, which will populate loadingErrorMessage if there is a file loading issue
        ArrayList<Task> tasks = this.loadTasksFromFile();
        this.taskManager = new TaskManager(tasks);
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
            this.loadingErrorMessage = Optional.of(this.messageFormatter.createErrorMessage(e));
            return new ArrayList<>();
        } catch (StorageException e) {
            this.loadingErrorMessage = Optional.of(this.messageFormatter.createErrorMessage(e));
            return new ArrayList<>();
        } catch (TaskException e) {
            this.loadingErrorMessage = Optional.of(this.messageFormatter.createErrorMessage(e));
            return new ArrayList<>();
        }
    }

    /**
     * Greets user with a greeting.
     *
     * @return a greeting to the user of type string.
     */
    public String greetUser() {
        return this.messageFormatter.greetUser();
    }

    /**
     * Gets loading error message in the form of an {@code Optional<String>}.
     *
     * @return Error message from loading the saved tasks history.
     */
    public Optional<String> getLoadingErrorMessage() {
        return this.loadingErrorMessage;
    }

    /**
     * Says bye to the user.
     *
     * @return an ending message for exiting the chatbot.
     */
    public String exitUser() {
        return this.messageFormatter.exitUser();
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
    public String addTask(String description, Command type,
            ArrayList<String> commandArgs) throws TaskException, TaskManagerException {
        Task task = this.taskManager.addTask(description, type, commandArgs);

        this.saveTasksToFile();

        int numTasks = this.taskManager.getNumTasks();
        return this.messageFormatter.createAddTaskMessage(task, numTasks);
    }

    /**
     * Get tasks that have been stored, and format it for the user.
     *
     * @return formatted tasks in the form of String.
     */
    public String getTasks() {
        ArrayList<Task> tasks = this.taskManager.getTasks();
        return this.messageFormatter.createListTaskMessage(tasks);
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

        return this.messageFormatter.createMarkAsDoneMessage(task);
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

        return this.messageFormatter.createMarkAsUndoneMessage(task);
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

        return this.messageFormatter.createRemoveTaskMessage(task, numTasks);
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
        String res = this.messageFormatter.createFilteredListTaskMessage(filteredTasks);
        return res;
    }

    /**
     * Returns a message for the user after the user asks Echo to perform a task
     *
     * @param userMessage Message from the user.
     * @return String of completed message, ready for rendering on the GUI
     */
    public String getResponse(String userMessage) {
        try {
            Command command = this.instructionParser.parseCommand(userMessage);

            switch (command) {
            case BYE:
                return this.exitUser();
            case LIST:
                return this.getTasks();
            case MARK:
                int markTaskNumber = this.instructionParser.parseMarkArgs(userMessage);
                String markMessage = this.markAsDone(markTaskNumber);
                return markMessage;
            case UNMARK:
                int unmarkTaskNumber = this.instructionParser.parseUnmarkArgs(userMessage);
                String unmarkMessage = this.markAsUndone(unmarkTaskNumber);
                return unmarkMessage;
            case DELETE:
                int deleteTaskNumber = this.instructionParser.parseDeleteArgs(userMessage);
                String deleteMessage = this.removeTask(deleteTaskNumber);
                return deleteMessage;
            case TODO:
                String todoDescription = instructionParser.parseTodoDescription(userMessage);
                ArrayList<String> todoCommandArgs = instructionParser.parseTodoArgs(userMessage);
                String todoMessage = this.addTask(todoDescription, Command.TODO, todoCommandArgs);
                return todoMessage;
            case DEADLINE:
                String deadlineDescription = instructionParser.parseDeadlineDescription(userMessage);
                ArrayList<String> deadlineArgs = instructionParser.parseDeadlineArgs(userMessage);
                String deadlineMessage = this.addTask(deadlineDescription, Command.DEADLINE, deadlineArgs);
                return deadlineMessage;
            case EVENT:
                String eventDescription = instructionParser.parseEventDescription(userMessage);
                ArrayList<String> eventArgs = instructionParser.parseEventArgs(userMessage);
                String eventMessage = this.addTask(eventDescription, Command.EVENT, eventArgs);
                return eventMessage;
            case FIND:
                String keyword = instructionParser.parseFindKeyword(userMessage);
                String foundTasksMessage = this.findTasks(keyword);
                return foundTasksMessage;
            default:
                String defaultMessage = "Unknown command, please try again!";
                return defaultMessage;
            }
        } catch (ParsingException e) {
            return this.messageFormatter.createErrorMessage(e);
        } catch (TaskManagerException e) {
            // if number to mark or unmark more than length of current task list
            return this.messageFormatter.createErrorMessage(e);
        } catch (TaskException e) {
            return this.messageFormatter.createErrorMessage(e);
        }
    }
}
