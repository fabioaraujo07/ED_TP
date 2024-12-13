package Game;

import Classes.*;
import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Interfaces.Action;

import java.util.Iterator;

public class ManualGame {

    public ManualGame() {
    }

    // Cenário 1: Ataque de inimigos
    public LinkedUnorderedList<Action> scenario1(ToCruz player, Building building) throws InvalidAction {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Action playerAction = new PlayerAtackAction(player);
        Action enemyAction = new EnemyAtackAction(player);

        try {
            playerAction.execute();
            actions.addToRear(playerAction);
            if (enemyAction.execute()) {
                actions.addToRear(enemyAction);
                enemyAction = new EnemyMoveAction(player, building);
                enemyAction.execute();
                actions.addToRear(enemyAction);
            }
        } catch (InvalidAction e) {
        }

        if (!player.isAlive()) {
            throw new InvalidAction("To Cruz is dead!!\nMission Failed");
        }
        return actions;
    }

    // Cenário 2: Movimentação do jogador
    public LinkedUnorderedList<Action> scenario2(ToCruz player, Building building, int option) throws InvalidAction {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Action playerAction = new PlayerMoveAction(player, building, option);
        Action enemyAction = new EnemyMoveAction(player, building);
        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();

        try {
            playerAction.execute();
            actions.addToRear(playerAction);
            enemyAction.execute();
            actions.addToRear(enemyAction);
        } catch (InvalidAction e) {
        }

        if (!player.isAlive()) {
            throw new InvalidAction("To Cruz is dead!!\nMission Failed");
        }
        return actions;
    }

    // Cenário 3: Movimentação dos inimigos
    public LinkedUnorderedList<Action> scenario3(ToCruz player, Building building) {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Action enemyAction = new EnemyMoveAction(player, building);

        try {
            enemyAction.execute();
            actions.addToRear(enemyAction);
        } catch (InvalidAction e) {
        }
        return actions;
    }

    // Cenário 4: Uso de item pelo jogador
    public LinkedUnorderedList<Action> scenario4(ToCruz player, Building building) throws InvalidAction {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        ItemAction itemAction = new ItemAction(player);

        if (!itemAction.execute()) {
            throw new InvalidAction("No items available in the bag.");
        }

        actions.addToRear(itemAction);
        LinkedUnorderedList<Action> scenario3Actions = scenario3(player, building);
        for (Action action : scenario3Actions) {
            actions.addToRear(action);
        }

        return actions;
    }

    // Cenário 5: Jogador encontra o objetivo, mas há inimigos
    public LinkedUnorderedList<Action> scenario5(ToCruz player, Building building, Goal goal) throws InvalidAction {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();

        GoalInteractionAction playerAction = new GoalInteractionAction(player, goal);
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        LinkedUnorderedList<Action> scenario1Actions = scenario1(player, building);
        for (Action action : scenario1Actions) {
            actions.addToRear(action);
        }

        playerAction.execute();
        actions.addToRear(playerAction);
        return actions;
    }

    // Cenário 6: Jogador encontra o objetivo sem inimigos
    public LinkedUnorderedList<Action> scenario6(ToCruz player, Building building, Goal goal) {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        GoalInteractionAction playerAction = new GoalInteractionAction(player, goal);
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        playerAction.execute();
        actions.addToRear(playerAction);
        return actions;
    }

    public LinkedUnorderedList<Action> startDivision(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Division targetDivision = findDivisionByOption(neighbors, option);
        EnemyMoveAction enemyAction = new EnemyMoveAction(player, building);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }

        player.setDivision(targetDivision);
        enemyAction.execute();
        actions.addToRear(enemyAction);

        return actions;
    }

    // Escolher divisão com base na opção
    private Division findDivisionByOption(LinkedUnorderedList<Division> neighbors, int option) {
        int index = 1;
        for (Division division : neighbors) {
            if (index == option) {
                return division;
            }
            index++;
        }
        return null;
    }

    // Método movido: Encontrar o kit de recuperação mais próximo
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
}