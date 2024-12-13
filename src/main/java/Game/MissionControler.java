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

    /**
     * Constructs a MissionControler.
     */
    public MissionControler() {
        this.trajectory = new LinkedUnorderedList<>();
        this.missionSuccess = false;
    }

    /**
     * Selects a mission based on the given choice.
     *
     * @param missionChoice the mission choice
     * @return true if the mission was successfully selected, false otherwise
     */
    public boolean selectMission(int missionChoice) {
        String selectedMissionFile;

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

    /**
     * Returns the file path of the selected mission.
     *
     * @return the file path of the selected mission
     */
    public String getSelectedMissionFile() {
        return building.getFilename();
    }

    /**
     * Starts the automatic game simulation.
     */
    public void startAutomaticGame() {
        AutomaticGame automaticGame = new AutomaticGame();
        PathResult result = automaticGame.simulatePath(player, building.getInAndOut(), goal, building.getMap());
        logPathResult(result);
    }

    /**
     * Starts the manual game simulation.
     *
     * @param scanner the scanner for user input
     */
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

    /**
     * Handles the player's actions during the manual game.
     *
     * @param scanner the scanner for user input
     * @param manualGame the manual game instance
     * @return true if the game should continue, false otherwise
     */
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
                        actions = manualGame.scenario6(player, goal);
                        processActions(actions);
                    } else {
                        System.out.println("You are not in the objective division yet.");
                    }
                    break;
                case 4:
                    return handleBuildingExit();
                case 0:
                    System.out.println("Exiting the game. See you next time!");
                    currentResult = null;
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

    /**
     * Displays the action menu based on the presence of enemies.
     *
     * @param currentEnemies the list of enemies in the current division
     */
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

    /**
     * Prompts the user to choose a division to move to.
     *
     * @param scanner the scanner for user input
     * @return the selected division option
     */
    private int chooseDivision(Scanner scanner) {
        System.out.println("Choose a division to move:");
        LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(player.getCurrentDivision());
        int option = 1;
        for (Division neighbor : neighbors) {
            System.out.println(option++ + ". " + neighbor.getName());
        }
        return scanner.nextInt();
    }

    /**
     * Handles the player's exit from the building.
     *
     * @return false to indicate the game should end
     */
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

    /**
     * Displays the player's trajectory.
     */
    private void displayTrajectory() {
        System.out.println("Trajectory:");
        for (Division division : trajectory) {
            System.out.print(" -> " + division.getName());
        }
        System.out.println();
    }

    /**
     * Finalizes the game and saves the results.
     */
    private void finalizeGame() {
        if (currentResult != null) {
            currentResult.setMissionSuccess(missionSuccess);
            Export.saveActionsToJSON(currentResult, resultsFilename);
            Export.savePathToJSON(codMission, trajectory, "src/main/resources/Trajectory.json");
        }
    }

    /**
     * Processes the list of actions.
     *
     * @param actions the list of actions to process
     */
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
                    if (from.equals(to)) {
                        System.out.println("Enemy stayed in " + from.getName());
                    } else {
                        System.out.println("Enemy moved from " + from.getName() + " to " + to.getName());
                    }
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

    /**
     * Logs the path result of the automatic game simulation.
     *
     * @param result the path result
     */
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

    /**
     * Finds the nearest recovery kit from the given start division.
     *
     * @param start the start division
     * @param map the map of divisions
     * @return the nearest recovery kit division, or null if not found
     */
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

    /**
     * Displays the best paths to the goal and the nearest recovery kit.
     *
     * @param start the start division
     * @param goal the goal
     * @param map the map of divisions
     */
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

    /**
     * Displays the locations of recovery kits and vests in the map.
     *
     * @param map the map of divisions
     */
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

    /**
     * Returns the player.
     *
     * @return the player
     */
    public ToCruz getPlayer() {
        return player;
    }

    /**
     * Prints the map of the building.
     *
     * @param currentDivision the current division of the player
     */
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
