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
    public void addTask(String taskDescription) {
        Task newTask = new Task(taskDescription);
        this.tasks.add(newTask);
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
}
