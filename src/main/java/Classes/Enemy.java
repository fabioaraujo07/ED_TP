package Classes;
import Collections.Lists.LinkedUnorderedList;
import Interfaces.EnemyADT;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Enemy{

    private int lifePoints;
    private int power;
    private String name;
    private boolean alive;

    public Enemy(int power, String name) {
        this.power = power;
        this.lifePoints = power;
        this.name = name;
        this.alive = true;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public void attackPlayer(ToCruz player) {
        if (alive) {
            int playerPoints = player.getLifePoints();
            playerPoints -= this.power;
            player.setLifePoints(playerPoints);
            if (player.getLifePoints() <= 0){
                player.setAlive(false);
            }
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        alive = alive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enemy enemy = (Enemy) o;
        return power == enemy.power && Objects.equals(name, enemy.name);
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "power=" + power +
                ", name='" + name + '\'' +
                '}';
    }
}
