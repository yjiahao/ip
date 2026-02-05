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
    private static final String MESSAGE_GREETING = "Hello! I'm Echo\n" + "What can I do for you?";
    private static final String MESSAGE_EXIT = "Bye. Hope to see you again soon!";
    // private static final String SEPARATOR = "____________________________________________________________";

    public MessageFormatter() {

    }

    /**
     * Greets user with a GREETING.
     *
     * @return a GREETING to the user of type string.
     */
    public String greetUser() {
        // return MessageFormatter.SEPARATOR + "\n" + MessageFormatter.GREETING
        //     + "\n" + MessageFormatter.SEPARATOR;
        return MessageFormatter.MESSAGE_GREETING;
    }

    /**
     * Says bye to the user.
     *
     * @return an ending message for exiting the chatbot.
     */
    public String exitUser() {
        // return MessageFormatter.SEPARATOR + "\n" + MessageFormatter.EXIT_MESSAGE
        //     + "\n" + MessageFormatter.SEPARATOR + "\n";
        return MessageFormatter.MESSAGE_EXIT;
    }

    /**
     * Creates a message for the user after adding a task.
     *
     * @param task Task that has been added
     * @param numTasks Number of tasks remaining
     * @return description to inform user the addition of a new task.
     */
    public String createAddTaskMessage(Task task, int numTasks) {
        // return MessageFormatter.SEPARATOR + "\n" + "Got it. I've added this task:\n  "
        //     + task.toString() + "\n" + "Now you have " + numTasks + " tasks in the list."
        //         + "\n" + MessageFormatter.SEPARATOR;
        assert task != null : "Task is null";
        assert numTasks >= 0 : "Number of tasks is negative";

        return "Got it. I've added this task:\n\n"
            + task.toString() + "\n" + "Now you have " + numTasks + " tasks in the list.";
    }

    /**
     * Formats the tasks for the user to see in the user interface.
     *
     * @param tasks ArrayList of Task.
     * @return Formatted string of Tasks suitable for the user interface.
     */
    public String createListTaskMessage(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list is null";

        String tasksString = this.createNumberedTasksString(tasks);
        // return MessageFormatter.SEPARATOR + "\n" + "Here are the tasks in your list:\n"
        //     + "\n" + tasksString + "\n" + MessageFormatter.SEPARATOR;
        return "Here are the tasks in your list:\n\n" + tasksString;
    }

    /**
     * Generates a message that tells user task is marked as done.
     *
     * @param task Task that has been marked as done.
     * @return String of formatted message for the user.
     */
    public String createMarkAsDoneMessage(Task task) {
        assert task != null : "Task is null";
        // return MessageFormatter.SEPARATOR + "\n" + "Nice! I've marked this task as done:\n  "
        //     + task.toString() + "\n" + MessageFormatter.SEPARATOR;
        return "Nice! I've marked this task as done:\n\n" + task.toString();
    }

    /**
     * Generates a message that tells user task is marked as not done yet.
     *
     * @param task Task that has been marked as undone.
     * @return String of formatted message for the user.
     */
    public String createMarkAsUndoneMessage(Task task) {
        assert task != null : "Task is null";
        // return MessageFormatter.SEPARATOR + "\n" + "OK, I've marked this task as not done yet:\n  "
        //     + task.toString() + "\n" + MessageFormatter.SEPARATOR;
        return "OK, I've marked this task as not done yet:\n\n" + task.toString();
    }

    /**
     * Formats a message for the user to tell them Task has been removed.
     *
     * @param task Task that has been removed.
     * @param numTasks Number of Tasks left.
     * @return String of formatted message after removal of Task.
     */
    public String createRemoveTaskMessage(Task task, int numTasks) {
        assert task != null : "Task is null";
        assert numTasks >= 0 : "Number of tasks is negative";
        // return MessageFormatter.SEPARATOR + "\n" + "Noted. I've removed this task:\n  "
        //     + task.toString() + "\n" + "Now you have " + numTasks + " tasks in the list."
        //         + "\n" + MessageFormatter.SEPARATOR;
        return "Noted. I've removed this task:\n\n"
            + task.toString() + "\n" + "Now you have " + numTasks + " tasks in the list.";
    }

    /**
     * Format an error message for the user to inform them of an exception that has been caught.
     *
     * @param e Exception that was cause.
     * @return String of formatted message for the user that includes the exception message.
     */
    public String createErrorMessage(Exception e) {
        // return MessageFormatter.SEPARATOR + "\n" + e.getMessage() + "\n" + MessageFormatter.SEPARATOR;
        return e.getMessage();
    }

    /**
     * Private helper method to format the tasks as a numbered list.
     *
     * @param tasks ArrayList of Task for formatting.
     * @return Formatted String of Tasks as a numbered list.
     */
    private String createNumberedTasksString(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list is null";

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
        assert filteredTasks != null : "Filtered task list is null";

        String tasksString = this.createNumberedTasksString(filteredTasks);
        return "Here are the matching tasks in your list:\n\n" + tasksString;
    }
}
