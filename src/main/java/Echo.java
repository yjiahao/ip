package main.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Echo {

    private static final String GREETING = "Hello! I'm Echo\n" + "What can I do for you?";
    private static final String EXIT_MESSAGE = "Bye. Hope to see you again soon!";
    private static final String SEPARATOR = "____________________________________________________________";

    private TaskManager taskManager;
    private Ui ui;

    /**
     * Initalizes a new instance of Echo
     */
    public Echo() {
        this.taskManager = new TaskManager();
        this.loadTasksFromFile();
        this.ui = new Ui();
    }

    private void loadTasksFromFile() {
        try {
            this.taskManager.loadTasks();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "\nStarting from empty history...");
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage() + "\nStarting from empty history...");
        }
    }

    /**
     * Greets user with a GREETING.
     * @return a GREETING to the user of type string.
     */
    public String greetUser() {
        return this.ui.greetUser();
    }

    /**
     * Says bye to the user.
     * @return an ending message for exiting the chatbot.
     */
    public String exitUser() {
        return this.ui.exitUser();
    }

    /**
     * Adds a task to the task list.
     * @param description the description of the task to be added.
     * @param type Type of command that the user is trying to perform.
     * @param commandArgs arguments for the particular command the user is trying to perform.
     * @return description to inform user the addition of a new task.
     */
    public String addTask(String description, Command type, ArrayList<String> commandArgs) {
        Task task = this.taskManager.addTask(description, type, commandArgs);
        this.saveTasksToFile();
        int numTasks = this.taskManager.getNumTasks();
        return this.ui.createAddTaskMessage(task, numTasks);
    }

    /**
     * Get tasks that have been stored, and format it for the user.
     * @return formatted tasks in the form of String.
     */
    public String getTasks() {
        ArrayList<Task> tasks = this.taskManager.getTasks();
        return this.ui.createListTaskMessage(tasks);
    }

    /**
     * Marks a task in the task manager as done.
     * @param taskNumber 1-indexed task number.
     * @return String of message telling user a task has been marked as done.
     */
    public String markAsDone(int taskNumber) {
        Task task = this.taskManager.markAsDone(taskNumber);
        this.saveTasksToFile();
        return this.ui.createMarkAsDoneMessage(task);
    }

    /**
     * Marks a task as undone in the task manager.
     * @param taskNumber 1-indexed task number.
     * @return String of message telling user a task has been marked as undone.
     */
    public String markAsUndone(int taskNumber) {
        Task task = this.taskManager.markAsUndone(taskNumber);
        this.saveTasksToFile();
        return this.ui.createMarkAsUndoneMessage(task);
    }

    /**
     * Removes a task from the task manager.
     * @param taskNumber 1-indexed task number to be removed.
     * @return String of message informing user the task has been removed.
     */
    public String removeTask(int taskNumber) {
        Task task = this.taskManager.removeTask(taskNumber);
        int numTasks = this.taskManager.getNumTasks();
        this.saveTasksToFile();
        return this.ui.createRemoveTaskMessage(task, numTasks);
    }

    /**
     * Save the tasks in the TaskManager to a file.
     */
    private void saveTasksToFile() {
        try {
            this.taskManager.saveTasks();
        } catch (IOException e) {
            System.out.println("Saving failed due to: " + e.getMessage());
        }
    }
}

// NOTE: with the addition of the Ui class, maybe we should have the main method here instead
