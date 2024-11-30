package Classes;
import Enumerations.Items;

public class Item {
    private Division division;
    private Items items;
    private int points;

    public Item (Division division, Items items, int points) {
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

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Item{" +
                "division=" + division +
                ", items=" + items +
                ", points=" + points +
                '}';
    }
}

