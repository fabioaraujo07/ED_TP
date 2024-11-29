package Classes;
import Enumerations.Items;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private Division division;
    private String items;
    private int points;

    public Item (Division division, String items, int points) {
        this.division = division;
        this.items = items;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

