package Classes;

import Collections.Stack.LinkedStack;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Exceptions.ItemNotFound;

public class ToCruz {

    private final int INITIAL_LIFE_POINTS = 100;

    private String name;
    private int lifePoints, power;
    private Division currentDivision;
    private LinkedStack<Item> bag;
    private final int POWER = 10;
    private boolean alive;

    public ToCruz(String name, Division currentDivision) {
        this.power = POWER;
        this.name = name;
        this.lifePoints = INITIAL_LIFE_POINTS;
        this.currentDivision = currentDivision;
        this.bag = new LinkedStack<>();
        this.alive = true;
    }

    public ToCruz(String name) {
        this.power = POWER;
        this.name = name;
        this.lifePoints = INITIAL_LIFE_POINTS;
        this.currentDivision = null;
        this.bag = new LinkedStack<>();
        this.alive = true;
    }

    public void addItem(Item item) {
        this.bag.push(item);
    }

    public void movePlayer(Map map, Division division) {
        Division current = getCurrentDivision();

        if (!map.getEdges(current).contains(division)) {
            throw new InvalidAction("You can't move this division, no connection");
        }
        setDivision(division);
    }

    private int LifePointsChanged(int points) {

        this.lifePoints += points;
        return this.lifePoints;
    }

    public void useItem(Item item) throws ItemNotFound {

        if (getBag().peek().equals(item)) {
            if (item.getItems().equals(Items.KIT_VIDA)) {
                if (this.lifePoints < INITIAL_LIFE_POINTS && LifePointsChanged(item.getPoints()) > INITIAL_LIFE_POINTS) {
                    this.lifePoints = INITIAL_LIFE_POINTS;
                }
                else if (this.lifePoints >= INITIAL_LIFE_POINTS) {
                    this.lifePoints -= item.getPoints();
                }
            }
            LifePointsChanged(item.getPoints());
            this.bag.pop();
        }
        else {
            throw new ItemNotFound("Item not found");
        }
    }

    public void attack(Enemy enemy) {

        if (this.alive) {
            int enemyPoints = enemy.getLifePoints();
            enemyPoints -= power;
            enemy.setLifePoints(enemyPoints);
            if (enemy.getLifePoints() <= 0){
                enemy.setAlive(false);
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getLifePoints() {
        return this.lifePoints;
    }

    public Division getCurrentDivision() {
        return this.currentDivision;
    }

    public LinkedStack<Item> getBag() {
        return this.bag;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setDivision(Division division) {
        this.currentDivision = division;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

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
