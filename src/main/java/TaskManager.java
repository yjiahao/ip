package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {

    private static final String FILE_PATH = "data/echo.txt";
    private ArrayList<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task to the task manager.
     * @param taskDescription description of task to be added.
     */
    public String addTask(String taskDescription, Command type, ArrayList<String> commandArgs) {
        if (type.equals(Command.EVENT)) {
            Task event = new Event(taskDescription, commandArgs.get(0), commandArgs.get(1));
            this.tasks.add(event);
            return event.toString();
        } else if (type.equals(Command.TODO)) {
            Task todo = new ToDo(taskDescription);
            this.tasks.add(todo);
            return todo.toString();
        } else {
            Task deadline = new Deadline(taskDescription, commandArgs.get(0));
            this.tasks.add(deadline);
            return deadline.toString();
        }
    }

    private void checkNotOutOfBounds(int taskNumber) {
        if (taskNumber > this.tasks.size()) {
            throw new IndexOutOfBoundsException("You do not have " + taskNumber + " tasks yet...");
        } else if (taskNumber <= 0) {
            throw new IndexOutOfBoundsException("No such thing as task " + taskNumber + "!");
        }
    }

    /**
     * Removes a Task from task list and returns it.
     * @param taskNumber the 1-indexed Task that is to be removed.
     * @return the Task that was removed.
     */
    public String removeTask(int taskNumber) {
        this.checkNotOutOfBounds(taskNumber);
        Task removedTask = this.tasks.remove(taskNumber - 1);
        return removedTask.toString();
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
        this.checkNotOutOfBounds(taskNumber);
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
        this.checkNotOutOfBounds(taskNumber);
        Task t = this.tasks.get(taskNumber - 1);
        t.markAsUndone();
        return t.toString();
    }

    public int getNumTasks() {
        return this.tasks.size();
    }

    /**
     * Save the tasks currently present in the tasks ArrayList
     * @throws IOException
     */
    public void saveTasks() throws IOException {
        File file = new File(TaskManager.FILE_PATH);
        // make parent directory if not exists
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        // clear contents of file first
        FileWriter clearWriter = new FileWriter(file, false);
        clearWriter.close();

        // append to the empty file
        FileWriter writer = new FileWriter(file, true);
        for (Task task : this.tasks) {
            String saveString = task.saveRepresentation();
            // save line with newline
            writer.write(saveString + System.lineSeparator());
        }
        writer.close();
        return;
    }

    /**
     * Load the tasks from a text file if it exists, else creates new empty file
     * @throws IOException, TaskManagerException
     */
    public void loadTasks() throws FileNotFoundException, TaskManagerException {
        File file = new File(TaskManager.FILE_PATH);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        boolean canRead = file.canRead();
        if (canRead) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // parse the line
                Task task = this.parseSavedTask(line);
                this.tasks.add(task);
            }
            scanner.close();
        }
        return;
    }

    /**
     * Read each line in String and parses it to return a new Task
     * Lines that are expected from the file:
     * ToDo: T | 1 | read book
     * Deadline: D | 0 | return book | June 6th
     * Event: E | 1 | project meeting | Aug 6th 2pm | 4pm
     * @param line String of a task that is currently in the File
     * @return a new Task that has been created after successful parsing and creation
     * @throws TaskManagerException when an invalid task type is detected
     */
    // TODO: handle this exception in the Main
    // TODO: we can check the length of args also to check the validity of each line
    private Task parseSavedTask(String line) throws TaskManagerException {
        String[] args = line.split(" \\| ");
        // check first arg to determine task type
        if (args[0].equals("T")) {
            // create new ToDo
            Task todo = new ToDo(args[2]);
            if (args[1].equals("1")) {
                todo.markAsDone();
            }
            return todo;
        } else if (args[0].equals("D")) {
            // create new Deadline
            Task deadline = new Deadline(args[2], args[3]);
            if (args[1].equals("1")) {
                deadline.markAsDone();
            }
            return deadline;
        } else if (args[0].equals("E")) {
            // create new Event
            Task event = new Event(args[2], args[3], args[4]);
            if (args[1].equals("1")) {
                event.markAsDone();
            }
            return event;
        } else {
            throw new TaskManagerException("Invalid task type when parsing!");
        }
    }
}
