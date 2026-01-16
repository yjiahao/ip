package main.java;

public class Echo {

    private static String greeting = "Hello! I'm Echo\n" + "What can I do for you?";
    private static String exitMessage = "Bye. Hope to see you again soon!";
    private static String separator = "____________________________________________________________";

    public Echo() {

    }

    private String greetUser() {
        return Echo.greeting;
    }

    private String exitUser() {
        return Echo.exitMessage;
    }

    /**
     * Greets the user and exits immediately after
     * @return Greeting and exit message before exiting
     */
    public String greetAndExit() {
        String greeting = this.greetUser();
        String exitString = this.exitUser();

        String greetingAndExit = Echo.separator + "\n" + greeting + "\n" +
            Echo.separator + "\n" + exitString + "\n" + Echo.separator;
        return greetingAndExit;
    }
}
