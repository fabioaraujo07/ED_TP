package Game;

import Classes.Enemy;
import Classes.ToCruz;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;
import Interfaces.Action;

/**
 * Represents an action where enemies attack the player.
 */
public class EnemyAtackAction implements Action {

    private ToCruz player;
    private LinkedUnorderedList<Enemy> enemies;
    private final LinkedUnorderedList<Enemy> attackingEnemies = new LinkedUnorderedList<>();

    /**
     * Constructs an EnemyAtackAction with the specified player.
     *
     * @param player the player character
     */
    public EnemyAtackAction(ToCruz player) {
        this.player = player;
        this.enemies = player.getCurrentDivision().getEnemies();
    }

    /**
     * Executes the enemy attack action.
     *
     * @return true if the attack was executed, false otherwise
     */
    @Override
    public boolean execute() {
        return EnemyAtack();
    }

    /**
     * Performs the enemy attack on the player.
     *
     * @return true if there are enemies to attack, false otherwise
     */
    private boolean EnemyAtack() {

        if (enemies.isEmpty()) {
            return false;
        }

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.attackPlayer(player);
                attackingEnemies.addToRear(enemy);
            }
        }
        return true;
    }

    /**
     * Returns the list of enemies.
     *
     * @return the list of enemies
     */
    public LinkedUnorderedList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the list of attacking enemies.
     *
     * @return the list of attacking enemies
     */
    public LinkedUnorderedList<Enemy> getAttackingEnemies() {
        return attackingEnemies;
    }
}