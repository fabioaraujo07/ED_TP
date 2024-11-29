package Classes;
import Enumerations.Items;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private Division division;
    private String items;
    private int recoveredPoints;
    private int extraPoints;

    public Item(Division division, String items, int recoveredPoints, int extraPoints) {
        this.division = division;
        this.items = items;
        this.recoveredPoints = recoveredPoints;
        this.extraPoints = extraPoints;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getRecoveredPoints() {
        return recoveredPoints;
    }

    public void setRecoveredPoints(int recoveredPoints) {
        this.recoveredPoints = recoveredPoints;
    }

    public int getExtraPoints() {
        return extraPoints;
    }

    public void setExtraPoints(int extraPoints) {
        this.extraPoints = extraPoints;
    }

}

