package main.java;

public class Echo {
    public static void main(String[] args) {

        String greeting = "Hello! I'm Echo\n" + "What can I do for you?\n";
        String exitMessage = "Bye. Hope to see you again soon!\n";
        String separator = "____________________________________________________________";
        String finalString = separator + "\n" + greeting + separator + "\n" + exitMessage + separator;
        System.out.println(finalString);
    }
}
