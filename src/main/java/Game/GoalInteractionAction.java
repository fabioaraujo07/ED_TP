package Game;

import Classes.Goal;
import Classes.ToCruz;
import Interfaces.Action;

/**
 * Represents an action where the player interacts with a goal.
 */
public class GoalInteractionAction implements Action {

    private ToCruz player;
    private Goal goal;
    private boolean executed;

    /**
     * Constructs a GoalInteractionAction with the specified player and goal.
     *
     * @param player the player character
     * @param goal the goal to interact with
     */
    public GoalInteractionAction(ToCruz player, Goal goal) {
        this.player = player;
        this.goal = goal;
        this.executed = false;
    }

    /**
     * Executes the goal interaction action.
     *
     * @return true if the action was executed, false otherwise
     */
    @Override
    public boolean execute() {
        if (!executed) {
            goal.setRequired(true);
            executed = true;
        }
        return executed;
    }

    /**
     * Returns a string representation of the goal interaction action.
     *
     * @return a string representation of the goal interaction action
     */
    @Override
    public String toString() {
        return player.getName() + " interacted with goal: " + goal.getType() + "\nGoal: " + goal.getType() + " successfully retrieved!\n";
    }

    /**
     * Returns the goal associated with this action.
     *
     * @return the goal associated with this action
     */
    public Goal getGoal() {
        return goal;
    }
}