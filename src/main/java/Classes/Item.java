package Classes;
import Enumerations.Items;

public class Item {

    private Items items;
    private int points;

    public Item (Items items, int points) {
        this.items = items;
        this.points = points;
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
                ", items=" + items +
                ", points=" + points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return items == item.items;
    }
}

