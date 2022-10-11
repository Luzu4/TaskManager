package pl.coderslab;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private static final String TASKS_FILE_NAME = "tasks.csv";
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        menu(input);
    }

    public static void menu(Scanner input){
        String userChoice ="";
        while(!"exit".equals(userChoice)){
            System.out.println(ConsoleColors.BLUE + "Please select an option: ");
            System.out.println(ConsoleColors.WHITE + """
                    add
                    remove
                    list
                    exit""");

            System.out.println("Write one from above options: ");
           userChoice = input.next();
            switch (userChoice.toLowerCase()) {
                case "add":
                    addTask(TASKS_FILE_NAME, input);
                    break;
                case "remove":
                    removeTask(input);
                    break;
                case "list":
                    showTasks(tasks(TASKS_FILE_NAME));
                    break;
                case "exit":
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    break;
                default:
                    System.out.println("Try one more time, wrong option.");
            }
        }
    }

    public static String[][] tasks(String fileName){
        Path tasksFilePath = Paths.get(fileName);
        try{
            List<String> allLinesFromFile = Files.readAllLines(tasksFilePath);
            String[][] tasksFromFile = new String[allLinesFromFile.size()][allLinesFromFile.get(1).split(",").length];
            for(int i = 0; i<allLinesFromFile.size();i++){
                for(int j = 0 ; j<allLinesFromFile.get(i).split(",").length; j++){
                    tasksFromFile[i][j] = allLinesFromFile.get(i).split(",")[j];
                }
            }
            return tasksFromFile;
        }catch(IOException e){
            System.out.println("Cant find file with name: " + fileName);
            return null;
        }

    }

    public static void addTask(String fileName, Scanner input){
        Path tasksFilePath = Paths.get(fileName);
        try{
            input.nextLine();
            System.out.println("Please add task description: ");
            String taskDescription = input.nextLine();
            System.out.println("Please add task due date");
            String taskDueDate = input.next();
            System.out.println("Is your tasks is important: true/false");
            boolean important = input.nextBoolean();
            Files.writeString(tasksFilePath, "\n" + taskDescription +", " + taskDueDate +", " + important, StandardOpenOption.APPEND);
        }catch(IOException e){
            System.out.println("Something Went Wrong");
        }
    }

    public static void showTasks(String[][] tasks){
        for(int i = 0; i < tasks.length;i++){
            System.out.print(i+ " : ");
            for(int j = 0; j< tasks[i].length; j++){
                System.out.print(tasks[i][j]);
            }
            System.out.println();
        }
    }

    public static void removeTask(Scanner input){
        System.out.println("Please select number to remove: ");
        int userChoice = input.nextInt();
        Path path = Paths.get(TASKS_FILE_NAME);
        try{
            List<String> linesFromTasks = Files.readAllLines(path);
            linesFromTasks.remove(userChoice);
            Files.writeString(path,"");
            for(String line : linesFromTasks){
                Files.writeString(path,line + "\n",StandardOpenOption.APPEND);
            }
        }catch(IOException e){
            System.out.println("Something went wrong");
        }

    }

}
