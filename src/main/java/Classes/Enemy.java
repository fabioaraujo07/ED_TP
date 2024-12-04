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
    private boolean isAlive;

    public Enemy(int power, String name) {
        this.power = power;
        this.lifePoints = power;
        this.name = name;
        this.isAlive = true;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public void attackPlayer(ToCruz player) {
        if (isAlive) {
            int playerPoints = player.getLifePoints();
            playerPoints -= this.power;
            player.setLifePoints(playerPoints);
            if (player.getLifePoints() <= 0){
                player.setAlive(false);
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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
