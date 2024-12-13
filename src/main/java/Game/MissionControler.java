package Game;

import Classes.*;
import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Export.Export;
import Interfaces.Action;
import java.util.Iterator;
import java.util.Scanner;

public class MissionControler {

    private SimulationResult currentResult;
    private boolean missionSuccess;
    private String resultsFilename;
    private String codMission;
    private LinkedUnorderedList<Division> trajectory;
    private Building building;
    private ToCruz player;
    private Goal goal;

    public MissionControler() {
        this.trajectory = new LinkedUnorderedList<>();
        this.missionSuccess = false;
    }

    public boolean selectMission(int missionChoice) {
        String selectedMissionFile;
        String trajectoryFilename = "src/main/resources/Trajectory.json";

        switch (missionChoice) {
            case 1:
                selectedMissionFile = "src/main/resources/Missão_v1.json";
                resultsFilename = "src/main/resources/SimulationResults_v1.json";
                codMission = "Pata de Coelho";
                break;
            case 2:
                selectedMissionFile = "src/main/resources/Missão_v2.json";
                resultsFilename = "src/main/resources/SimulationResults_v2.json";
                codMission = "Operation Stealth";
                break;
            case 3:
                selectedMissionFile = "src/main/resources/Missão_v3.json";
                resultsFilename = "src/main/resources/SimulationResults_v3.json";
                codMission = "Infiltration";
                break;
            default:
                return false;
        }

        this.building = new Building(selectedMissionFile);
        this.goal = building.getGoal();
        this.player = new ToCruz("Tó Cruz");
        this.currentResult = new SimulationResult(codMission, player.getLifePoints(), 0, 0, 0, missionSuccess);
        return true;
    }

    public String getSelectedMissionFile() {
        return building.getFilename();
    }

    public void startAutomaticGame() {
        AutomaticGame automaticGame = new AutomaticGame();
        PathResult result = automaticGame.simulatePath(player, building.getInAndOut(), goal, building.getMap());
        logPathResult(result);
    }

    public void startManualGame(Scanner scanner) {
        ManualGame manualGame = new ManualGame();

        System.out.println("\nGame has started!");

        System.out.println("Choose entrance division:");
        int entranceOption = 1;
        for (Division entrance : building.getInAndOut()) {
            System.out.println(entranceOption++ + ". " + entrance.getName());
        }
        int entranceChoice = scanner.nextInt();
        manualGame.startDivision(player, building, building.getInAndOut(), entranceChoice);
        trajectory.addToRear(player.getCurrentDivision());
        System.out.println("You are currently in the division: " + player.getCurrentDivision().getName());
        System.out.println("Objective: " + goal.getType() + " in the division " + goal.getDivision().getName());

        boolean gameRunning = true;

        while (gameRunning) {
            gameRunning = handlePlayerActions(scanner, manualGame);
        }

        finalizeGame();
    }

    private boolean handlePlayerActions(Scanner scanner, ManualGame manualGame) {
        System.out.println("\n--------------------------------------");
        System.out.println("Current Division: " + player.getCurrentDivision().getName());
        System.out.println("Health: " + player.getLifePoints());
        System.out.println("Items in the Bag: " + player.getBag().size());
        System.out.println("--------------------------------------");

        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();

        displayActionMenu(currentEnemies);

        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            LinkedUnorderedList<Action> actions;
            switch (choice) {
                case 1:
                    if (!currentEnemies.isEmpty()) {
                        actions = manualGame.scenario1(player, building);
                    } else {
                        actions = manualGame.scenario2(player, building, chooseDivision(scanner));
                    }
                    processActions(actions);
                    break;
                case 2:
                    actions = manualGame.scenario4(player, building);
                    processActions(actions);
                    break;
                case 3:
                    if (player.getCurrentDivision().equals(goal.getDivision())) {
                        actions = manualGame.scenario6(player, building, goal);
                        processActions(actions);
                    } else {
                        System.out.println("You are not in the objective division yet.");
                    }
                    break;
                case 4:
                    return handleBuildingExit();
                case 0:
                    System.out.println("Exiting the game. See you next time!");
                    return false;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } catch (InvalidAction e) {
            System.out.println(e.getMessage());
        }

        if (!player.isAlive()) {
            System.out.println("Game Over! Tó Cruz is dead.");
            return false;
        }

        displayPaths(player.getCurrentDivision(), goal, building.getMap());
        displayItemsLocations(building.getMap());
        printMap(player.getCurrentDivision());

        return true;
    }

