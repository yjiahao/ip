package echo.task;

import java.util.ArrayList;
import java.util.List;

import echo.command.Command;
import echo.exception.TaskException;
import echo.exception.TaskManagerException;

/**
 * Manages the collection of tasks in the Echo application.
 * This class handles operations on tasks including adding, removing, marking/unmarking,
 * and searching for tasks.
 *
 * The TaskManager maintains an ArrayList of tasks and provides methods to manipulate
 * and query this collection. It performs validation to ensure task operations are valid
 * (e.g., task numbers are within bounds).
 */
public class TaskManager {

    private static final String ERROR_MESSAGE_TASK_LIST_NULL = "tasks cannot be null";
    private static final String ERROR_MESSAGE_TASK_NULL = "Task is null";
    private static final String ERROR_MESSAGE_TASK_DESCRIPTION_NULL = "Task description is null";
    private static final String ERROR_MESSAGE_COMMAND_TYPE_NULL = "Command type is null";
    private static final String ERROR_MESSAGE_COMMAND_ARGUMENTS_NULL = "Command arguments is null";
    private static final String ERROR_MESSAGE_UNKNOWN_TASK_TYPE = "No such Task of type %s";
    private static final String ERROR_MESSAGE_TASK_NUMBER_MORE_THAN_SIZE = "You do not have %d tasks yet...";
    private static final String ERROR_MESSAGE_TASK_NUMBER_LESS_THAN_EQUAL_ZERO =
        "No such thing as task %d!";

    private static final String WARNING_MESSAGE_CONFLICTING_TASKS =
        "\n\nNote that you have other Tasks conflicting with this new task!\n";

    private ArrayList<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a new TaskManager
     * @param tasks The tasks for the TaskManager to manage
     */
    public TaskManager(ArrayList<Task> tasks) {
        assert tasks != null : TaskManager.ERROR_MESSAGE_TASK_LIST_NULL;

        this.tasks = tasks;
    }

    /**
     * Adds a new task to the task manager.
     *
     * @param taskDescription description of task to be added.
     * @param type Command of type Command.EVENT, Command.TODO, or Command.DEADLINE
     * @param commandArgs Arguments for the corresponding command
     * @return a String representation of the Task that was added.
     *     If the newly added task conflicts with another task, alert the user but allow adding it
     * @throws TaskException If creation of Task was unsuccessful before adding into TaskManager
     * @throws TaskManagerException If the Command type is not of TODO, EVENT or DEADLINE
     */
    public String addTask(String taskDescription, Command type,
            ArrayList<String> commandArgs) throws TaskException, TaskManagerException {

        assert taskDescription != null : TaskManager.ERROR_MESSAGE_TASK_DESCRIPTION_NULL;
        assert type != null : TaskManager.ERROR_MESSAGE_COMMAND_TYPE_NULL;
        assert commandArgs != null : TaskManager.ERROR_MESSAGE_COMMAND_ARGUMENTS_NULL;

        if (type.equals(Command.EVENT)) {
            Task event = new Event(taskDescription, commandArgs.get(0), commandArgs.get(1));
            String formattedString = this.getTaskStringWithConflictNotice(event);
            this.tasks.add(event);
            return formattedString;
        } else if (type.equals(Command.TODO)) {
            Task todo = new ToDo(taskDescription);
            String formattedString = this.getTaskStringWithConflictNotice(todo);
            this.tasks.add(todo);
            return formattedString;
        } else if (type.equals(Command.DEADLINE)) {
            Task deadline = new Deadline(taskDescription, commandArgs.get(0));
            String formattedString = this.getTaskStringWithConflictNotice(deadline);
            this.tasks.add(deadline);
            return formattedString;
        } else {
            throw new TaskManagerException(
                TaskManager.ERROR_MESSAGE_UNKNOWN_TASK_TYPE.formatted(type.toString()));
        }
    }

