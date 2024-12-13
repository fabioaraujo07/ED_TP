package Game;

import Classes.*;
import Collections.Graph.Graph;
import Collections.Linked.LinkedUnorderedList;

import java.util.Iterator;

/**
 * Represents an automatic game simulation.
 */
public class AutomaticGame {

    /**
     * Simulates the path for the player to reach the goal and exit the building.
     *
     * @param player the player character
     * @param entries the list of possible entry divisions
     * @param goal the goal division
     * @param map the map of the building
     * @return the result of the pathfinding operation
     */
    public PathResult simulatePath(ToCruz player, LinkedUnorderedList<Division> entries, Goal goal, Graph<Division> map) {
        LinkedUnorderedList<Division> bestPathToGoal = null;
        LinkedUnorderedList<Division> bestPathToExit = null;
        int bestLifePointsRemaining = 0;

        for (Division entry : entries) {
            LinkedUnorderedList<Division> tempPathToGoal = new LinkedUnorderedList<>();
            Iterator<Division> pathToGoal = map.iteratorShortestPath(entry, goal.getDivision());
            while (pathToGoal.hasNext()) {
                tempPathToGoal.addToRear(pathToGoal.next());
            }

            int damageToGoal = 0;
            LinkedUnorderedList<Division> clearedDivisions = new LinkedUnorderedList<>();
            for (Division division : tempPathToGoal) {
                damageToGoal += division.calculateTotalDamage();
                clearedDivisions.addToRear(division);
            }

            int lifePointsAfterGoal = player.getLifePoints() - damageToGoal;
            if (lifePointsAfterGoal <= 0) continue;

            LinkedUnorderedList<Division> tempPathToExit = new LinkedUnorderedList<>();
            Iterator<Division> pathToExit = map.iteratorShortestPath(goal.getDivision(), entry);
            while (pathToExit.hasNext()) {
                tempPathToExit.addToRear(pathToExit.next());
            }

            int damageToExit = 0;
            for (Division division : tempPathToExit) {
                if (!clearedDivisions.contains(division)) {
                    damageToExit += division.calculateTotalDamage();
                }
            }

            int lifePointsRemaining = lifePointsAfterGoal - damageToExit;

            if (lifePointsRemaining > bestLifePointsRemaining) {
                bestLifePointsRemaining = lifePointsRemaining;
                bestPathToGoal = tempPathToGoal;
                bestPathToExit = tempPathToExit;
            }
        }

        boolean missionSuccess = bestLifePointsRemaining > 0;
        return new PathResult(bestPathToGoal, bestPathToExit, bestLifePointsRemaining, missionSuccess);
    }
}