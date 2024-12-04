package Game;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Collections.Lists.UnorderedArrayList;
import Exceptions.InvalidAction;
import Game.CombatHandler;

import java.util.Scanner;

public class Mission {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Improbable Mission game!");
        String filepath = "src/main/resources/Missão.json";

        Building building = new Building(filepath);
        Goal goal = building.getGoal();
        Division startDivision = building.getInAndOut().first();
        ToCruz player = new ToCruz("Tó Cruz", startDivision);

        System.out.println("\nRegistered divisions in the graph:");
        for (Division division : building.getMap().getVertexes()) {
            System.out.println("- " + division.getName());
        }

        CombatHandler combatHandler = new CombatHandler();


        System.out.println("\nGame has started!");
        System.out.println("You are currently in the division: " + startDivision.getName());
        System.out.println("Objective: " + goal.getType() + " in the division " + goal.getDivision().getName());

        boolean gameRunning = true;


        while (gameRunning) {

            if (!combatHandler.getEnemiesInDivision(player, building.getEnemies()).isEmpty()) {
                System.out.println("\n--------------------------------------");
                System.out.println("Current Division: " + player.getCurrentDivision().getName());
                System.out.println("Health: " + player.getLifePoints());
                System.out.println("Items in the Bag: " + player.getBag().size());
                System.out.println("--------------------------------------");
                System.out.println("2. Attack enemies (Scenario 1)");
                System.out.println("3. Use an item (Scenario 4)");
            }
            else {
                System.out.println("\n--------------------------------------");
                System.out.println("Current Division: " + player.getCurrentDivision().getName());
                System.out.println("Health: " + player.getLifePoints());
                System.out.println("Items in the Bag: " + player.getBag().size());
                System.out.println("--------------------------------------");
                System.out.println("Choose an action:");
                System.out.println("1. Move to another division");
                System.out.println("2. Attack enemies (Scenario 1)");
                System.out.println("3. Use an item (Scenario 4)");
                System.out.println("4. Search for the objective (Scenario 5 or 6)");
                System.out.println("5. Exit the game");
                Division currentDivision = player.getCurrentDivision();
                System.out.println("Connected divisions:");
                for (Division neighbor : building.getMap().getEdges(currentDivision)) {
                    System.out.println("- " + neighbor.getName());
                }
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:

                    int option = 0;
                    LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
                    System.out.println("Connected divisions:");
                    for (Division neighbor : building.getMap().getEdges(player.getCurrentDivision())) {
                        System.out.println(++option + ". " + neighbor.getName());
                    }

                    System.out.print("Enter the option to the division: ");
                    int divisionOption = Integer.parseInt(scanner.nextLine());
                    try {
                        combatHandler.scenario2(player, building, neighbors, divisionOption);
                    } catch (InvalidAction e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:

                    try{
                        combatHandler.scenario1(player, building);
                    } catch (InvalidAction e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:

                    try{
                        combatHandler.scenario4(player, building);
                    } catch (InvalidAction e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:

                    if (player.getCurrentDivision().equals(goal.getDivision())) {
                        combatHandler.scenario6(player, building, goal);
                    } else {
                        System.out.println("You are not in the objective division yet.");
                    }
                    break;

                case 5:

                    System.out.println("Exiting the game. See you next time!");
                    gameRunning = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

            if (!player.isAlive()) {
                System.out.println("Tó Cruz has died. Game over.");
                gameRunning = false;
            } else if (goal.isRequired() && player.getCurrentDivision().equals(building.getInAndOut().last())) {
                System.out.println("Congratulations! You have successfully completed the mission!");
                gameRunning = false;
            }
        }

        scanner.close();
    }
}
