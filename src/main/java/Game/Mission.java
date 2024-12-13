package Game;

import Classes.*;
import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Interfaces.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Mission {

    private static SimulationResult currentResult;
    private static boolean missionSuccess = false;

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

        System.out.println("You are currently in the division: " + player.getCurrentDivision().getName());
        System.out.println("Objective: " + goal.getType() + " in the division " + goal.getDivision().getName());

        currentResult = new SimulationResult("Pata de Coelho", player.getLifePoints(), 0, 0, 0, missionSuccess);

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
                        if (goal.isRequired() && player.isAlive()) {
                            System.out.println("Congratulation!! \nMission completed successfully :)");
                            missionSuccess = true;
                        } else {
                            System.out.println("Oh no!! \nYou didn't reach the goal. Mission failed :(");
                            missionSuccess = false;
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

        currentResult.setMissionSuccess(missionSuccess);
        saveResultToJSON(currentResult);

        scanner.close();
    }

    private static void processActions(LinkedUnorderedList<Action> actions, ToCruz player) {

        for (Action action : actions) {
            if (action instanceof PlayerAtackAction) {
                PlayerAtackAction attackAction = (PlayerAtackAction) action;
                for (Enemy enemy : attackAction.getAttackedEnemies()) {
                    currentResult.setTotalDamages(currentResult.getTotalDamages() + enemy.getPower());
                    System.out.println(player.getName() + " attacked " + enemy.getName() +
                            ". Enemy remaining life: " + enemy.getLifePoints());
                }
            } else if (action instanceof EnemyAtackAction) {
                EnemyAtackAction attackAction = (EnemyAtackAction) action;
                for (Enemy enemy : attackAction.getAttackingEnemies()) {
                    System.out.println(enemy.getName() + " attacked " + player.getName() +
                            ". To Cruz remaining life: " + player.getLifePoints());
                }
            } else if (action instanceof EnemyMoveAction) {
                EnemyMoveAction moveAction = (EnemyMoveAction) action;
                Iterator<Division> fromIterator = moveAction.getFrom().iterator();
                Iterator<Division> toIterator = moveAction.getTo().iterator();
                while (fromIterator.hasNext() && toIterator.hasNext()) {
                    Division from = fromIterator.next();
                    Division to = toIterator.next();
                    System.out.println("Enemy moved from " + from.getName() + " to " + to.getName());
                }
            } else if (action instanceof GoalInteractionAction) {
                GoalInteractionAction goalAction = (GoalInteractionAction) action;
                System.out.println("Objective reached: " + goalAction.getGoal().getType());
            } else if (action instanceof ItemAction) {
                ItemAction itemAction = (ItemAction) action;
                if (itemAction.getItem().getItems().equals(Items.KIT_VIDA)) {
                    currentResult.setHealthItemsUsed(currentResult.getHealthItemsUsed() + 1);
                    System.out.println(player.getName() + " used a health item.");
                } else if (itemAction.getItem().getItems().equals(Items.COLETE)) {
                    currentResult.setVestUsed(currentResult.getVestUsed() + 1);
                    System.out.println(player.getName() + " used a vest.");
                }
            }
        }

        if (player.getCurrentDivision().getItems().equals(Items.COLETE)) {
            currentResult.setVestUsed(currentResult.getVestUsed() + 1);
            System.out.println(player.getName() + " found and used a vest in the division.");
        }

        currentResult.setRemainingLifePoints(player.getLifePoints());// Atualiza os pontos de vida restantes
    }

    public static void saveResultToJSON(SimulationResult result) {
        String filename = "src/main/resources/SimulationResults.json";
        JSONArray resultsList = new JSONArray();

        // Carregar resultados existentes
        try (FileReader reader = new FileReader(filename)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                resultsList = (JSONArray) obj;
            }
        } catch (IOException e) {
            // Arquivo não existe, criar novo
            System.out.println("File not found, creating a new one.");
        } catch (ParseException e) {
            System.out.println("File is empty or malformed, starting with an empty list.");
        }

        // Adicionar novo resultado à lista de JSONs
        JSONObject resultDetails = new JSONObject();
        resultDetails.put("missionVersion", result.getMissionVersion());
        resultDetails.put("remainingLifePoints", result.getRemainingLifePoints());
        resultDetails.put("totalDamageDealt", result.getTotalDamages());
        resultDetails.put("healthItemsUsed", result.getHealthItemsUsed());
        resultDetails.put("vestsUsed", result.getVestUsed());
        resultDetails.put("missionSuccess", result.isMissionSuccess());

        resultsList.add(resultDetails);

        // Escrever os resultados no arquivo
        try (FileWriter file = new FileWriter(filename)) {
            file.write(resultsList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
        System.out.println();

        System.out.println("Best life points remaining: " + result.getLifePointsRemaining());
    }
}