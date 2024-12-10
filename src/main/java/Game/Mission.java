package Game;

import Classes.*;

import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;

import java.util.Scanner;

public class Mission {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Improbable Mission game!");

        System.out.println("Available missions:");
        System.out.println("1. Missao_v1.json");
        System.out.println("2. Missao_v2.json");
        System.out.println("3. Missao_v3.json");

        System.out.println("Choose a mission:");
        int missionChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String selectedMissionFile = null;
        switch (missionChoice) {
            case 1:
                selectedMissionFile = "src/main/resources/Missão_v1.json";
                break;
            case 2:
                selectedMissionFile = "src/main/resources/Missão_v2.json";
                break;
            case 3:
                selectedMissionFile = "src/main/resources/Missão_v3.json";
                break;
            default:
                System.out.println("Invalid mission choice.");
                return;
        }

        System.out.println("Selected mission file: " + selectedMissionFile);

        String filepath = selectedMissionFile;

        Building building = new Building(filepath);
        Goal goal = building.getGoal();
        Division startDivision = null;
        ToCruz player = new ToCruz("Tó Cruz");


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
                            String log = combatHandler.scenario1(player, building);
                            System.out.println(log);
                        } else {
                            int option = 1;
                            LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
                            System.out.println("Choose a division to move:");
                            for (Division neighbor : neighbors) {
                                System.out.println(option++ + ". " + neighbor.getName());
                            }
                            int divisionOption = scanner.nextInt();
                            String log = combatHandler.scenario2(player, building, neighbors, divisionOption);
                            System.out.println(log);
                        }
                        break;

                    case 2: // Uso de item
                        String log = combatHandler.scenario4(player, building);
                        System.out.println(log);
                        break;

                    case 3: // Procura pelo objetivo
                        if (player.getCurrentDivision().equals(goal.getDivision())) {
                            log = combatHandler.scenario6(player, building, goal);
                            System.out.println(log);
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
                gameRunning = false;
            }

            // Exibe o melhor caminho para o objetivo e para o kit de recuperação mais próximo
            building.displayPaths(player.getCurrentDivision());
        }

        scanner.close();
    }
}