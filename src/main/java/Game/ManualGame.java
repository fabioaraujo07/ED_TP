package Game;

import Classes.*;
import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Interfaces.Action;

import java.util.Iterator;

/**
 * Represents a manual game simulation with various scenarios.
 */
public class ManualGame {

    /**
     * Constructs a ManualGame instance.
     */
    public ManualGame() {
    }

    /**
     * Scenario 1: Enemy attack.
     *
     * @param player the player character
     * @param building the building where the action takes place
     * @return the list of actions performed
     * @throws InvalidAction if the player is dead
     */
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

    /**
     * Scenario 2: Player movement.
     *
     * @param player the player character
     * @param building the building where the action takes place
     * @param option the movement option
     * @return the list of actions performed
     * @throws InvalidAction if the player is dead
     */
    public LinkedUnorderedList<Action> scenario2(ToCruz player, Building building, int option) throws InvalidAction {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Action playerAction = new PlayerMoveAction(player, building, option);
        Action enemyAction = new EnemyMoveAction(player, building);

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

    /**
     * Scenario 3: Enemy movement.
     *
     * @param player the player character
     * @param building the building where the action takes place
     * @return the list of actions performed
     */
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

    /**
     * Scenario 4: Player uses an item.
     *
     * @param player the player character
     * @param building the building where the action takes place
     * @return the list of actions performed
     * @throws InvalidAction if no items are available in the bag
     */
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

    /**
     * Scenario 5: Player finds the goal but there are enemies.
     *
     * @param player the player character
     * @param building the building where the action takes place
     * @param goal the goal to interact with
     * @return the list of actions performed
     * @throws InvalidAction if the player has not found the goal yet
     */
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

    /**
     * Scenario 6: Player finds the goal without enemies.
     *
     * @param player the player character
     * @param goal the goal to interact with
     * @return the list of actions performed
     * @throws InvalidAction if the player has not found the goal yet
     */
    public LinkedUnorderedList<Action> scenario6(ToCruz player, Goal goal) {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        GoalInteractionAction playerAction = new GoalInteractionAction(player, goal);
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        playerAction.execute();
        actions.addToRear(playerAction);
        return actions;
    }

    /**
     * Starts the division with the specified option.
     *
     * @param player the player character
     * @param building the building where the action takes place
     * @param neighbors the list of neighboring divisions
     * @param option the selected option
     * @return the list of actions performed
     * @throws InvalidAction if the selected option is invalid
     */
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

    /**
     * Finds the division based on the selected option.
     *
     * @param neighbors the list of neighboring divisions
     * @param option the selected option
     * @return the selected division, or null if the option is invalid
     */
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
}