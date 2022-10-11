package pl.coderslab;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class TaskManager {
    private static final String TASKS_FILE_NAME = "tasks.csv";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        menu(input);
    }

    public static void menu(Scanner input) {
        String userChoice = "";
        String[][] tasksFromFile = tasks(TASKS_FILE_NAME);
        while (!"exit".equals(userChoice)) {
            System.out.println(ConsoleColors.BLUE + "Please select an option: ");
            System.out.println(ConsoleColors.WHITE + """
                    add
                    remove
                    list
                    exit""");

            System.out.println("Write one from above options: ");
            userChoice = input.next();
            switch (userChoice.toLowerCase().replaceAll(" ", "")) {
                case "add" -> tasksFromFile = addTask(tasksFromFile, input);
                case "remove" -> {
                    showTasks(Objects.requireNonNull(tasksFromFile));
                    tasksFromFile = removeTask(tasksFromFile, input);
                }
                case "list" -> showTasks(Objects.requireNonNull(tasksFromFile));
                case "exit" -> exit(Objects.requireNonNull(tasksFromFile));
                default -> System.out.println("Try one more time, wrong option.");
            }
            input.nextLine();
        }
    }

    public static String[][] tasks(String fileName) {
        Path tasksFilePath = Paths.get(fileName);
        try {
            List<String> allLinesFromFile = Files.readAllLines(tasksFilePath);
            String[][] tasksFromFile = new String[allLinesFromFile.size()][allLinesFromFile.get(1).split(",").length];
            for (int i = 0; i < allLinesFromFile.size(); i++) {
                for (int j = 0; j < allLinesFromFile.get(i).split(",").length; j++) {
                    tasksFromFile[i][j] = allLinesFromFile.get(i).split(",")[j];
                }
            }
            return tasksFromFile;
        } catch (IOException e) {
            System.out.println("Cant find file with name: " + fileName);
            return null;
        }

    }

    public static String[][] addTask(String[][] tasksFromFile, Scanner input) {
        input.nextLine();
        System.out.println("Please add task description: ");
        String taskDescription = input.nextLine();

        System.out.println("Please add task due date(\"YYYY-MM-DD\"");
        String taskDueDate = input.next();
        while (!taskDueDate.matches("\\d\\d\\d\\d-1[0-2]-[0-3]\\d") && !taskDueDate.matches("\\d\\d\\d\\d-0\\d-[0-3]\\d")) {
            input.nextLine();
            System.out.println("Please add task due date(\"YYYY-MM-DD\"");
            taskDueDate = input.next();
        }


        System.out.println("Is your tasks is important: true/false");
        String important;
        important = input.next();
        while (!"true".equalsIgnoreCase(important) && !"false".equalsIgnoreCase(important)) {
            input.nextLine();
            System.out.println("Please type \"true\" or \"false\"");
            important = input.next();
        }
        tasksFromFile = Arrays.copyOf(tasksFromFile, tasksFromFile.length + 1);
        tasksFromFile[tasksFromFile.length - 1] = new String[]{taskDescription, " " + taskDueDate, " " + important};
        return tasksFromFile;
    }

    public static void showTasks(String[][] tasks) {
        for (int i = 0; i < tasks.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tasks[i].length; j++) {
                System.out.print(tasks[i][j]);
            }
            System.out.println();
        }
    }

    public static String[][] removeTask(String[][] tasksFromFile, Scanner input) {
        System.out.println("Please select number to remove: ");
        int userChoice = 9999;
        while (userChoice > tasksFromFile.length) {
            input.nextLine();
            if (input.hasNextInt()) {
                userChoice = input.nextInt();
            } else {
                System.out.println("That is not Integer");
            }
            System.out.println("Index of task is out of range, try one more time: ");
        }

        String[][] newTasksFromFile = Arrays.copyOf(tasksFromFile, tasksFromFile.length - 1);
        for (int i = 0, j = 0; i < tasksFromFile.length; i++) {
            if (i != userChoice) {
                newTasksFromFile[j] = tasksFromFile[i];
                j++;
            }
        }
        return newTasksFromFile;

    }

    public static void exit(String[][] tasksFromFile) {
        Path path = Paths.get(TASKS_FILE_NAME);
        try {
            Files.writeString(path, "");
            for (String[] strings : tasksFromFile) {
                Files.writeString(path, strings[0] + "," + strings[1] + "," + strings[2] + "\n", StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
        System.out.println(ConsoleColors.RED + "Bye, bye.");
    }

}
