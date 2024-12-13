package Game;

import Classes.*;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;
import Game.CombatHandler;
import Interfaces.Action;

import java.util.Scanner;

public class Mission {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Improbable Mission game!");

        System.out.println("Available missions:");
        System.out.println("1. Pata de Coelho");
        System.out.println("2. Operation Stealth");
        System.out.println("3. Infiltration");

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
        ToCruz player = new ToCruz("Tó Cruz");

        CombatHandler combatHandler = new CombatHandler();

        System.out.println("\nChoose mode:");
        System.out.println("1. Manual");
        System.out.println("2. Automatic");
        int modeChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (modeChoice == 2) {
            PathResult result = building.simulatePath(player, building.getInAndOut(), goal);
            logPathResult(result);
            return;
        }

        // Modo manual
        System.out.println("\nGame has started!");

        int entranceOption = 1;
        System.out.println("Choose entrance division:");
        for (Division entrance : building.getInAndOut()) {
            System.out.println(entranceOption++ + ". " + entrance.getName());
        }
        int entranceChoice = scanner.nextInt();
        combatHandler.startDivision(player, building, building.getInAndOut(), entranceChoice);
//        Division startDivision = null;
//
//        for (Action action : startActions) {
//            if (action instanceof PlayerMoveAction) {
//                startDivision = ((PlayerMoveAction) action).getTo();
//                break; // Encontrado o movimento inicial do jogador
//            }
//        }
//
//        if (startDivision == null) {
//            throw new InvalidAction("Failed to determine the starting division.");
//        }

        System.out.println("You are currently in the division: " + player.getCurrentDivision().getName());
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
                System.out.println("0. Exit the game");
            } else {
                System.out.println("Choose an action:");
                System.out.println("1. Move to another division (Scenario 2)");
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
                LinkedUnorderedList<Action> actions;
                switch (choice) {
                    case 1: // Ataque de inimigos ou movimentação
                        if (!currentEnemies.isEmpty()) {
                            actions = combatHandler.scenario1(player, building);
                        } else {
                            System.out.println("Choose a division to move:");
                            LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
                            int option = 1;
                            for (Division neighbor : neighbors) {
                                System.out.println(option++ + ". " + neighbor.getName());
                            }
                            int divisionOption = scanner.nextInt();
                            actions = combatHandler.scenario2(player, building, divisionOption);
                        }
                        processActions(actions, player);
                        break;

                    case 2: // Uso de item
                        actions = combatHandler.scenario4(player, building);
                        processActions(actions, player);
                        break;

                    case 3: // Procura pelo objetivo
                        if (player.getCurrentDivision().equals(goal.getDivision())) {
                            actions = combatHandler.scenario6(player, building, goal);
                        } else {
                            System.out.println("You are not in the objective division yet.");
                            continue;
                        }
                        processActions(actions, player);
                        break;

                    case 4: // Sair do edifício
                        System.out.println("Exiting Building");
                        if (goal.isRequired()) {
                            System.out.println("Congratulation!! \nMission completed successfully :)");
                        } else {
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
                System.out.println("Game Over! Tó Cruz is dead.");
                gameRunning = false;
            }

            // Exibe informações adicionais
            building.displayPaths(player.getCurrentDivision());
            building.displayItemsLocations();
            building.printMap(player.getCurrentDivision());
        }

        scanner.close();
    }

    private static void processActions(LinkedUnorderedList<Action> actions, ToCruz player) {
        for (Action action : actions) {
            if (action instanceof PlayerMoveAction) {
                PlayerMoveAction moveAction = (PlayerMoveAction) action;
                System.out.println("Player moved from " + moveAction.getFrom().getName() + " to " + moveAction.getTo().getName());
            } else if (action instanceof EnemyMoveAction) {
                System.out.println("Enemies moved.");
            } else if (action instanceof PlayerAtackAction) {
                System.out.println("Player attacked enemies.");
            } else if (action instanceof EnemyAtackAction) {
                System.out.println("Enemies attacked the player.");
            } else if (action instanceof ItemAction) {
                ItemAction itemAction = (ItemAction) action;
                System.out.println("Player used an item: " + itemAction.getItem().getItems());
            } else if (action instanceof GoalInteractionAction) {
                System.out.println("Player reached the goal.");
            }
        }
    }


    public static void logPathResult(PathResult result) {
        System.out.println("Path to goal:");
        for (Division division : result.getPathToGoal()) {
            System.out.print(division.getName() + " -> ");
        }
        System.out.println("Goal");

        System.out.println("Path to exit:");
        for (Division division : result.getPathToExit()) {
            System.out.print(division.getName() + " -> ");
        }
        System.out.println("Exit");

        System.out.println("Best life points remaining: " + result.getLifePointsRemaining());
    }
}
