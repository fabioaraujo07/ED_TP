package Game;

public class SimulationResult {

    private String missionVersion;
    private int remainingLifePoints;
    private int totalDamages;
    private int healthItemsUsed;
    private int vestUsed;
    private boolean missionSuccess;

    /**
     * Constructs a SimulationResult with the specified parameters.
     *
     * @param missionVersion the version of the mission
     * @param remainingLifePoints the remaining life points after the simulation
     * @param totalDamages the total damages taken during the simulation
     * @param healthItemsUsed the number of health items used during the simulation
     * @param vestUsed the number of vests used during the simulation
     * @param missionSuccess whether the mission was successful
     */
    public SimulationResult(String missionVersion, int remainingLifePoints, int totalDamages, int healthItemsUsed, int vestUsed, boolean missionSuccess) {
        this.missionVersion = missionVersion;
        this.remainingLifePoints = remainingLifePoints;
        this.totalDamages = totalDamages;
        this.healthItemsUsed = healthItemsUsed;
        this.vestUsed = vestUsed;
        this.missionSuccess = missionSuccess;
    }

    /**
     * Returns the version of the mission.
     *
     * @return the mission version
     */
    public String getMissionVersion() {
        return missionVersion;
    }

    /**
     * Returns the remaining life points after the simulation.
     *
     * @return the remaining life points
     */
    public int getRemainingLifePoints() {
        return remainingLifePoints;
    }

    /**
     * Returns the total damages taken during the simulation.
     *
     * @return the total damages
     */
    public int getTotalDamages() {
        return totalDamages;
    }

    /**
     * Returns the number of health items used during the simulation.
     *
     * @return the number of health items used
     */
    public int getHealthItemsUsed() {
        return healthItemsUsed;
    }

    /**
     * Returns the number of vests used during the simulation.
     *
     * @return the number of vests used
     */
    public int getVestUsed() {
        return vestUsed;
    }

    /**
     * Returns whether the mission was successful.
     *
     * @return true if the mission was successful, false otherwise
     */
    public boolean isMissionSuccess() {
        return missionSuccess;
    }

    /**
     * Sets whether the mission was successful.
     *
     * @param missionSuccess the new mission success status
     */
    public void setMissionSuccess(boolean missionSuccess) {
        this.missionSuccess = missionSuccess;
    }

    /**
     * Sets the version of the mission.
     *
     * @param missionVersion the new mission version
     */
    public void setMissionVersion(String missionVersion) {
        this.missionVersion = missionVersion;
    }

    /**
     * Sets the remaining life points after the simulation.
     *
     * @param remainingLifePoints the new remaining life points
     */
    public void setRemainingLifePoints(int remainingLifePoints) {
        this.remainingLifePoints = remainingLifePoints;
    }

    /**
     * Sets the total damages taken during the simulation.
     *
     * @param totalDamages the new total damages
     */
    public void setTotalDamages(int totalDamages) {
        this.totalDamages = totalDamages;
    }

    /**
     * Sets the number of health items used during the simulation.
     *
     * @param healthItemsUsed the new number of health items used
     */
    public void setHealthItemsUsed(int healthItemsUsed) {
        this.healthItemsUsed = healthItemsUsed;
    }

    /**
     * Sets the number of vests used during the simulation.
     *
     * @param vestUsed the new number of vests used
     */
    public void setVestUsed(int vestUsed) {
        this.vestUsed = vestUsed;
    }

    /**
     * Returns a string representation of the simulation result.
     *
     * @return a string representation of the simulation result
     */
    @Override
    public String toString() {
        return "Mission version: " + missionVersion + "\n"
                + "Remaining life points: " + remainingLifePoints + "\n"
                + "Total damages: " + totalDamages + "\n"
                + "Health items used: " + healthItemsUsed + "\n"
                + "Vest used: " + vestUsed + "\n"
                + "Mission success: " + missionSuccess + "\n";
    }
}
