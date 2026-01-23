package echo.exception;

/**
 * Exceptions related to the InstructionParser class
 */
public class ParsingException extends Exception {
    /**
     * Constructor for ParsingException
     * @param message Message for the exception
     */
    public ParsingException(String message) {
        super(message);
    }
}
