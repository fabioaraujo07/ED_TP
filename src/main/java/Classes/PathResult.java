package Classes;

import Collections.Linked.LinkedUnorderedList;

/**
 * Represents the result of a pathfinding operation in the game.
 */
public class PathResult {

    private LinkedUnorderedList<Division> pathToGoal;
    private LinkedUnorderedList<Division> pathToExit;
    private int lifePointsRemaining;
    private boolean missionSuccess;

    /**
     * Constructs a PathResult with the specified paths, life points remaining, and mission success status.
     *
     * @param pathToGoal the path to the goal
     * @param pathToExit the path to the exit
     * @param lifePointsRemaining the remaining life points
     * @param missionSuccess the mission success status
     */
    public PathResult(LinkedUnorderedList<Division> pathToGoal, LinkedUnorderedList<Division> pathToExit, int lifePointsRemaining, boolean missionSuccess) {
        this.pathToGoal = pathToGoal;
        this.pathToExit = pathToExit;
        this.lifePointsRemaining = lifePointsRemaining;
        this.missionSuccess = missionSuccess;
    }

    /**
     * Returns the path to the goal.
     *
     * @return the path to the goal
     */
    public LinkedUnorderedList<Division> getPathToGoal() {
        return pathToGoal;
    }

    /**
     * Returns the path to the exit.
     *
     * @return the path to the exit
     */
    public LinkedUnorderedList<Division> getPathToExit() {
        return pathToExit;
    }

    /**
     * Returns the remaining life points.
     *
     * @return the remaining life points
     */
    public int getLifePointsRemaining() {
        return lifePointsRemaining;
    }

    /**
     * Returns whether the mission was successful.
     *
     * @return true if the mission was successful, false otherwise
     */
    public boolean isMissionSuccess() {
        return missionSuccess;
    }
}