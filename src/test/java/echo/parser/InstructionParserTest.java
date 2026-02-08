package echo.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import echo.command.Command;
import echo.exception.ParsingException;

public class InstructionParserTest {
    private InstructionParser parser;

    @BeforeEach
    public void setUp() {
        parser = new InstructionParser();
    }

    // NOTE: Tests for parseCommand
    @Test
    public void parseCommand_validTodo_success() throws ParsingException {
        assertEquals(Command.TODO, parser.parseCommand("todo read book"));
    }

    @Test
    public void parseCommand_validTodoWithExtraSpaces_success() throws ParsingException {
        assertEquals(Command.TODO, parser.parseCommand("  todo   read book  "));
    }

    @Test
    public void parseCommand_todoNoDescription_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("todo");
        });
        assertEquals("The todo description cannot be empty leh...", exception.getMessage());
    }

    @Test
    public void parseCommand_validDeadline_success() throws ParsingException {
        assertEquals(Command.DEADLINE, parser.parseCommand("deadline submit report /by 2026-01-25 1800"));
    }

    @Test
    public void parseCommand_deadlineNoDescription_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("deadline");
        });
        assertEquals("got no deadline description and by when you should complete it...", exception.getMessage());
    }

    @Test
    public void parseCommand_deadlineNoBy_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("deadline submit report");
        });
        assertEquals("need to indicate when you need to complete the deadline by!", exception.getMessage());
    }

    @Test
    public void parseCommand_deadlineEmptyDescription_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("deadline /by 2026-01-25 1800");
        });
        assertEquals("got no deadline description...", exception.getMessage());
    }

    @Test
    public void parseCommand_validEvent_success() throws ParsingException {
        assertEquals(Command.EVENT, parser.parseCommand("event meeting /from 2pm /to 4pm"));
    }

    @Test
    public void parseCommand_eventNoDescription_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("event");
        });
        assertEquals("why got no event description one...", exception.getMessage());
    }

    @Test
    public void parseCommand_validList_success() throws ParsingException {
        assertEquals(Command.LIST, parser.parseCommand("list"));
    }

    @Test
    public void parseCommand_validMark_success() throws ParsingException {
        assertEquals(Command.MARK, parser.parseCommand("mark 1"));
    }

    @Test
    public void parseCommand_markNoNumber_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("mark");
        });
        assertEquals("mark requires a task number!", exception.getMessage());
    }

    @Test
    public void parseCommand_markInvalidNumber_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("mark abc");
        });
        assertEquals("mark needs a valid task number!", exception.getMessage());
    }

    @Test
    public void parseCommand_validUnmark_success() throws ParsingException {
        assertEquals(Command.UNMARK, parser.parseCommand("unmark 1"));
    }

    @Test
    public void parseCommand_unmarkNoNumber_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("unmark");
        });
        assertEquals("unmark requires a task number!", exception.getMessage());
    }

    @Test
    public void parseCommand_unmarkInvalidNumber_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("unmark xyz");
        });
        assertEquals("unmark needs a valid task number!", exception.getMessage());
    }

    @Test
    public void parseCommand_validDelete_success() throws ParsingException {
        assertEquals(Command.DELETE, parser.parseCommand("delete 1"));
    }

    @Test
    public void parseCommand_deleteNoNumber_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("delete");
        });
        assertEquals("delete requires a task number!", exception.getMessage());
    }

    @Test
    public void parseCommand_deleteInvalidNumber_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("delete notanumber");
        });
        assertEquals("delete needs a valid task number!", exception.getMessage());
    }

    @Test
    public void parseCommand_validBye_success() throws ParsingException {
        assertEquals(Command.BYE, parser.parseCommand("bye"));
    }

    @Test
    public void parseCommand_unknownCommand_exceptionThrown() {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseCommand("unknown");
        });
        assertEquals("Sorry what does that mean ah? I never see unknown before...", exception.getMessage());
    }

    // NOTE: Test for parseMarkUnmarkArgs
    @Test
    public void parseMarkUnmarkArgs_validInput_success() {
        assertEquals(1, parser.parseMarkUnmarkArgs("mark 1"));
        assertEquals(5, parser.parseMarkUnmarkArgs("unmark 5"));
    }

    // NOTE: Test for parseDeleteArgs
    @Test
    public void parseDeleteArgs_validInput_success() {
        assertEquals(1, parser.parseDeleteArgs("delete 1"));
        assertEquals(10, parser.parseDeleteArgs("delete 10"));
    }

    // NOTE: Test for parseTodoDescription
    @Test
    public void parseTodoDescription_validInput_success() throws ParsingException {
        assertEquals("read book", parser.parseTodoDescription("todo read book"));
        assertEquals("buy groceries", parser.parseTodoDescription("todo buy groceries"));
    }

    @Test
    public void parseTodoDescription_emptyString_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseTodoDescription("");
        });
        assertEquals("User message cannot be empty!", exception.getMessage());
    }

    @Test
    public void parseTodoDescription_noEventDescription_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseTodoDescription("todo ");
        });
        assertEquals("The todo description cannot be empty leh...", exception.getMessage());
    }

    // NOTE: Test for parseTodoArgs
    @Test
    public void parseTodoArgs_validInput_returnsEmptyList() {
        ArrayList<String> result = parser.parseTodoArgs("todo read book");
        assertEquals(0, result.size());
    }

    // No empty string test for parseTodoArgs since we dont extract information from string in any way

    // NOTE: Test for parseDeadlineDescription
    @Test
    public void parseDeadlineDescription_validInput_success() throws ParsingException {
        assertEquals("submit report",
            parser.parseDeadlineDescription("deadline submit report /by 2026-01-25 1800"));
        assertEquals("finish homework",
            parser.parseDeadlineDescription("deadline finish homework /by tomorrow"));
    }

    @Test
    public void parseDeadlineDescription_emptyString_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseDeadlineDescription("");
        });
        assertEquals("User message cannot be empty!", exception.getMessage());
    }

    @Test
    public void parseDeadlineDescription_noDeadlineDescription_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseDeadlineDescription("deadline /by 2026-01-01 1600");
        });
        assertEquals("got no deadline description...", exception.getMessage());
    }

    // NOTE: Test for parseDeadlineArgs
    @Test
    public void parseDeadlineArgs_validInput_success() throws ParsingException {
        ArrayList<String> result = parser
            .parseDeadlineArgs("deadline submit report /by 2026-01-25 1800");
        assertEquals(1, result.size());
        assertEquals("2026-01-25 1800", result.get(0));
    }

    @Test
    public void parseDeadlineArgs_emptyString_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseDeadlineArgs("");
        });
        assertEquals("User message cannot be empty!", exception.getMessage());
    }

    @Test
    public void parseDeadlineArgs_noBy_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseDeadlineArgs("deadline submit report");
        });
        assertEquals("need to indicate when you need to complete the deadline by!", exception.getMessage());
    }

    @Test
    public void parseDeadlineArgs_multipleBy_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseDeadlineArgs("deadline submit report /by 2026-01-25 1800 /by 2026-01-25 1800");
        });
        assertEquals("Did you specify more than one /by for the deadline?", exception.getMessage());
    }

    // NOTE: Test for parseEventDescription
    @Test
    public void parseEventDescription_validInput_success() throws ParsingException {
        assertEquals("project meeting",
            parser.parseEventDescription("event project meeting /from 2pm /to 4pm"));
        assertEquals("conference",
            parser.parseEventDescription("event conference /from Mon 2pm /to Mon 6pm"));
    }

    @Test
    public void parseEventDescription_emptyString_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseEventDescription("");
        });
        assertEquals("User message cannot be empty!", exception.getMessage());
    }

    @Test
    public void parseEventDescription_noEventDescription_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseEventDescription("event /from Mon 2pm /to Mon 6pm");
        });
        assertEquals("why got no event description one...", exception.getMessage());
    }

    // NOTE: Test for parseEventArgs
    @Test
    public void parseEventArgs_validInput_success() throws ParsingException {
        ArrayList<String> result = parser
            .parseEventArgs("event project meeting /from 2026-01-25 1400 /to 2026-01-25 1600");
        assertEquals(2, result.size());
        assertEquals("2026-01-25 1400", result.get(0));
        assertEquals("2026-01-25 1600", result.get(1));
    }

    @Test
    public void parseEventArgs_emptyString_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseDeadlineArgs("");
        });
        assertEquals("User message cannot be empty!", exception.getMessage());
    }

    @Test
    public void parseEventArgs_missingFrom_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseEventArgs("event project meeting /to 4pm");
        });
        assertEquals("Did you forget to specify /from for the event?", exception.getMessage());
    }

    @Test
    public void parseEventArgs_missingTo_exceptionThrown() throws ParsingException {
        ParsingException exception = assertThrows(ParsingException.class, () -> {
            parser.parseEventArgs("event project meeting /from 2pm");
        });
        assertEquals("Did you forget to specify /to for the event?", exception.getMessage());
    }
}
