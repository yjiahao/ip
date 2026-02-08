package echo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import echo.command.Command;
import echo.exception.TaskException;
import echo.exception.TaskManagerException;

public class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new TaskManager();
    }

    // NOTE: tests for Todo tasks
    @Test
    public void addTask_validTodo_success() throws TaskException, TaskManagerException {
        String taskString = taskManager.addTask("read book", Command.TODO, new ArrayList<>());
        assertEquals("[T][ ] read book", taskString);
        assertEquals(1, taskManager.getNumTasks());
    }

    @Test
    public void addTask_invalidTodo_exceptionThrown() throws TaskException {
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("", Command.TODO, new ArrayList<>());
        });
        assertEquals("Task description cannot be empty!", exception.getMessage());
    }

    // NOTE: tests for Deadline tasks
    @Test
    public void addTask_validDeadline_success() throws TaskException, TaskManagerException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-01-23 1800"));
        String taskString = taskManager.addTask("sweep floor", Command.DEADLINE, deadlineArrayList);
        assertEquals("[D][ ] sweep floor (by: 23 Jan 2026 1800)", taskString);
    }

    @Test
    public void addTask_invalidDeadlineTime_exceptionThrown() throws TaskException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-01-23 180"));
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("sweep floor", Command.DEADLINE, deadlineArrayList);
        });
        assertEquals("Date is in the wrong format! Must be in yyyy-mm-dd HHmm", exception.getMessage());
    }

    @Test
    public void addTask_invalidDeadlineDate_exceptionThrown() throws TaskException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-01-6 1800"));
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("sweep floor", Command.DEADLINE, deadlineArrayList);
        });
        assertEquals("Date is in the wrong format! Must be in yyyy-mm-dd HHmm", exception.getMessage());
    }

    // NOTE: tests for Event task
    @Test
    public void addTask_validEvent_success() throws TaskException, TaskManagerException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-02-23 1800", "2026-02-24 1800"));
        String taskString = taskManager.addTask("Attend Conference", Command.EVENT, deadlineArrayList);
        assertEquals("[E][ ] Attend Conference (from: 23 Feb 2026 1800 to: 24 Feb 2026 1800)", taskString);
    }

    @Test
    public void addTask_invalidEventStartTime_exceptionThrown() throws TaskException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-02-23 180", "2026-02-24 1800"));
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("Attend Conference", Command.EVENT, deadlineArrayList);
        });
        assertEquals("Date is in the wrong format! Must be in yyyy-mm-dd HHmm", exception.getMessage());
    }

    @Test
    public void addTask_invalidEventEndTime_exceptionThrown() throws TaskException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-02-23 1800", "2026-02-24 150"));
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("Attend Conference", Command.EVENT, deadlineArrayList);
        });
        assertEquals("Date is in the wrong format! Must be in yyyy-mm-dd HHmm", exception.getMessage());
    }

    @Test
    public void addTask_invalidEventStartDate_exceptionThrown() throws TaskException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-02-2 1800", "2026-02-24 1500"));
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("Attend Conference", Command.EVENT, deadlineArrayList);
        });
        assertEquals("Date is in the wrong format! Must be in yyyy-mm-dd HHmm", exception.getMessage());
    }

    @Test
    public void addTask_invalidEventEndDate_exceptionThrown() throws TaskException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-02-23 1800", "2026-02-2 1500"));
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("Attend Conference", Command.EVENT, deadlineArrayList);
        });
        assertEquals("Date is in the wrong format! Must be in yyyy-mm-dd HHmm", exception.getMessage());
    }

    @Test
    public void addTask_invalidEventEndDateBeforeStartDate_exceptionThrown() throws TaskException {
        ArrayList<String> deadlineArrayList = new ArrayList<>(List.of("2026-02-25 1800", "2026-02-24 1500"));
        TaskException exception = assertThrows(TaskException.class, () -> {
            taskManager.addTask("Attend Conference", Command.EVENT, deadlineArrayList);
        });
        assertEquals("Start date cannot be later than end date!", exception.getMessage());
    }

    // NOTE: Testing the markTask() method
    @Test
    public void markTask_taskAdded_success() throws TaskException, TaskManagerException {
        taskManager.addTask("read book", Command.TODO, new ArrayList<>());
        Task markedTask = taskManager.markAsDone(1);
        assertEquals("[T][X] read book", markedTask.toString());
    }

    @Test
    public void markTask_noTasksAddedYet_exceptionThrown() throws TaskException, TaskManagerException {
        TaskManagerException exception = assertThrows(TaskManagerException.class, () -> {
            taskManager.markAsDone(1);
        });
        assertEquals("You do not have 1 tasks yet...", exception.getMessage());
    }

    @Test
    public void markTask_negativeTaskNumber_exceptionThrown() throws TaskException, TaskManagerException {
        TaskManagerException exception = assertThrows(TaskManagerException.class, () -> {
            taskManager.markAsDone(-10);
        });
        assertEquals("No such thing as task -10!", exception.getMessage());
    }

    @Test
    public void anotherDummyTest() {
        assertEquals(4, 4);
    }
}
