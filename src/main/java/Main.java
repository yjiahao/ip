package main.java;

/**
 * Main entry point for the chatbot.
 * This class initializes and runs the Echo program which greets the user
 * and displays an exit message.
 */
public class Main {
    /**
     * Main method that starts the chatbot.
     * Creates an Echo instance and prints the greeting and exit messages.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Echo echo = new Echo();
        System.out.println(echo.greetAndExit());
    }
}
