package Classes;

import java.util.Objects;

public class Enemy {

    private int lifePoints;
    private int power;
    private String name;
    private boolean alive;

    /**
     * Constructs an Enemy with the specified power and name.
     *
     * @param power the power of the enemy
     * @param name the name of the enemy
     */
    public Enemy(int power, String name) {
        this.power = power;
        this.lifePoints = power;
        this.name = name;
        this.alive = true;
    }
    /**
     * Returns the power of the enemy.
     *
     * @return the power of the enemy
     */
    public int getPower() {
        return power;
    }

    /**
     * Returns the name of the enemy.
     *
     * @return the name of the enemy
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the life points of the enemy.
     *
     * @return the life points of the enemy
     */
    public int getLifePoints() {
        return lifePoints;
    }

    /**
     * Sets the life points of the enemy.
     *
     * @param lifePoints the new life points of the enemy
     */
    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    /**
     * Attacks the player, reducing the player's life points by the enemy's power.
     *
     * @param player the player to attack
     */
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

    /**
     * Returns whether the enemy is alive.
     *
     * @return true if the enemy is alive, false otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the alive status of the enemy.
     *
     * @param alive the new alive status of the enemy
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Checks if this enemy is equal to another object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enemy enemy = (Enemy) o;
        return power == enemy.power && Objects.equals(name, enemy.name);
    }

    /**
     * Returns a string representation of the enemy.
     *
     * @return a string representation of the enemy
     */
    @Override
    public String toString() {
        return "Enemy{" +
                "power=" + power +
                ", name='" + name + '\'' +
                '}';
    }
}
