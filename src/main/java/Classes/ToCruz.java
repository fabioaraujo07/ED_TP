package Classes;

import Collections.Stack.LinkedStack;
import Exceptions.ItemNotFound;
import Interfaces.PlayerADT;

public class ToCruz<T> implements PlayerADT<T> {
    private final int INITIAL_LIFE_POINTS = 100;

    private String name;
    private int lifePoints, power;
    private Division currentDivision;
    private LinkedStack<Item> bag;
    private final int POWER = 10;
    private boolean isAlive;

    public ToCruz(String name,Division currentDivision) {
        this.power = POWER;
        this.name = name;
        this.lifePoints = INITIAL_LIFE_POINTS;
        this.currentDivision = currentDivision;
        this.bag = new LinkedStack<>();
        this.isAlive = true;
    }

    @Override
    public void addItem(Item item) {
        bag.push(item);
        System.out.println("Item added to the bag");
    }

    private void removeItem(Item item) {
        bag.pop();
        System.out.println("Item removed from the bag");
    }

    // Tem ki djobi inda um lógica midjor, ainda ka sta funciona de midjor forma
    @Override
    public void movePlayer(Map map, Division division) {
        Division current = getCurrentDivision();

        if (map.getEdges(current).contains(division)) {
            moveDivision(division);
        }else {
            System.out.println("You can't move this division, no connection");
        }
    }

    public void moveDivision(Division division) {
        this.currentDivision = division;
        System.out.println("Player has moved to the division: " + currentDivision);
    }

    private int LifePointsChanged(int points) {
        this.lifePoints += points;
        System.out.println("The life points changed to " + lifePoints);
        return lifePoints;
    }

    @Override
    public void useItem(ToCruz player, Item item) throws ItemNotFound {
        if (player.getBag().peek().equals(item)) {
            player.LifePointsChanged(item.getPoints());

            player.removeItem(item);
            System.out.println("Player has used the item " + item);
        }else {
            throw new ItemNotFound("Item not found");
        }
    }

    @Override
    public void attack(Enemy enemy) {
        if (isAlive) {
            int enemyPoints = enemy.getLifePoints();
            enemyPoints -= power;
            enemy.setLifePoints(enemyPoints);
        }

    }

    public String getName() {
        return name;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public Division getCurrentDivision() {
        return currentDivision;
    }

    public LinkedStack<Item> getBag() {
        return bag;
    }

    public int getPower() {
        return power;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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
