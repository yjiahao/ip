package main.java;

import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task to the task manager.
     * @param taskDescription description of task to be added.
     */
    public Task addTask(String taskDescription, Command type, ArrayList<String> commandArgs) {
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
     * Get all tasks in the task manager and format them into a string.
     * @return a string of numbered tasks, separated by newlines.
     */
    public String getTasks() {
        String res = "";
        int i = 1;
        for (Task t : this.tasks) {
            res = res + i + ". " + t + "\n";
            i += 1;
        }
        return res.stripTrailing();
    }

    /**
     * Marks a task as done.
     * @param taskNumber the task number (1-indexed) to mark as done.
     * @return the task that was marked as done in String.
     */
    public String markAsDone(int taskNumber) {
        // array is 0 indexed so need to translate by 1
        Task t = this.tasks.get(taskNumber - 1);
        t.markAsDone();
        return t.toString();
    }

    /**
     * Marks a task as undone.
     * @param taskNumber the task number (1-indexed) to mark as undone.
     * @return the task that was marked as undone in String.
     */
    public String markAsUndone(int taskNumber) {
        Task t = this.tasks.get(taskNumber - 1);
        t.markAsUndone();
        return t.toString();
    }

    public int getNumTasks() {
        return this.tasks.size();
    }
}
