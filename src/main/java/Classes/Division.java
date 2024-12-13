package Classes;
import Collections.Linked.LinkedUnorderedList;

/**
 * Represents a division in the game, containing enemies and items.
 */
public class Division {

    private String name;
    private LinkedUnorderedList<Enemy> enemies;
    private LinkedUnorderedList<Item> items;

    /**
     * Constructs a Division with the specified name.
     *
     * @param name the name of the division
     */
    public Division(String name) {
        this.enemies = new LinkedUnorderedList<>();
        this.items = new LinkedUnorderedList<>();
        this.name = name;
    }

    /**
     * Returns the name of the division.
     *
     * @return the name of the division
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of enemies in the division.
     *
     * @return the list of enemies
     */
    public LinkedUnorderedList<Enemy> getEnemies() {
        return enemies;
    }
    /**
     * Returns the list of items in the division.
     *
     * @return the list of items
     */
    public LinkedUnorderedList<Item> getItems() {
        return items;
    }

    /**
     * Adds an enemy to the division.
     *
     * @param enemy the enemy to add
     */
    public void addEnemy(Enemy enemy) {
        this.enemies.addToRear(enemy);
    }

    /**
     * Adds an item to the division.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        this.items.addToRear(item);
    }

    /**
     * Removes an enemy from the division.
     *
     * @param enemy the enemy to remove
     */
    public void removeEnemy(Enemy enemy){
        this.enemies.remove(enemy);
    }

    /**
     * Calculates the total damage of all enemies in the division.
     *
     * @return the total damage
     */
    public int calculateTotalDamage() {

        int totalDamage = 0;
        for (Enemy enemy : enemies) {
            totalDamage += enemy.getPower();
        }
        return totalDamage;
    }

    /**
     * Checks if this division is equal to another object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Division)) {
            return false;
        }
        return name.equalsIgnoreCase(((Division) o).name);
    }

    /**
     * Returns a string representation of the division.
     *
     * @return a string representation of the division
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Division{name='").append(name).append("', ");

        sb.append("enemies=[");
        for (Enemy enemy : enemies) {
            sb.append(enemy.getName()).append("(Power: ").append(enemy.getPower()).append("), ");
        }
        if (!enemies.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("], ");

        sb.append("items=[");
        for (Item item : items) {
            sb.append(item.getItems()).append("(Points: ").append(item.getPoints()).append("), ");
        }
        if (!items.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]}");

        return sb.toString();
    }
}
