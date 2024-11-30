package Classes;
import Interfaces.EnemyADT;

import java.util.List;
import java.util.Objects;

public class Enemy implements EnemyADT {

    private int lifePoints;
    private int power;
    private String name;
    private Division division;


    public Enemy(int power, String name, Division division) {
        this.division = division;
        this.power = power;
        this.lifePoints = power;
        this.name = name;
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

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
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
                ", division=" + division +
                '}';
    }

    @Override
    public void moveNPC(ToCruz player, Map map, Division division) {

    }

    @Override
    public void attackPlayer(ToCruz player, Map map, Division division) {

    }
}
