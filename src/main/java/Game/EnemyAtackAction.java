package Game;

import Classes.Enemy;
import Classes.ToCruz;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;
import Interfaces.Action;

public class EnemyAtackAction implements Action {

    private ToCruz player;
    private LinkedUnorderedList<Enemy> enemies;
    boolean atack;

    public EnemyAtackAction(ToCruz player) {
        this.player = player;
        this.enemies = player.getCurrentDivision().getEnemies();
        this.atack = false;
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
            }
        }
        atack = true;
        return atack;
    }

    public LinkedUnorderedList<Enemy> getEnemies() {
        return enemies;
    }
}
