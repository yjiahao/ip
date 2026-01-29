package echo.ui;

import java.util.ArrayList;

import echo.task.Task;

/**
 * Handles all user interface interactions for the Echo application.
 * This class is responsible for formatting and generating messages displayed to the user,
 * including greetings, task notifications, error messages, and task lists.
 *
 * All messages are formatted with separators for better readability in the UI.
 */
public class MessageFormatter {
    private static final String GREETING = "Hello! I'm Echo\n" + "What can I do for you?";
    private static final String EXIT_MESSAGE = "Bye. Hope to see you again soon!";
    private static final String SEPARATOR = "____________________________________________________________";

    public MessageFormatter() {

    }

    /**
     * Greets user with a GREETING.
     *
     * @return a GREETING to the user of type string.
     */
    public String greetUser() {
        return MessageFormatter.SEPARATOR + "\n" + MessageFormatter.GREETING
            + "\n" + MessageFormatter.SEPARATOR;
    }

    /**
     * Says bye to the user.
     *
     * @return an ending message for exiting the chatbot.
     */
    public String exitUser() {
        return MessageFormatter.SEPARATOR + "\n" + MessageFormatter.EXIT_MESSAGE
            + "\n" + MessageFormatter.SEPARATOR + "\n";
    }

    /**
     * Creates a message for the user after adding a task.
     *
     * @param task Task that has been added
     * @param numTasks Number of tasks remaining
     * @return description to inform user the addition of a new task.
     */
    public String createAddTaskMessage(Task task, int numTasks) {
        return MessageFormatter.SEPARATOR + "\n" + "Got it. I've added this task:\n  "
            + task.toString() + "\n" + "Now you have " + numTasks + " tasks in the list."
                + "\n" + MessageFormatter.SEPARATOR;
    }

    /**
     * Formats the tasks for the user to see in the user interface.
     *
     * @param tasks ArrayList of Task.
     * @return Formatted string of Tasks suitable for the user interface.
     */
    public String createListTaskMessage(ArrayList<Task> tasks) {
        String tasksString = this.createNumberedTasksString(tasks);
        return MessageFormatter.SEPARATOR + "\n" + "Here are the tasks in your list:\n"
            + "\n" + tasksString + "\n" + MessageFormatter.SEPARATOR;
    }

    /**
     * Generates a message that tells user task is marked as done.
     *
     * @param task Task that has been marked as done.
     * @return String of formatted message for the user.
     */
    public String createMarkAsDoneMessage(Task task) {
        return MessageFormatter.SEPARATOR + "\n" + "Nice! I've marked this task as done:\n  "
            + task.toString() + "\n" + MessageFormatter.SEPARATOR;
    }

    /**
     * Generates a message that tells user task is marked as not done yet.
     *
     * @param task Task that has been marked as undone.
     * @return String of formatted message for the user.
     */
    public String createMarkAsUndoneMessage(Task task) {
        return MessageFormatter.SEPARATOR + "\n" + "OK, I've marked this task as not done yet:\n  "
            + task.toString() + "\n" + MessageFormatter.SEPARATOR;
    }

    /**
     * Formats a message for the user to tell them Task has been removed.
     *
     * @param task Task that has been removed.
     * @param numTasks Number of Tasks left.
     * @return String of formatted message after removal of Task.
     */
    public String createRemoveTaskMessage(Task task, int numTasks) {
        return MessageFormatter.SEPARATOR + "\n" + "Noted. I've removed this task:\n  "
            + task.toString() + "\n" + "Now you have " + numTasks + " tasks in the list."
                + "\n" + MessageFormatter.SEPARATOR;
    }

    /**
     * Format an error message for the user to inform them of an exception that has been caught.
     *
     * @param e Exception that was cause.
     * @return String of formatted message for the user that includes the exception message.
     */
    public String createErrorMessage(Exception e) {
        return MessageFormatter.SEPARATOR + "\n" + e.getMessage() + "\n" + MessageFormatter.SEPARATOR;
    }

    /**
     * Private helper method to format the tasks as a numbered list.
     *
     * @param tasks ArrayList of Task for formatting.
     * @return Formatted String of Tasks as a numbered list.
     */
    private String createNumberedTasksString(ArrayList<Task> tasks) {
        String res = "";
        int i = 1;
        for (Task t : tasks) {
            res = res + i + ". " + t + "\n";
            i += 1;
        }
        String tasksString = res.stripTrailing();
        return tasksString;
    }

    /**
     * Formats the filtered tasks for the user to see in the user interface.
     *
     * @param filteredTasks ArrayList of Task that have been filtered by some sort of keyword.
     * @return Formatted string of Tasks suitable for the user interface.
     */
    public String createFilteredListTaskMessage(ArrayList<Task> filteredTasks) {
        String tasksString = this.createNumberedTasksString(filteredTasks);
        return MessageFormatter.SEPARATOR + "\n" + "Here are the matching tasks in your list:\n"
            + "\n" + tasksString + "\n" + MessageFormatter.SEPARATOR;
    }
}
