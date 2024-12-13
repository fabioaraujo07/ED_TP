package Game;

import Classes.Enemy;
import Classes.ToCruz;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;
import Interfaces.Action;

public class EnemyAtackAction implements Action {

    private ToCruz player;
    private LinkedUnorderedList<Enemy> enemies;
    private final LinkedUnorderedList<Enemy> attackingEnemies = new LinkedUnorderedList<>();

    public EnemyAtackAction(ToCruz player) {
        this.player = player;
        this.enemies = player.getCurrentDivision().getEnemies();
    }

    @Override
    public boolean execute() {
        return EnemyAtack();
    }

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

    public LinkedUnorderedList<Enemy> getEnemies() {
        return enemies;
    }
    public LinkedUnorderedList<Enemy> getAttackingEnemies() {
        return attackingEnemies;
    }
}
