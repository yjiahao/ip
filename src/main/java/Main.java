package main.java;

import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        // initialize hello message by Echo
        String greeting = echo.greetUser();
        System.out.println(greeting);

        while (true) {
            String userMessage = scanner.nextLine();
            if (userMessage.equals("bye")) {
                System.out.println(echo.exitUser());
                break;
            } else {
                System.out.println(echo.replyUser(userMessage));
            }
        }
        
        scanner.close();
        // System.out.println(echo.greetAndExit());
    }
}
