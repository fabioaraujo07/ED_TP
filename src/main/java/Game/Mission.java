package Game;

import Classes.*;

import Collections.Array.UnorderedArrayList;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;
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
        Division startDivision = combatHandler.startDivision(player, building, building.getInAndOut(), entranceChoice);
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
                            LinkedUnorderedList<Action> actions = combatHandler.scenario1(player, building);
                            for (Action action : actions) {
                                if (action instanceof PlayerAtackAction) {
                                    currentEnemies = ((PlayerAtackAction) action).getEnemies();
                                    for (Enemy enemy : currentEnemies) {
                                        if (enemy.isAlive()) {
                                            System.out.println(player.getName() + " attacked " + enemy.getName() + ". To Cruz remaining life: " + enemy.getLifePoints());
                                        } else {
                                            System.out.println(enemy.getName() + " was defeated.");
                                        }
                                    }
                                    System.out.println("All enemies were defeated.");
                                } else if (action instanceof EnemyAtackAction) {
                                        currentEnemies = ((EnemyAtackAction) action).getEnemies();
                                        for (Enemy enemy : currentEnemies) {
                                            if (enemy.isAlive()) {
                                                System.out.println(enemy.getName() + " counter-attacked " + player.getName() + ". To Cruz remaining life: " + player.getLifePoints() + "\n");
                                            }
                                        }
                                } else if (action instanceof EnemyMoveAction) {
                                    UnorderedArrayList<Division> movedFrom = ((EnemyMoveAction) action).getFrom();
                                    UnorderedArrayList<Division> movedTo = ((EnemyMoveAction) action).getTo();
                                    for (Division to : movedTo) {
                                        for (Enemy enemy : to.getEnemies()) {
                                            if (movedFrom.contains(to)) {
                                                System.out.println(enemy.getName() + " stayed at " + to.getName());
                                            } else {
                                                System.out.println(enemy.getName() + " moved to " + to.getName());
                                                if (to.equals(player.getCurrentDivision())) {
                                                    System.out.println(enemy.getName() + " encountered " + player.getName() + " and is attacking!");
                                                    enemy.attackPlayer(player);
                                                    System.out.println(enemy.getName() + " attacked causing " + enemy.getPower() + " damage to " + player.getName() + ".");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            int option = 1;
                            LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
                            System.out.println("Choose a division to move:");
                            for (Division neighbor : neighbors) {
                                System.out.println(option++ + ". " + neighbor.getName());
                            }
                            int divisionOption = scanner.nextInt();
                            LinkedUnorderedList<Action> actions = combatHandler.scenario2(player, building, divisionOption);
                            for (Action action : actions) {
                                if (action instanceof PlayerMoveAction) {
                                    Division from = ((PlayerMoveAction) action).getFrom();
                                    Division to = ((PlayerMoveAction) action).getTo();
                                    System.out.println(player.getName() + " moved from " + from.getName() + " to " + to.getName());
                                } else if (action instanceof EnemyMoveAction) {
                                    UnorderedArrayList<Division> movedFrom = ((EnemyMoveAction) action).getFrom();
                                    UnorderedArrayList<Division> movedTo = ((EnemyMoveAction) action).getTo();
                                    for (Division to : movedTo) {
                                        for (Enemy enemy : to.getEnemies()) {
                                            if (movedFrom.contains(to)) {
                                                System.out.println(enemy.getName() + " stayed at " + to.getName());
                                            } else {
                                                System.out.println(enemy.getName() + " moved to " + to.getName());
                                                if (to.equals(player.getCurrentDivision())) {
                                                    System.out.println(enemy.getName() + " encountered " + player.getName() + " and is attacking!");
                                                    enemy.attackPlayer(player);
                                                    System.out.println(enemy.getName() + " attacked causing " + enemy.getPower() + " damage to " + player.getName() + ".");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    case 2: // Uso de item
                        String log = combatHandler.scenario4(player, building);
                        System.out.println(log);
                        break;

                    case 3: // Procura pelo objetivo
                        if (player.getCurrentDivision().equals(goal.getDivision())) {
                            String log1 = combatHandler.scenario6(player, building, goal);
                            System.out.println(log1);
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
            building.displayItemsLocations();
            building.printMap(player.getCurrentDivision());
        }

        scanner.close();
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