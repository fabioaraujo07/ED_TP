package Game;

public class SimulationResult {

    private String missionVersion;
    private int remainingLifePoints;
    private int totalDamages;
    private int healthItemsUsed;
    private int vestUsed;
    private boolean missionSuccess;

    public SimulationResult(String missionVersion, int remainingLifePoints, int totalDamages, int healthItemsUsed, int vestUsed, boolean missionSuccess) {
        this.missionVersion = missionVersion;
        this.remainingLifePoints = remainingLifePoints;
        this.totalDamages = totalDamages;
        this.healthItemsUsed = healthItemsUsed;
        this.vestUsed = vestUsed;
        this.missionSuccess = missionSuccess;
    }

    public String getMissionVersion() {
        return missionVersion;
    }

    public int getRemainingLifePoints() {
        return remainingLifePoints;
    }

    public int getTotalDamages() {
        return totalDamages;
    }

    public int getHealthItemsUsed() {
        return healthItemsUsed;
    }

    public int getVestUsed() {
        return vestUsed;
    }

    public boolean isMissionSuccess() {
        return missionSuccess;
    }

    public void setMissionSuccess(boolean missionSuccess) {
        this.missionSuccess = missionSuccess;
    }

    public void setMissionVersion(String missionVersion) {
        this.missionVersion = missionVersion;
    }

    public void setRemainingLifePoints(int remainingLifePoints) {
        this.remainingLifePoints = remainingLifePoints;
    }

    public void setTotalDamages(int totalDamages) {
        this.totalDamages = totalDamages;
    }

    public void setHealthItemsUsed(int healthItemsUsed) {
        this.healthItemsUsed = healthItemsUsed;
    }

    public void setVestUsed(int vestUsed) {
        this.vestUsed = vestUsed;
    }

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
