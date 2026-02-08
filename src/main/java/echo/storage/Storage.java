package echo.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import echo.exception.StorageException;
import echo.exception.TaskException;
import echo.task.Deadline;
import echo.task.Event;
import echo.task.Task;
import echo.task.ToDo;

/**
 * Handles the loading and saving of tasks to and from persistent storage.
 * This class manages file I/O operations for the Echo application, including
 * creating necessary directories, reading task data from files, and writing
 * task data back to files.
 *
 * The storage format uses a pipe-delimited text format where each line represents
 * a task with its type, completion status, description, and any date/time information.
 */
public class Storage {
    private static final String ERROR_MESSAGE_INCORRECT_FORMAT =
        "Your data file is formatted incorrectly! Starting with an empty history...";
    private static final String ERROR_MESSAGE_INVALID_TASK_TYPE =
        "Invalid task type when parsing!";

    private final String path;

    /**
     * Constructs a new Storage class
     *
     * @param path String file path to save and load tasks from
     */
    public Storage(String path) {
        this.path = path;
    }

    /**
     * Saves the tasks currently present in the tasks ArrayList into a file with this.path
     *
     * @param tasks ArrayList of Task for the to save
     * @throws IOException If the file exists but is a directory rather than a regular file,
     *     does not exist but cannot be created, or cannot be opened for any other reason
     */
    public void saveTasks(ArrayList<Task> tasks) throws IOException {
        File file = new File(this.path);
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
        for (Task task : tasks) {
            String saveString = task.saveRepresentation();
            // save line with newline
            writer.write(saveString + System.lineSeparator());
        }
        writer.close();
        return;
    }

    /**
     * Loads the tasks from a text file if it exists, else creates new empty file
     *
     * @throws FileNotFoundException if file does not exist.
     * @throws StorageException if there was an invalid task type when parsing.
     * @throws TaskException if there was an error while creating a new Task after parsing.
     */
    public ArrayList<Task> loadTasks() throws FileNotFoundException, StorageException, TaskException {
        File file = new File(this.path);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        boolean canRead = file.canRead();
        ArrayList<Task> tasks = new ArrayList<>();
        if (canRead) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // parse the line
                Task task = this.parseSavedTask(line);
                tasks.add(task);
            }
            scanner.close();
        }
        return tasks;
    }

    /**
     * Read each line in String and parses it to return a new Task
     * Lines that are expected from the file:
     * ToDo: T | 1 | read book
     * Deadline: D | 0 | return book | 2026-01-25 1000
     * Event: E | 1 | project meeting | 2026-01-27 1200 | 2026-01-27 1500
     *
     * @param line String of a task that is currently in the File
     * @return a new Task that has been created after successful parsing and creation
     * @throws StorageException when an invalid task type is detected
     * @throws TaskException if there was an error constructing the new Task after parsing
     */
    private Task parseSavedTask(String line) throws StorageException {
        String[] args = line.split(Task.getSplitPattern());
        String taskMarker = args[Task.getTaskTypeIndex()];

        try {
            if (taskMarker.equals(ToDo.getMarker())) {
                return ToDo.fromSaveFormat(line);
            } else if (taskMarker.equals(Deadline.getMarker())) {
                return Deadline.fromSaveFormat(line);
            } else if (taskMarker.equals(Event.getMarker())) {
                return Event.fromSaveFormat(line);
            } else {
                throw new StorageException(Storage.ERROR_MESSAGE_INVALID_TASK_TYPE);
            }
        } catch (TaskException e) {
            throw new StorageException(e.getMessage() + "\n" + Storage.ERROR_MESSAGE_INCORRECT_FORMAT);
        }
    }

}
// TODO: maybe consider using getters from the tasks to create the format and save the format?
