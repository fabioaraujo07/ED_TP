package Game;

import Classes.Goal;
import Classes.ToCruz;
import Interfaces.Action;

public class GoalInteractionAction implements Action {

    private ToCruz player;
    private Goal goal;
    private boolean executed;

    public GoalInteractionAction(ToCruz player, Goal goal) {
        this.player = player;
        this.goal = goal;
        this.executed = false;
    }

    @Override
    public boolean execute() {
        if (!executed) {
            goal.setRequired(true);
            executed = true;
        }
        return executed;
    }

    @Override
    public String toString() {
        return player.getName() + " interacted with goal: " + goal.getType() + "\nGoal: " + goal.getType() + " successfully retrieved!\n";
    }

    public Goal getGoal() {
        return goal;
    }
}