    private void displayActionMenu(LinkedUnorderedList<Enemy> currentEnemies) {
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
            System.out.println("4. Exit Building");
            System.out.println("0. Exit the game");
        }
    }

    private int chooseDivision(Scanner scanner) {
        System.out.println("Choose a division to move:");
        LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
        int option = 1;
        for (Division neighbor : neighbors) {
            System.out.println(option++ + ". " + neighbor.getName());
        }
        return scanner.nextInt();
    }

    private boolean handleBuildingExit() {
        System.out.println("Exiting Building");
        if (goal.isRequired() && player.isAlive()) {
            System.out.println("Congratulations! Mission completed successfully :)");
            missionSuccess = true;
        } else {
            System.out.println("Mission failed. You didn't reach the goal.");
            missionSuccess = false;
        }
        displayTrajectory();
        return false;
    }

    private void displayTrajectory() {
        System.out.println("Trajectory:");
        for (Division division : trajectory) {
            System.out.print(" -> " + division.getName());
        }
        System.out.println();
    }

    private void finalizeGame() {
        currentResult.setMissionSuccess(missionSuccess);
        Export.saveActionsToJSON(currentResult, resultsFilename);
        Export.savePathToJSON(codMission, trajectory, "src/main/resources/Trajectory.json");
    }

    private void processActions(LinkedUnorderedList<Action> actions) {
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
                }
            } else if (action instanceof PlayerMoveAction) {
                PlayerMoveAction moveAction = (PlayerMoveAction) action;
                trajectory.addToRear(moveAction.getTo());
                if (moveAction.isUsedItem()) {
                    currentResult.setVestUsed(currentResult.getVestUsed() + 1);
                    System.out.println(player.getName() + " used a vest.");
                }
                System.out.println(player.getName() + " moved to " + moveAction.getTo().getName());
            }
        }
    }

    private void logPathResult (PathResult result){
        System.out.println("Path to goal:");
        for (Division division : result.getPathToGoal()) {
            System.out.print(division.getName() + " -> ");
        }
        System.out.println("Goal");

        System.out.println("Path to exit:");
        for (Division division : result.getPathToExit()) {
            System.out.print(division.getName() + " -> ");
        }
        System.out.println();

        System.out.println("Best life points remaining: " + result.getLifePointsRemaining());
    }

    public Division findNearestRecoveryKit(Division start, Map<Division> map) {
        Iterator<Division> iterator = map.iteratorBFS(start);
        while (iterator.hasNext()) {
            Division division = iterator.next();
            LinkedUnorderedList<Item> items = division.getItems();
            for (Item item : items) {
                if (item.getItems() == Items.KIT_VIDA) {
                    return division;
                }
            }
        }
        return null;
    }

    public void displayPaths(Division start, Goal goal, Map<Division> map) {
        Iterator<Division> pathToGoal = map.iteratorShortestPath(start, goal.getDivision());
        Division nearestRecoveryKit = findNearestRecoveryKit(start, map);
        Iterator<Division> pathToRecoveryKit;

        if (nearestRecoveryKit != null) {
            pathToRecoveryKit = map.iteratorShortestPath(start, nearestRecoveryKit);
        } else {
            pathToRecoveryKit = new LinkedUnorderedList<Division>().iterator();
        }

        System.out.println("\nBest path to the goal:");
        while (pathToGoal.hasNext()) {
            System.out.print(" -> " + pathToGoal.next().getName());
        }

        System.out.println("\n\nBest path to the nearest recovery kit:");
        while (pathToRecoveryKit.hasNext()) {
            System.out.print(" -> " + pathToRecoveryKit.next().getName());
        }
        System.out.println();
    }

    public void displayItemsLocations(Map<Division> map) {
        System.out.println("\nLocations of recovery kits and vests:");

        for (Division division : map.getVertexes()) {
            LinkedUnorderedList<Item> items = division.getItems();
            for (Item item : items) {
                if (item.getItems() == Items.KIT_VIDA) {
                    System.out.println("Recovery kit found in: " + division.getName());
                } else if (item.getItems() == Items.COLETE) {
                    System.out.println("Vest found in: " + division.getName());
                }
            }
        }
    }
    public ToCruz getPlayer() {
        return player;
    }

    public Goal getGoal() {
        return goal;
    }

    public Building getBuilding() {
        return building;
    }


    public void printMap(Division currentDivision) {
        System.out.println("\nBuilding Map:");

        for (Division division : building.getMap().getVertexes()) {
            if (division.equals(currentDivision)) {
                System.out.print("* " + division.getName() + " * -> ");
            } else {
                System.out.print("  " + division.getName() + " -> ");
            }

            LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(division);
            for (Division neighbor : neighbors) {
                System.out.print(" -> " + neighbor.getName() + " ");
            }
            System.out.println();
        }
    }


}
