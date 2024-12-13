package Classes;
import Enumerations.Items;

public class Item {

    private Items items;
    private int points;

    /**
     * Constructs an Item with the specified type and points.
     *
     * @param items the type of the item
     * @param points the points of the item
     */
    public Item (Items items, int points) {
        this.items = items;
        this.points = points;
    }

    /**
     * Returns the type of the item.
     *
     * @return the type of the item
     */
    public Items getItems() {
        return items;
    }

    /**
     * Sets the type of the item.
     *
     * @param items the new type of the item
     */
    public void setItems(Items items) {
        this.items = items;
    }

    /**
     * Returns the points of the item.
     *
     * @return the points of the item
     */
    public int getPoints() {
        return points;
    }

    /**
     * Checks if this item is equal to another object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Item)) {
            return false;
        }
        return items == ((Item) o).items;
    }

    /**
     * Returns a string representation of the item.
     *
     * @return a string representation of the item
     */
    @Override
    public String toString() {
        return "Item{" +
                ", items=" + items +
                ", points=" + points +
                '}';
    }
}

