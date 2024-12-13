package Classes;

import Collections.Linked.LinkedUnorderedList;

public class PathResult {

    private LinkedUnorderedList<Division> pathToGoal;
    private LinkedUnorderedList<Division> pathToExit;
    private int lifePointsRemaining;
    private boolean missionSuccess;

    public PathResult(LinkedUnorderedList<Division> pathToGoal, LinkedUnorderedList<Division> pathToExit, int lifePointsRemaining, boolean missionSuccess, Division bestEntry) {
        this.pathToGoal = pathToGoal;
        this.pathToExit = pathToExit;
        this.lifePointsRemaining = lifePointsRemaining;
        this.missionSuccess = missionSuccess;
    }

    public LinkedUnorderedList<Division> getPathToGoal() {
        return pathToGoal;
    }

    public LinkedUnorderedList<Division> getPathToExit() {
        return pathToExit;
    }

    public int getLifePointsRemaining() {
        return lifePointsRemaining;
    }

    public boolean isMissionSuccess() {
        return missionSuccess;
    }
}