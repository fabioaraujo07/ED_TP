package Classes;

import Collections.Stack.LinkedStack;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Exceptions.ItemNotFound;

/**
 * Represents the player character, Tó Cruz, in the game.
 */
public class ToCruz {

    private final int INITIAL_LIFE_POINTS = 100;

    private String name;
    private int lifePoints, power;
    private Division currentDivision;
    private LinkedStack<Item> bag;
    private final int POWER = 10;
    private boolean alive;

    /**
     * Constructs a new Tó Cruz with the specified name and initial division.
     *
     * @param name the name of the player
     * @param currentDivision the initial division of the player
     */
    public ToCruz(String name, Division currentDivision) {
        this.power = POWER;
        this.name = name;
        this.lifePoints = INITIAL_LIFE_POINTS;
        this.currentDivision = currentDivision;
        this.bag = new LinkedStack<>();
        this.alive = true;
    }

    /**
     * Constructs a new Tó Cruz with the specified name.
     *
     * @param name the name of the player
     */
    public ToCruz(String name) {
        this.power = POWER;
        this.name = name;
        this.lifePoints = INITIAL_LIFE_POINTS;
        this.currentDivision = null;
        this.bag = new LinkedStack<>();
        this.alive = true;
    }

    /**
     * Adds an item to the player's bag.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        this.bag.push(item);
    }

    /**
     * Moves the player to a new division if there is a connection.
     *
     * @param map the map of the building
     * @param division the division to move to
     * @throws InvalidAction if there is no connection to the division
     */
    public void movePlayer(Map map, Division division) {
        Division current = getCurrentDivision();

        if (!map.getEdges(current).contains(division)) {
            throw new InvalidAction("You can't move this division, no connection");
        }
        setDivision(division);
    }

    /**
     * Changes the player's life points by the specified amount.
     *
     * @param points the amount to change the life points by
     * @return the new life points
     */
    private int LifePointsChanged(int points) {
        this.lifePoints += points;
        return this.lifePoints;
    }

    /**
     * Uses an item from the player's bag.
     *
     * @param item the item to use
     * @throws ItemNotFound if the item is not found in the bag
     */
    public void useItem(Item item) throws ItemNotFound {
        if (getBag().peek().equals(item)) {
            if (item.getItems().equals(Items.KIT_VIDA)) {
                if (this.lifePoints < INITIAL_LIFE_POINTS && LifePointsChanged(item.getPoints()) > INITIAL_LIFE_POINTS) {
                    this.lifePoints = INITIAL_LIFE_POINTS;
                } else if (this.lifePoints >= INITIAL_LIFE_POINTS) {
                    this.lifePoints -= item.getPoints();
                }
            }
            LifePointsChanged(item.getPoints());
            this.bag.pop();
        } else {
            throw new ItemNotFound("Item not found");
        }
    }

    /**
     * Attacks an enemy, reducing its life points.
     *
     * @param enemy the enemy to attack
     */
    public void attack(Enemy enemy) {
        if (this.alive) {
            int enemyPoints = enemy.getLifePoints();
            enemyPoints -= power;
            enemy.setLifePoints(enemyPoints);
            if (enemy.getLifePoints() <= 0) {
                enemy.setAlive(false);
            }
        }
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the life points of the player.
     *
     * @return the life points of the player
     */
    public int getLifePoints() {
        return this.lifePoints;
    }

    /**
     * Returns the current division of the player.
     *
     * @return the current division of the player
     */
    public Division getCurrentDivision() {
        return this.currentDivision;
    }

    /**
     * Returns the bag of the player.
     *
     * @return the bag of the player
     */
    public LinkedStack<Item> getBag() {
        return this.bag;
    }

    /**
     * Returns whether the player is alive.
     *
     * @return true if the player is alive, false otherwise
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Sets the current division of the player.
     *
     * @param division the new division
     */
    public void setDivision(Division division) {
        this.currentDivision = division;
    }

    /**
     * Sets the life points of the player.
     *
     * @param lifePoints the new life points
     */
    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    /**
     * Sets whether the player is alive.
     *
     * @param alive the new alive status
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Returns a string representation of the player.
     *
     * @return a string representation of the player
     */
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", LifePoints=" + lifePoints +
                ", Division='" + currentDivision + '\'' +
                ", Bag=" + bag +
                '}';
    }
}