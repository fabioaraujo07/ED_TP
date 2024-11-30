package Classes;

import Collections.Stack.LinkedStack;
import Exceptions.ElementNotFound;
import Exceptions.ItemNotFound;
import Interfaces.Player;

public class ToCruz<T> implements Player<T> {
    private String name;
    private int lifePoints;
    private Division currentDivision;
    private LinkedStack<Item> bag;
    private final int INITIAL_LIFE_POINTS = 100;

    public ToCruz(String name,Division currentDivision) {
        this.name = name;
        this.lifePoints = INITIAL_LIFE_POINTS;
        this.currentDivision = currentDivision;
    }


    @Override
    public void addItem(Item item) {
        bag.push(item);
        System.out.println("Item added to the bag");
    }

    @Override
    public void removeItem(Item item) {
        bag.pop();
        System.out.println("Item removed from the bag");
    }

    // Tem ki djobi inda um lógica midjor
    @Override
    public void movePlayer(ToCruz player, Map map, Division division) {
        Division current = player.getCurrentDivision();

        if (map.getEdges(division).contains(current)) {
            player.moveDivision(current);
        }else {
            System.out.println("You can't move this division, no connection");
        }
    }

    public void moveDivision(Division division) {
        this.currentDivision = division;
        System.out.println("The division has moved division to the" + currentDivision);
    }

    @Override
    public int LifePointsChanged(int points) {
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
