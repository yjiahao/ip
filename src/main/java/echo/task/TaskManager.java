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

    private ArrayList<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public TaskManager(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new task to the task manager.
     *
     * @param taskDescription description of task to be added.
     * @param type Command of type Command.EVENT, Command.TODO, or Command.DEADLINE
     * @param commandArgs Arguments for the corresponding command
     * @throws TaskException If creation of Task was unsuccessful before adding into TaskManager
     * @throws TaskManagerException If the Command type is not of TODO, EVENT or DEADLINE
     */
    public Task addTask(String taskDescription, Command type,
            ArrayList<String> commandArgs) throws TaskException, TaskManagerException {
        if (type.equals(Command.EVENT)) {
            Task event = new Event(taskDescription, commandArgs.get(0), commandArgs.get(1));
            this.tasks.add(event);
            return event;
        } else if (type.equals(Command.TODO)) {
            Task todo = new ToDo(taskDescription);
            this.tasks.add(todo);
            return todo;
        } else if (type.equals(Command.DEADLINE)) {
            Task deadline = new Deadline(taskDescription, commandArgs.get(0));
            this.tasks.add(deadline);
            return deadline;
        } else {
            throw new TaskManagerException("No such Task of type " + type.toString());
        }
    }

    /**
     * Checks a task number specified by user is not out of bounds
     *
     * @param taskNumber Task number specified by user to mark/unmark/remove
     * @throws TaskManagerException If negative taskNumber or taskNumber more than number of tasks present
     */
    private void checkNotOutOfBounds(int taskNumber) throws TaskManagerException {
        if (taskNumber > this.tasks.size()) {
            throw new TaskManagerException("You do not have " + taskNumber + " tasks yet...");
        } else if (taskNumber <= 0) {
            throw new TaskManagerException("No such thing as task " + taskNumber + "!");
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
