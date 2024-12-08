package Game;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Exceptions.InvalidAction;

import java.util.Scanner;

public class Mission {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Improbable Mission game!");
        String filepath = "src/main/resources/Missão.json";

        Building building = new Building(filepath);
        Goal goal = building.getGoal();
        Division startDivision = null;
        ToCruz player = new ToCruz("Tó Cruz");

        System.out.println("\nRegistered divisions in the graph:");
        for (Division division : building.getMap().getVertexes()) {
            System.out.println("- " + division.getName());
        }

        CombatHandler combatHandler = new CombatHandler();

        System.out.println("\nGame has started!");

        int entranceOption = 1;
        System.out.println("Choose entrance division:");
        for (Division entrance : building.getInAndOut()) {
            System.out.println(entranceOption++ + ". " + entrance.getName());
        }
        int entranceChoice = scanner.nextInt();
        startDivision = combatHandler.startDivision(player, building, building.getInAndOut(), entranceChoice);
        System.out.println("You are currently in the division: " + startDivision.getName());
        System.out.println("Objective: " + goal.getType() + " in the division " + goal.getDivision().getName());

        boolean gameRunning = true;

        while (gameRunning) {

            // Atualiza a interface do jogador
            System.out.println("\n--------------------------------------");
            System.out.println("Current Division: " + player.getCurrentDivision().getName());
            System.out.println("Health: " + player.getLifePoints());
            System.out.println("Items in the Bag: " + player.getBag().size());
            System.out.println("--------------------------------------");

            // Verifica se há inimigos na divisão
            LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();
            if (!currentEnemies.isEmpty()) {
                System.out.println("Enemies detected in your current division!");
                System.out.println("1. Attack enemies (Scenario 1)");
                System.out.println("2. Use an item (Scenario 4)");
                System.out.println("3. Exit the game");
            } else {
                System.out.println("Choose an action:");
                System.out.println("1. Move to another division");
                System.out.println("2. Use an item (Scenario 4)");
                System.out.println("3. Search for the objective (Scenario 5 or 6)");
                for (Division exit : building.getInAndOut()) {
                    if (player.getCurrentDivision().equals(exit)) {
                        System.out.println("4. Exit Building");
                    }
                }
                System.out.println("0. Exit the game");
                System.out.println("\nConnected divisions:");
                LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
                int option = 1;
                for (Division neighbor : neighbors) {
                    System.out.println(option++ + ". " + neighbor.getName());
                }
            }

            // Processa a escolha do jogador
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1: // Movimentação ou ataque de inimigos
                        if (!currentEnemies.isEmpty()) {
                            combatHandler.scenario1(player, building);
                        } else {
                            int option = 1;
                            LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
                            System.out.println("Choose a division to move:");
                            for (Division neighbor : neighbors) {
                                System.out.println(option++ + ". " + neighbor.getName());
                            }
                            int divisionOption = scanner.nextInt();
                            combatHandler.scenario2(player, building, neighbors, divisionOption);
                        }
                        break;

                    case 2: // Uso de item
                        combatHandler.scenario4(player, building);
                        break;

                    case 3: // Procura pelo objetivo
                        if (player.getCurrentDivision().equals(goal.getDivision())) {
                            combatHandler.scenario6(player, building, goal);
                        } else {
                            System.out.println("You are not in the objective division yet.");
                        }
                        break;

                    case 4: // Sair do edificio
                        System.out.println("Exiting Building");
                        if (goal.isRequired()) {
                            System.out.println("Congratulation!! \nMission completed with successs :)");
                        }
                        else {
                            System.out.println("Oh no!! \nYou didn't reach the goal. Mission failed :(");
                        }
                        gameRunning = false;
                        break;

                    case 0: // Saída do jogo
                        System.out.println("Exiting the game. See you next time!");
                        gameRunning = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InvalidAction e) {
                System.out.println(e.getMessage());
            }

            // Verifica condições de vitória ou derrota
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
