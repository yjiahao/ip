package main.java;

import java.util.ArrayList;

public class Echo {

    private static final String greeting = "Hello! I'm Echo\n" + "What can I do for you?";
    private static final String exitMessage = "Bye. Hope to see you again soon!";
    private static final String separator = "____________________________________________________________";

    private TaskManager taskManager;

    public Echo() {
        this.taskManager = new TaskManager();
    }

    /**
     * Greets user with a greeting.
     * @return a greeting to the user of type string.
     */
    public String greetUser() {
        return Echo.separator + "\n" + Echo.greeting + "\n" + Echo.separator;
    }

    /**
     * Says bye to the user.
     * @return an ending message for exiting the chatbot.
     */
    public String exitUser() {
        return Echo.separator + "\n" + Echo.exitMessage + "\n" + Echo.separator + "\n";
    }

    /**
     * Adds a task to the task list.
     * @param description the description of the task to be added.
     * @return description to inform user the addition of a new task.
     */
    public String addTask(String description, Command type, ArrayList<String> commandArgs) {
        String taskString = this.taskManager.addTask(description, type, commandArgs);
        return Echo.separator + "\n" + "Got it. I've added this task:\n  "
            + taskString + "\n" + "Now you have " + this.taskManager.getNumTasks() + " tasks in the list."
                + "\n" + Echo.separator;
    }

    /**
     * Get tasks that have been stored, and format it for the user.
     * @return formatted tasks in the form of String.
     */
    public String getTasks() {
        String tasks = this.taskManager.getTasks();
        return Echo.separator + "\n" + "Here are the tasks in your list:\n"
            + "\n" + tasks + "\n" + Echo.separator;
    }

    /**
     * Marks a task in the task manager as done.
     * @param taskNumber 1-indexed task number.
     * @return String of message telling user a task has been marked as done.
     */
    public String markAsDone(int taskNumber) {
        String taskString = this.taskManager.markAsDone(taskNumber);
        return Echo.separator + "\n" + "Nice! I've marked this task as done:\n  " + taskString + "\n" + Echo.separator;
    }

    /**
     * Marks a task as undone in the task manager.
     * @param taskNumber 1-indexed task number.
     * @return String of message telling user a task has been marked as undone.
     */
    public String markAsUndone(int taskNumber) {
        String taskString = this.taskManager.markAsUndone(taskNumber);
        return Echo.separator + "\n" + "OK, I've marked this task as not done yet:\n  "
            + taskString + "\n" + Echo.separator;
    }

    /**
     * Removes a task from the task manager.
     * @param taskNumber 1-indexed task number to be removed.
     * @return String of message informing user the task has been removed.
     */
    public String removeTask(int taskNumber) {
        String taskString = this.taskManager.removeTask(taskNumber);
        return Echo.separator + "\n" + "Noted. I've removed this task:\n  "
            + taskString + "\n" + "Now you have " + this.taskManager.getNumTasks() + " tasks in the list."
                + "\n" + Echo.separator;
    }
}
