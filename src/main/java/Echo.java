package main.java;

public class Echo {

    private static String GREETING = "Hello! I'm Echo\n" + "What can I do for you?";
    private static String EXIT_MESSAGE = "Bye. Hope to see you again soon!";
    private static String SEPARATOR = "____________________________________________________________";

    public Echo() {

    }

    private String greetUser() {
        return Echo.GREETING;
    }

    private String exitUser() {
        return Echo.EXIT_MESSAGE;
    }

    public String greetAndExit() {
        String greeting = this.greetUser();
        String exitString = this.exitUser();

        String greetingAndExit = Echo.SEPARATOR + "\n" + greeting + "\n" +
            Echo.SEPARATOR + "\n" + exitString + "\n" + Echo.SEPARATOR;
        return greetingAndExit;
    }
}
