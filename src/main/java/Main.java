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
            String botMessage;

            if (userMessage.equals("bye")) {
                System.out.println(echo.exitUser());
                break;
            } else if (userMessage.startsWith("mark ")) {
                String[] parts = userMessage.split(" ");
                int taskNumber = Integer.parseInt(parts[1]);
                botMessage = echo.markAsDone(taskNumber);
            } else if (userMessage.startsWith("unmark ")) {
                String[] parts = userMessage.split(" ");
                int taskNumber = Integer.parseInt(parts[1]);
                botMessage = echo.markAsUndone(taskNumber);
            } else if (userMessage.equals("list")) {
                botMessage = echo.getTasks();
            } else {
                botMessage = echo.addTask(userMessage);
            }

            System.out.println(botMessage);
        }

        scanner.close();
    }
}