    private String getTaskStringWithConflictNotice(Task task) throws TaskManagerException {
        boolean hasConflicts = this.isConflicting(task);
        String res = task.toString();
        if (hasConflicts) {
            res = res + TaskManager.WARNING_MESSAGE_CONFLICTING_TASKS;
        }
        return res;
    }

    private boolean isConflicting(Task task) throws TaskManagerException {
        boolean hasConflicts = this.tasks.stream()
            .reduce(false, (currentlyHasConflicts, newTask)
                -> currentlyHasConflicts || newTask.hasSchedulingConflict(task), (x, y) -> x || y);
        return hasConflicts;
    }

    /**
     * Checks a task number specified by user is not out of bounds
     *
     * @param taskNumber Task number specified by user to mark/unmark/remove
     * @throws TaskManagerException If negative taskNumber or taskNumber more than number of tasks present
     */
    private void checkNotOutOfBounds(int taskNumber) throws TaskManagerException {
        if (taskNumber > this.tasks.size()) {
            throw new TaskManagerException(TaskManager.ERROR_MESSAGE_TASK_NUMBER_MORE_THAN_SIZE.formatted(taskNumber));
        } else if (taskNumber <= 0) {
            throw new TaskManagerException(
                TaskManager.ERROR_MESSAGE_TASK_NUMBER_LESS_THAN_EQUAL_ZERO.formatted(taskNumber));
        }
    }

    /**
     * Removes a Task from task list and returns it.
     *
     * @param taskNumber the 1-indexed Task that is to be removed.
     * @return The Task that was removed.
     * @throws TaskManagerException If negative taskNumber or taskNumber more than number of tasks present
     */
    public Task removeTask(int taskNumber) throws TaskManagerException {
        this.checkNotOutOfBounds(taskNumber);
        // array is 0 indexed so need to translate it by 1
        Task removedTask = this.tasks.remove(taskNumber - 1);
        return removedTask;
    }

    /**
     * Get all tasks in the task manager as an ArrayList of Task.
     *
     * @return An ArrayList of Task.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Marks a task as done.
     *
     * @param taskNumber the task number (1-indexed) to mark as done.
     * @return the task that was marked as done in String.
     * @throws TaskManagerException If negative taskNumber or taskNumber more than number of tasks present
     */
    public Task markAsDone(int taskNumber) throws TaskManagerException {
        this.checkNotOutOfBounds(taskNumber);
        // array is 0 indexed so need to translate by 1
        Task t = this.tasks.get(taskNumber - 1);

        assert t != null : TaskManager.ERROR_MESSAGE_TASK_NULL;

        t.markAsDone();
        return t;
    }

    /**
     * Marks a Task as undone.
     *
     * @param taskNumber the task number (1-indexed) to mark as undone.
     * @return the Task that was marked as undone.
     * @throws TaskManagerException If negative taskNumber or taskNumber more than number of tasks present
     */
    public Task markAsUndone(int taskNumber) throws TaskManagerException {
        this.checkNotOutOfBounds(taskNumber);
        Task t = this.tasks.get(taskNumber - 1);

        assert t != null : TaskManager.ERROR_MESSAGE_TASK_NULL;

        t.markAsUndone();
        return t;
    }

    /**
     * Get the number of tasks present in the TaskManager currently.
     *
     * @return Number of tasks present of type int.
     */
    public int getNumTasks() {
        return this.tasks.size();
    }

    /**
     * Searches for Tasks whose descriptions contain keyword.
     *
     * @param keyword Keyword to search for in the Task descriptions.
     * @return ArrayList of Task whose descriptions contain keyword.
     */
    public ArrayList<Task> findTasks(String keyword) {
        List<Task> list = this.tasks.stream()
            .filter(x -> x.descriptionContains(keyword))
            .toList();
        ArrayList<Task> filteredTasks = new ArrayList<>(list);
        return filteredTasks;
    }
}
// TODO: managed to detect scheduling conflicts. But check if it is possible to show which tasks conflict
// with the current task that is added
