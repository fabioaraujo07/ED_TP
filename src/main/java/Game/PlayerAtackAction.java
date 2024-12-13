package Game;

import Classes.Enemy;
import Classes.ToCruz;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;
import Interfaces.Action;

public class PlayerAtackAction implements Action {

    private ToCruz player;
    private LinkedUnorderedList<Enemy> enemies;
    private final LinkedUnorderedList<Enemy> attackedEnemies = new LinkedUnorderedList<>();

    /**
     * Constructs a PlayerAtackAction with the specified player.
     *
     * @param player the player performing the action
     */
    public PlayerAtackAction(ToCruz player) {
        this.player = player;
        this.enemies = player.getCurrentDivision().getEnemies();
    }

    /**
     * Executes the player attack action.
     *
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute() {
        return PlayerAtack();
    }

    /**
     * Attacks the enemies in the current division.
     *
     * @return true if the attack was successful, false otherwise
     * @throws InvalidAction if there are no enemies to attack
     */
    private boolean PlayerAtack() {

        LinkedUnorderedList<Enemy> defeatedEnemies = new LinkedUnorderedList<>();

        if (enemies.isEmpty()) {
            throw new InvalidAction("No enemies to attack in the current division.");
        }

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                player.attack(enemy);
                attackedEnemies.addToRear(enemy);
            }

            if (!enemy.isAlive()) {
                defeatedEnemies.addToRear(enemy);
            }
        }

        if (defeatedEnemies.size() == enemies.size()) {
            for (Enemy defeatedEnemy : defeatedEnemies) {
                enemies.remove(defeatedEnemy);
            }
        }
        return true;
    }

    /**
     * Returns the list of enemies in the current division.
     *
     * @return the list of enemies
     */
    public LinkedUnorderedList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the list of enemies that were attacked.
     *
     * @return the list of attacked enemies
     */
    public LinkedUnorderedList<Enemy> getAttackedEnemies() {
        return attackedEnemies;
    }
}
