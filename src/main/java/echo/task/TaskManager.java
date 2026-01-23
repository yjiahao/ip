package echo.task;

import java.util.ArrayList;

import echo.command.Command;
import echo.exception.TaskException;
import echo.exception.TaskManagerException;

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
     * @param taskDescription description of task to be added.
     */
    public Task addTask(String taskDescription, Command type, ArrayList<String> commandArgs) throws TaskException {
        if (type.equals(Command.EVENT)) {
            Task event = new Event(taskDescription, commandArgs.get(0), commandArgs.get(1));
            this.tasks.add(event);
            return event;
        } else if (type.equals(Command.TODO)) {
            Task todo = new ToDo(taskDescription);
            this.tasks.add(todo);
            return todo;
        } else {
            Task deadline = new Deadline(taskDescription, commandArgs.get(0));
            this.tasks.add(deadline);
            return deadline;
        }
    }

    /**
     * Private helper method to check a task number specified by user is not out of bounds
     * @param taskNumber Task number specified by user to mark/unmark/remove
     * @throws TaskManagerException
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
     * @param taskNumber the 1-indexed Task that is to be removed.
     * @return the Task that was removed.
     * @throws TaskManagerException
     */
    public Task removeTask(int taskNumber) throws TaskManagerException {
        this.checkNotOutOfBounds(taskNumber);
        Task removedTask = this.tasks.remove(taskNumber - 1);
        return removedTask;
    }

    /**
     * Get all tasks in the task manager as an ArrayList of Task.
     * @return a string of tasks.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Marks a task as done.
     * @param taskNumber the task number (1-indexed) to mark as done.
     * @return the task that was marked as done in String.
     * @throws TaskManagerException
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
     * @param taskNumber the task number (1-indexed) to mark as undone.
     * @return the Task that was marked as undone.
     * @throws TaskManagerException
     */
    public Task markAsUndone(int taskNumber) throws TaskManagerException {
        this.checkNotOutOfBounds(taskNumber);
        Task t = this.tasks.get(taskNumber - 1);
        t.markAsUndone();
        return t;
    }

    /**
     * Get the number of tasks present in the TaskManager currently.
     * @return Number of tasks present of type int.
     */
    public int getNumTasks() {
        return this.tasks.size();
    }
}
