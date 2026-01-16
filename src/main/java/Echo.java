package main.java;

public class Echo {

    private static String greeting = "Hello! I'm Echo\n" + "What can I do for you?";
    private static String exitMessage = "Bye. Hope to see you again soon!";
    private static String separator = "____________________________________________________________";

    public Echo() {

    }

    public String greetUser() {
        return Echo.separator + "\n" + Echo.greeting + "\n" + Echo.separator + "\n";
    }

    public String exitUser() {
        return Echo.separator + "\n" + Echo.exitMessage + "\n" + Echo.separator + "\n";
    }

    public String replyUser(String s) {
        return Echo.separator + "\n" + s + "\n" + Echo.separator + "\n";
    }
}
